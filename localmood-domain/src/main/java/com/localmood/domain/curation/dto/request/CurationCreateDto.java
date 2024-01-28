package com.localmood.domain.curation.dto.request;

import com.localmood.domain.curation.entity.Curation;
import com.localmood.domain.member.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CurationCreateDto {

	private String title;
	private String keyword;
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
