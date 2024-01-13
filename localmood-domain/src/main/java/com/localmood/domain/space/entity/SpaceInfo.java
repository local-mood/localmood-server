package com.localmood.domain.space.entity;

import com.localmood.common.entity.BaseTimeEntity;

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
@Table(name = "space_info")
public class SpaceInfo extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "space_id")
	private Space space;

	@Column
	private String purpose;

	@Column
	private String mood;

	@Column
	private String music;

	@Column
	private String interior;

	@Column
	private String visitor;

	@Column
	private String optServ;

	@Builder
	public SpaceInfo (Space space, String purpose, String mood, String music, String interior, String visitor, String optServ) {
		this.space = space;
		this.purpose = purpose;
		this.mood = mood;
		this.music = music;
		this.interior = interior;
		this.visitor = visitor;
		this.optServ = optServ;
	}

}
