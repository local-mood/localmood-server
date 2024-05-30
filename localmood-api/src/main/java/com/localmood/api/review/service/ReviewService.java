package com.localmood.api.review.service;

import static com.localmood.common.utils.RepositoryUtil.*;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.localmood.common.exception.LocalmoodException;
import com.localmood.common.util.CheckScrapUtil;
import com.localmood.common.util.PurposeUtil;
import com.localmood.domain.space.entity.SpaceType;
import org.springframework.stereotype.Service;

import com.localmood.api.review.dto.request.ReviewCreateDto;
import com.localmood.api.review.dto.response.ReviewResponseDto;
import com.localmood.common.exception.ErrorCode;
import com.localmood.common.s3.service.AwsS3Service;
import com.localmood.domain.member.entity.Member;
import com.localmood.domain.review.entity.Review;
import com.localmood.domain.review.entity.ReviewImg;
import com.localmood.domain.review.repository.ReviewImgRepository;
import com.localmood.domain.review.repository.ReviewRepository;
import com.localmood.domain.space.entity.Space;
import com.localmood.domain.space.repository.SpaceRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ReviewService {
	private final AwsS3Service awsS3Service;
	private final SpaceRepository spaceRepository;
	private final ReviewRepository reviewRepository;
	private final ReviewImgRepository reviewImgRepository;
	private final CheckScrapUtil checkScrapUtil;


	public Map<String, Long> createReview(String spaceId, @Valid ReviewCreateDto reviewCreateDto,
							 Member member, MultipartFile[] multipartFiles) {
		Space space = findByIdOrThrow(spaceRepository, Long.parseLong(spaceId), ErrorCode.SPACE_NOT_FOUND);

		Review review = reviewCreateDto.toEntity(space, member);
		reviewRepository.save(review);

		if (multipartFiles != null && multipartFiles.length > 0) {
			for (MultipartFile multipartFile : multipartFiles) {
				String imageUrl = awsS3Service.uploadFile(multipartFile);
				saveReviewImage(review, space, member, imageUrl);
			}
		}

		Map<String, Long> response = new HashMap<>();
		response.put("reviewId", review.getId());
		response.put("spaceId", space.getId());

		return response;
	}

	// 사용자별 공간 기록 조회
	public Map<String, Object> getReviewForMember(Member member) {
		Map<String, Object> response = new LinkedHashMap<>();

		// 리뷰 목록을 생성 시간 순서대로 가져오기
		List<Review> review = reviewRepository.findByMemberIdOrderByCreatedAtDesc(member.getId());
		List<ReviewResponseDto> reviews = new ArrayList<>();

		if (!review.isEmpty()) {
			reviews = review
				.stream()
				.map(r -> mapToReviewResponseDto(r, member))
				.collect(Collectors.toList());
		}
		response.put("reviewCount", reviews.size());
		response.put("reviews", reviews);

		return response;
	}

	private ReviewResponseDto mapToReviewResponseDto(Review review, Member member) {
		String image = getReviewImageUrl(review.getId());

		return new ReviewResponseDto(
			review.getSpace().getId(),
			getReviewImageUrl(review.getId()),
			review.getSpace().getName(),
			review.getSpace().getType().toString(),
			review.getSpace().getAddress(),
			review.getMember().getNickname(),
			checkScrapUtil.checkIfSpaceScraped(review.getSpace().getId(), member.getId())
		);
	}

	private void saveReviewImage(Review review, Space space, Member member, String imageUrl) {
		ReviewImg reviewImg = ReviewImg.builder()
			.review(review)
			.space(space)
			.member(member)
			.imgUrl(imageUrl)
			.build();

		reviewImgRepository.save(reviewImg);
	}

	private String getReviewImageUrl(Long reviewId) {
		return reviewImgRepository.findByReviewId(reviewId)
			.map(ReviewImg::getImgUrl)
			.orElse(null);
	}

	private List<String> getReviewImageUrls(Long reviewId) {
		List<String> imageUrls = new ArrayList<>();
		List<ReviewImg> reviewImages = reviewImgRepository.findListByReviewId(reviewId);
		for (ReviewImg img : reviewImages) {
			imageUrls.add(img.getImgUrl());
			if (imageUrls.size() == 2) {
				break;
			}
		}
		return imageUrls;
	}


	// 공간별 공간 기록 조회
	public Map<String, Object> getSpaceReview(Long spaceId, Optional<Member> memberOptional) {
		Map<String, Object> response = new LinkedHashMap<>();

		// 해당 공간의 리뷰만 필터링
		List<Review> spaceReviews = reviewRepository.findBySpaceId(spaceId);
		Space space = spaceRepository.findById(spaceId).orElseThrow(() -> new LocalmoodException(ErrorCode.SPACE_NOT_FOUND));

		// Space type에 따라 방문 목적 구분
		List<String> purposes;
		if (space.getType() == SpaceType.CAFE) {
			purposes = PurposeUtil.CAFE_PURPOSES;
		} else if (space.getType() == SpaceType.RESTAURANT) {
			purposes = PurposeUtil.RESTAURANT_PURPOSES;
		} else {
			throw new LocalmoodException(ErrorCode.SPACE_TYPE_NOT_FOUND);
		}

		// 방문 목적 전체 개수 계산
		Map<String, Integer> purposeCounts = new HashMap<>();
		for (Review review : spaceReviews) {
			String[] reviewPurposes = review.getPurpose().split(",");
			for (String reviewPurpose : reviewPurposes) {
				if (purposes.contains(reviewPurpose.trim())) {
					purposeCounts.put(reviewPurpose.trim(), purposeCounts.getOrDefault(reviewPurpose.trim(), 0) + 1);
				}
			}
		}

		// 방문 목적별 리뷰 개수로부터 퍼센티지 계산
		Map<String, Integer> purposePercentages = new HashMap<>();
		int totalPurposeCount = purposeCounts.values().stream().mapToInt(Integer::intValue).sum();

		for (String purpose : purposes) {
			int purposeCount = purposeCounts.getOrDefault(purpose, 0);
			int percentage = totalPurposeCount == 0 ? 0 : (purposeCount * 100) / totalPurposeCount;
			purposePercentages.put(purpose, percentage);
		}

		// 방문 목적별로 그룹화하여 dto 생성
		Map<String, List<Map<String, Object>>> reviewMap = new HashMap<>();
		for (Review review : spaceReviews) {
			String[] reviewPurposes = review.getPurpose().split(",");

			for (String reviewPurpose : reviewPurposes) {
				if (purposes.contains(reviewPurpose.trim())) {
					List<Map<String, Object>> reviewList = reviewMap.getOrDefault(reviewPurpose.trim(), new ArrayList<>());
					reviewList.add(mapToReviewDetailResponseDto(review, memberOptional));
					reviewMap.put(reviewPurpose.trim(), reviewList);
				}
			}
		}

		// 본인 리뷰 먼저 정렬
		for (String purpose : reviewMap.keySet()) {
			List<Map<String, Object>> reviews = reviewMap.get(purpose);
			reviews.sort((r1, r2) -> {
				boolean isAuthor1 = r1.get("author").equals(memberOptional.map(Member::getNickname).orElse(""));
				boolean isAuthor2 = r2.get("author").equals(memberOptional.map(Member::getNickname).orElse(""));
				return Boolean.compare(isAuthor2, isAuthor1);
			});
		}

		response.put("purposeEval", purposePercentages);
		response.put("reviewCount", spaceReviews.size());
		response.put("reviews", reviewMap);

		return response;
	}

	private Map<String, Object> mapToReviewDetailResponseDto(Review review, Optional<Member> memberOptional) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd");

		List<String> images = getReviewImageUrls(review.getId());
		boolean isScraped = memberOptional.
				map(member -> checkScrapUtil.checkIfSpaceScraped(review.getSpace().getId(), member.getId())).orElse(false);

		Map<String, Object> reviewDetailResponseDto = new LinkedHashMap<>();
		reviewDetailResponseDto.put("image", images);
		reviewDetailResponseDto.put("name", review.getSpace().getName());
		reviewDetailResponseDto.put("type", review.getSpace().getType().toString());
		reviewDetailResponseDto.put("address", review.getSpace().getAddress());
		reviewDetailResponseDto.put("author", review.getMember().getNickname());
		reviewDetailResponseDto.put("createdAt", review.getCreatedAt().format(formatter));
		reviewDetailResponseDto.put("interior", review.getInterior());
		reviewDetailResponseDto.put("mood", review.getMood());
		reviewDetailResponseDto.put("music", review.getMusic());
		reviewDetailResponseDto.put("positiveEval", review.getPositive_eval());
		reviewDetailResponseDto.put("negativeEval", review.getNegative_eval());
		reviewDetailResponseDto.put("isScraped", isScraped);

		return reviewDetailResponseDto;
	}

}
