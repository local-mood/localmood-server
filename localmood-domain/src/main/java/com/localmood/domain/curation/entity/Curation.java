package com.localmood.domain.curation.entity;

import com.localmood.common.entity.BaseTimeEntity;
import com.localmood.domain.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "curation")
public class Curation extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "member_id")
	private Member member;

	@Column
	private String title;

	@Column
	private String keyword;

	@Column
	private Boolean privacy;

	public void setTitle(String title) {
		this.title = title;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public void setPrivacy(boolean privacy) {
		this.privacy = privacy;
	}

	@Builder
	public Curation (Member member, String title, String keyword, Boolean privacy) {
		this.member = member;
		this.title = title;
		this.keyword = keyword;
		this.privacy = privacy;
	}

}
