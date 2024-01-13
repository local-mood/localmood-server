package com.localmood.domain.curation.entity;

import com.localmood.common.entity.BaseTimeEntity;
import com.localmood.domain.space.entity.Space;

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
@Table(name = "curation_space")
public class CurationSpace extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "curation_id")
	private Curation curation;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "space_id")
	private Space space;

	@Builder
	public CurationSpace (Curation curation, Space space) {
		this.curation = curation;
		this.space = space;
	}
}
