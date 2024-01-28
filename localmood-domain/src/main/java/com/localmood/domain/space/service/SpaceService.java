package com.localmood.domain.space.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.localmood.domain.curation.repository.CurationRepository;
import com.localmood.domain.review.repository.ReviewImgRepository;
import com.localmood.domain.scrap.repository.ScrapSpaceRepository;
import com.localmood.domain.space.dto.SpaceDetailDto;
import com.localmood.domain.space.dto.SpaceSearchDto;
import com.localmood.domain.space.dto.SpaceRecommendDto;
import com.localmood.domain.space.dto.request.SpaceFilterRequest;
import com.localmood.domain.space.dto.request.SpaceSearchRequest;
import com.localmood.domain.space.entity.Space;
import com.localmood.domain.space.entity.SpaceDish;
import com.localmood.domain.space.entity.SpaceInfo;
import com.localmood.domain.space.entity.SpaceMenu;
import com.localmood.domain.space.entity.SpaceSubType;
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
	private final ReviewImgRepository reviewImgRepository;
	private final ScrapSpaceRepository scrapSpaceRepository;
	private final CurationRepository curationRepository;

	public Map<String, List<SpaceRecommendDto>> getSpaceRecommendList(Long memberId) {
		String[] keywordArr = {"연인과의 데이트", "친구와의 만남", "왁자지껄 떠들기 좋은", "대화에 집중할 수 있는"};
		HashMap<String, List<SpaceRecommendDto>> spaceRecommendListMap = new HashMap<>();

		for (int i=0; i < keywordArr.length; i++) {
			var restaurantList = spaceRepository.findRestaurantRecommendByKeyword(keywordArr[i], memberId);
			var cafeList = spaceRepository.findCafeRecommendByKeyword(keywordArr[i], memberId);

			var mergedSpaceList = Stream.of(restaurantList, cafeList)
					.flatMap(x -> x.stream())
					.collect(Collectors.toList());

			spaceRecommendListMap.put(keywordArr[i], mergedSpaceList);
		}

		return spaceRecommendListMap;
	}

	public List<SpaceSearchDto> getSpaceSearchList(SpaceSearchRequest request, String sort, Long memberId) {
		return spaceRepository.findSpaceByName(request.getName(), sort, memberId);
	}

	public List<SpaceSearchDto> getSpaceFilterList(SpaceFilterRequest request, String sort, Long memberId) {
		return spaceRepository.findSpaceByKeywords(
				request.getType(),
				SpaceSubType.of(request.getSubType()),
				request.getPurpose(),
				request.getMood(),
				request.getMusic(),
				request.getInterior(),
				request.getVisitor(),
				request.getOptServ(),
				SpaceDish.of(request.getDish()),
				request.getDisDesc(),
				sort,
				memberId
		);
	}

	public HashMap<String,Object> getSpaceDetail(Long spaceId, Long memberId) {
		HashMap<String, Object> spaceDetailMap = new HashMap<>();

		Space space = spaceRepository.findById(spaceId).orElseThrow();
		SpaceInfo spaceInfo = spaceInfoRepository.findBySpaceId(spaceId).orElseThrow();
		SpaceMenu spaceMenu = spaceMenuRepository.findBySpaceId(spaceId).orElseThrow();
		List<String> imgUrlList = reviewImgRepository.findImageUrlsBySpaceId(spaceId);
		Boolean isScraped = scrapSpaceRepository.existsByMemberIdAndSpaceId(memberId, spaceId);

		spaceDetailMap.put("info",
				SpaceDetailDto.builder()
						.id(space.getId())
						.name(space.getName())
						.imgUrlList(imgUrlList)
						.address(space.getAddress())
						.type(space.getType())
						.subType(space.getSubType())
						.dish(spaceMenu.getDish())
						.dishDesc(spaceMenu.getDishDesc())
						.visitorNum(spaceInfo.getVisitor())
						.optionalService(spaceInfo.getOptServ())
						.purpose(spaceInfo.getPurpose())
						.mood(spaceInfo.getMood())
						.music(spaceInfo.getMusic())
						.positiveEval(spaceInfo.getPositiveEval())
						.negativeEval(spaceInfo.getNegativeEval())
						.isScraped(isScraped)
						.build()
		);

		// 공간과 비슷한 장소
		spaceDetailMap.put("similarSpaceList", spaceRepository.findSimilarSpace(spaceInfo.getPurpose(), spaceInfo.getMood(), memberId));

				// 공간이 담긴 큐레이션
		spaceDetailMap.put("relatedCurationList", curationRepository.findCurationBySpaceId(spaceId, memberId));

		return spaceDetailMap;
	}
}
