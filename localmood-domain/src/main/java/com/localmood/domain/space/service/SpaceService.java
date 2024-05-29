package com.localmood.domain.space.service;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
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
import com.localmood.domain.scrap.repository.ScrapSpaceRepository;
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

		// 공간과 비슷한 장소
		spaceDetailMap.put("similarSpaceList", spaceRepository.findSimilarSpace(spaceInfo.getPurpose(), spaceInfo.getMood(), member));

		// 공간이 담긴 큐레이션
		spaceDetailMap.put("relatedCurationList", curationRepository.findCurationBySpaceId(spaceId, member));

		return spaceDetailMap;
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
	private String[][] calculateEvalPercent(Map<String, Integer> evalCount, int totalReviews) {
		int totalCount = evalCount.values().stream().mapToInt(Integer::intValue).sum();

		// 각 키워드의 퍼센티지 계산
		List<Map.Entry<String, Integer>> percentageList = new ArrayList<>();

		for (Map.Entry<String, Integer> entry : evalCount.entrySet()) {
			String keyword = entry.getKey();
			int count = entry.getValue();
			int percentage = (int) Math.round(((double) count / totalCount) * 100);
			percentageList.add(new AbstractMap.SimpleEntry<>(keyword, percentage));
		}

		// 퍼센티지 조정
		int totalPercentage = percentageList.stream().mapToInt(Map.Entry::getValue).sum();
		int difference = totalPercentage - 100;
		while (difference != 0) {
			for (int i = 0; i < percentageList.size(); i++) {
				if (difference == 0) break;
				Map.Entry<String, Integer> entry = percentageList.get(i);
				int percentage = entry.getValue();
				if (difference > 0 && percentage > 1) {
					percentageList.set(i, new AbstractMap.SimpleEntry<>(entry.getKey(), percentage - 1));
					difference--;
				} else if (difference < 0) {
					percentageList.set(i, new AbstractMap.SimpleEntry<>(entry.getKey(), percentage + 1));
					difference++;
				}
			}
		}

		// 내림차순 정렬
		percentageList.sort((e1, e2) -> e2.getValue() - e1.getValue());

		// 결과 배열로 변환
		String[][] keywordArray = new String[percentageList.size()][2];
		for (int i = 0; i < percentageList.size(); i++) {
			Map.Entry<String, Integer> entry = percentageList.get(i);
			keywordArray[i][0] = entry.getKey();
			keywordArray[i][1] = String.valueOf(entry.getValue());
		}

		return keywordArray;
	}
}