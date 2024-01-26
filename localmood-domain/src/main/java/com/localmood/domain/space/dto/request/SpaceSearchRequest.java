package com.localmood.domain.space.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class SpaceSearchRequest {

	@NotNull @Size(min=2)
	private String name;

}
