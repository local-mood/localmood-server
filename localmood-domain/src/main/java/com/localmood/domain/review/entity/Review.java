package com.localmood.domain.review.entity;

import com.localmood.common.entity.BaseTimeEntity;
import com.localmood.domain.member.entity.Member;
import com.localmood.domain.space.entity.Space;

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
@Table(name = "review")
public class Review extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "space_id")
	private Space space;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@Column
	private String purpose;

	@Column
	private String mood;

	@Column
	private String music;

	@Column
	private String interior;

	@Column
	private String positive_eval;

	@Column
	private String negative_eval;

	@Builder
	public Review (Space space, Member member, String purpose, String mood, String music, String interior, String positive_eval, String negative_eval) {
		this.space = space;
		this.member = member;
		this.purpose = purpose;
		this.mood = mood;
		this.music = music;
		this.interior = interior;
		this.positive_eval = positive_eval;
		this.negative_eval = negative_eval;
	}

}
