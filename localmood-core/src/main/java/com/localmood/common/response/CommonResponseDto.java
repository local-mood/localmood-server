package com.localmood.common.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommonResponseDto {
	private String status;
	private String message;

	protected CommonResponseDto(String status) {
		this.status = status;
	}

	public static CommonResponseDto success() {
		return new CommonResponseDto("SUCCESS");
	}

	public static CommonResponseDto fail() {
		return new CommonResponseDto("FAIL");
	}

	// TODO
	//   - AUTH 적용 후 message 변경
	public void setMessage(String message) {
		this.message = message;
	}
}