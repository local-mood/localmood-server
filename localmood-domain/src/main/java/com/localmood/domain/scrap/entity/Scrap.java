package com.localmood.domain.scrap.entity;

import com.localmood.common.entity.BaseTimeEntity;
import com.localmood.domain.curation.entity.Curation;
import com.localmood.domain.member.entity.Member;

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
@Table(name = "scrap")
public class Scrap extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "curation_id")
	private Curation curation;

	@Builder
	public Scrap (Member member, Curation curation) {
		this.member = member;
		this.curation = curation;
	}

}
