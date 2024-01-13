package com.localmood.domain.space.entity;

import com.localmood.common.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
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
@Table(name = "space_menu")
public class SpaceMenu extends BaseTimeEntity {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "space_id")
	private Space space;

	@Column
	@Enumerated(EnumType.STRING)
	private SpaceDish dish;

	@Column
	private String dishDesc;

	@Builder
	public SpaceMenu (Space space, SpaceDish dish, String dishDesc) {
		this.space = space;
		this.dish = dish;
		this.dishDesc = dishDesc;
	}

}
