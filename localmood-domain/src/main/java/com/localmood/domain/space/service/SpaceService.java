package com.localmood.domain.space.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.localmood.domain.space.dto.SpaceDto;
import com.localmood.domain.space.dto.SpaceRecommendDto;
import com.localmood.domain.space.dto.request.SpaceFilterRequest;
import com.localmood.domain.space.dto.request.SpaceSearchRequest;
import com.localmood.domain.space.repository.SpaceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpaceService {

	private final SpaceRepository spaceRepository;

	public Map<String, List<SpaceRecommendDto>> getSpaceRecommendList() {
		String[] keywordArr = {"연인과의 데이트", "친구와의 만남", "왁자지껄 떠들기 좋은", "대화에 집중할 수 있는"};
		HashMap<String, List<SpaceRecommendDto>> spaceRecommendListMap = new HashMap<>();

		for (int i=0; i < keywordArr.length; i++) {
			var restaurantList = spaceRepository.findRestaurantRecommendByKeyword(keywordArr[i]);
			var cafeList = spaceRepository.findCafeRecommendByKeyword(keywordArr[i]);

			var mergedSpaceList = Stream.of(restaurantList, cafeList)
					.flatMap(x -> x.stream())
					.collect(Collectors.toList());

			spaceRecommendListMap.put(keywordArr[i], mergedSpaceList);
		}

		return spaceRecommendListMap;
	}

	public List<SpaceDto> getSpaceSearchList(SpaceSearchRequest request, String sort) {
		return spaceRepository.findSpaceByName(request.getName(), sort);
	}

	public List<SpaceDto> getSpaceFilterList(SpaceFilterRequest request, String sort) {
		return spaceRepository.findSpaceByKeywords(
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
				sort);
	}
}
