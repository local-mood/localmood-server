package com.localmood.domain.space.service;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Collections;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.localmood.common.util.CheckScrapUtil;
import com.localmood.domain.review.entity.Review;
import com.localmood.domain.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import com.localmood.domain.curation.repository.CurationRepository;
import com.localmood.domain.member.entity.Member;
import com.localmood.domain.review.repository.ReviewImgRepository;
import com.localmood.domain.space.dto.SpaceDetailDto;
import com.localmood.domain.space.dto.SpaceRecommendDto;
import com.localmood.domain.space.dto.request.SpaceFilterRequest;
import com.localmood.domain.space.dto.request.SpaceSearchRequest;
import com.localmood.domain.space.dto.response.SpaceSearchResponse;
import com.localmood.domain.space.entity.Space;
import com.localmood.domain.space.entity.SpaceInfo;
import com.localmood.domain.space.entity.SpaceMenu;
import com.localmood.domain.space.repository.SpaceInfoRepository;
import com.localmood.domain.space.repository.SpaceMenuRepository;
import com.localmood.domain.space.repository.SpaceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpaceService {

	private final SpaceRepository spaceRepository;
	private final SpaceInfoRepository spaceInfoRepository;
	private final SpaceMenuRepository spaceMenuRepository;
	private final ReviewRepository reviewRepository;
	private final ReviewImgRepository reviewImgRepository;
	private final CurationRepository curationRepository;
	private final CheckScrapUtil checkScrapUtil;

	public Map<String, List<SpaceRecommendDto>> getSpaceRecommendList(Optional<Member> member) {
		String[] keywordArr = {"연인과의 데이트", "친구와의 만남", "왁자지껄 떠들 수 있는", "대화에 집중할 수 있는"};
		HashMap<String, List<SpaceRecommendDto>> spaceRecommendListMap = new HashMap<>();

		for (int i=0; i < keywordArr.length; i++) {
			var restaurantList = spaceRepository.findRestaurantRecommendByKeyword(keywordArr[i], member);
			var cafeList = spaceRepository.findCafeRecommendByKeyword(keywordArr[i], member);

			var mergedSpaceList = Stream.of(restaurantList, cafeList)
					.flatMap(x -> x.stream())
					.collect(Collectors.toList());

			spaceRecommendListMap.put(keywordArr[i], mergedSpaceList);
		}

		return spaceRecommendListMap;
	}

	public SpaceSearchResponse getSpaceSearchList(SpaceSearchRequest request, String sort, Optional<Member> member) {
		var spaceList = spaceRepository.findSpaceByName(request.getName(), sort, member);

		return SpaceSearchResponse.builder()
				.spaceCount(spaceList.size())
				.spaceList(spaceList)
				.build();
	}

	public SpaceSearchResponse getSpaceFilterList(SpaceFilterRequest request, String sort, Optional<Member> member) {
		var spaceList = spaceRepository.findSpaceByKeywords(
				request.getType(),
				request.getSubType(),
				request.getPurpose(),
				request.getMood(),
				request.getMusic(),
				request.getInterior(),
				request.getVisitor(),
				request.getOptServ(),
				request.getDish(),
				request.getDisDesc(),
				sort,
				member
		);

		return SpaceSearchResponse.builder()
				.spaceCount(spaceList.size())
				.spaceList(spaceList)
				.build();
	}

	public HashMap<String,Object> getSpaceDetail(Long spaceId, Optional<Member> member) {
		HashMap<String, Object> spaceDetailMap = new HashMap<>();

		Space space = spaceRepository.findById(spaceId).orElseThrow();
		SpaceInfo spaceInfo = spaceInfoRepository.findBySpaceId(spaceId).orElseThrow();
		SpaceMenu spaceMenu = spaceMenuRepository.findBySpaceId(spaceId).orElseThrow();
		List<String> imgUrlList = reviewImgRepository.findImageUrlsBySpaceIdOrderByCreatedAtAsc(spaceId)
				.stream()
				.limit(2)
				.collect(Collectors.toList());
		Boolean isScraped = member.isPresent() ? checkScrapUtil.checkIfSpaceScraped(spaceId, member.get().getId()) : false;
		List<Review> reviews = reviewRepository.findBySpaceId(spaceId);

		Map<String, Integer> positiveEvalCount = new HashMap<>();
		Map<String, Integer> negativeEvalCount = new HashMap<>();
		int totalReviews = reviews.size();

		for (Review review : reviews) {
			// 긍정적 평가 파싱
			List<String[]> positiveEvalList = parseKeyword(review.getPositive_eval());
			for (String[] eval : positiveEvalList) {
				String keyword = eval[0];
				positiveEvalCount.put(keyword, positiveEvalCount.getOrDefault(keyword, 0) + 1);
			}

			// 부정 평가 파싱
			List<String[]> negativeEvalList = parseKeyword(review.getNegative_eval());
			for (String[] eval : negativeEvalList) {
				String keyword = eval[0];
				negativeEvalCount.put(keyword, negativeEvalCount.getOrDefault(keyword, 0) + 1);
			}
		}

		// 긍정적 평가 퍼센티지 계산
		List<String[][]> positiveEvalResult = Collections.singletonList(calculateEvalPercent(positiveEvalCount, totalReviews));

		// 부정적 평가 퍼센티지 계산
		List<String[][]> negativeEvalResult = Collections.singletonList(calculateEvalPercent(negativeEvalCount, totalReviews));

		spaceDetailMap.put("info",
				SpaceDetailDto.builder()
						.id(space.getId())
						.name(space.getName())
						.imgUrlList(imgUrlList)
						.address(space.getAddress())
						.type(space.getType())
						.subType(Optional.ofNullable(space.getSubType()))
						.dish(Optional.ofNullable(spaceMenu.getDish()))
						.dishDesc(spaceMenu.getDishDesc())
						.visitorNum(spaceInfo.getVisitor())
						.optionalService(spaceInfo.getOptServ())
						.purpose(spaceInfo.getPurpose())
						.mood(spaceInfo.getMood())
						.interior(spaceInfo.getInterior())
						.music(spaceInfo.getMusic())
						.positiveEval(positiveEvalResult)
						.negativeEval(negativeEvalResult)
						.isScraped(isScraped)
						.build()
		);

		return spaceDetailMap;
	}

	public HashMap<String,Object> getSpaceRelatedInfo(Long spaceId, Optional<Member> member) {
		HashMap<String, Object> spaceCurationlMap = new HashMap<>();

		SpaceInfo spaceInfo = spaceInfoRepository.findBySpaceId(spaceId).orElseThrow();

		spaceCurationlMap.put("similarSpaceList", spaceRepository.findSimilarSpace(spaceInfo.getPurpose(), spaceInfo.getMood(), member));
		spaceCurationlMap.put("relatedCurationList", curationRepository.findCurationBySpaceId(spaceId, member));

		return spaceCurationlMap;
	}

	// 키워드 파싱
	private List<String[]> parseKeyword(String keywordList) {
		List<String[]> parsedKeywords = new ArrayList<>();
		if (keywordList != null && !keywordList.isEmpty()) {
			String[] keywords = keywordList.split(",");
			for (String keyword : keywords) {
				parsedKeywords.add(new String[]{keyword.trim()});
			}
		}
		return parsedKeywords;
	}

	// 키워드 퍼센티지 계산 및 정렬
	private String[][] calculateEvalPercent(Map<String, Integer> keywordCountMap, int totalReviews) {
		String[][] keywordArray;

		if (totalReviews == 0) {
			// 리뷰 없는 경우
			keywordArray = new String[0][0];
		} else {
			keywordArray = new String[keywordCountMap.size()][2];
			int totalKeywords = keywordCountMap.values().stream().mapToInt(Integer::intValue).sum();

			int i = 0;
			// 리뷰가 여러 개일 경우 퍼센티지 계산
			for (Map.Entry<String, Integer> entry : keywordCountMap.entrySet()) {
				String content = entry.getKey();
				int count = entry.getValue();
				double percentage = (double) count / totalKeywords * 100;

				// 소수점 아래를 반올림하여 조정
				int roundedPercentage = (int) Math.round(percentage);

				keywordArray[i][0] = content;
				keywordArray[i][1] = String.valueOf(roundedPercentage);
				i++;
			}

			// 퍼센티지 높은 순으로 정렬
			Arrays.sort(keywordArray, (a, b) -> Integer.compare(Integer.parseInt(b[1]), Integer.parseInt(a[1])));
		}

		return keywordArray;
	}
}