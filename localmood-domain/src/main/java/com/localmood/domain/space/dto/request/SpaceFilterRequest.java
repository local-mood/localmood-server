package com.localmood.domain.space.dto.request;
import jakarta.validation.constraints.Null;
import lombok.Getter;

@Getter
public class SpaceFilterRequest {

	private String type;
	private String purpose;
	private String mood;
	private String music;
	private String interior;
	private String visitor;
	private String optServ;
	private String dish;
	private String disDesc;

}
