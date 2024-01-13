package com.localmood.domain.space.entity;

import com.localmood.common.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "space")
public class Space extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String name;

	@Column
	private String address;

	@Column
	@Enumerated(EnumType.STRING)
	private SpaceType type;

	@Column
	@Enumerated(EnumType.STRING)
	private SpaceSubType subType;

	@Builder
	public Space (String name, String address, SpaceType type, SpaceSubType subType) {
		this.name = name;
		this.address = address;
		this.type = type;
		this.subType = subType;
	}

}
