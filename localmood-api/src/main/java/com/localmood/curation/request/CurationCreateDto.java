package com.localmood.curation.request;

import com.localmood.domain.curation.entity.Curation;
import com.localmood.domain.member.entity.Member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "큐레이션 생성 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CurationCreateDto {

	@Schema(description = "제목", required = true)
	private String title;

	@Schema(description = "대표 키워드", required = true)
	private String keyword;

	@Schema(description = "공개 여부", required = true)
	private boolean privacy;

	public Curation toEntity(Member member) {
		return Curation.builder()
			.member(member)
			.title(title)
			.keyword(keyword)
			.privacy(privacy)
			.build();
	}
}
