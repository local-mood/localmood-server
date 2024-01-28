package com.localmood.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "멤버를 찾지 못했습니다.", "존재하는 멤버인지 확인해주세요."),
	ALREADY_MEMBER_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다.", "다른 이메일을 사용해주세요."),
	CURATION_NOT_FOUND(HttpStatus.NOT_FOUND, "큐레이션을 찾지 못했습니다.", "존재하는 큐레이션인지 확인해주세요."),
	SPACE_NOT_FOUND(HttpStatus.NOT_FOUND, "공간을 찾지 못했습니다.", "존재하는 공간인지 확인해주세요."),
	SPACE_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "공간 정보를 찾지 못했습니다.", "존재하는 공간 정보인지 확인해주세요."),
	SPACE_MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "공간 메뉴를 찾지 못했습니다.", "존재하는 공간 메뉴인지 확인해주세요."),
	SPACE_SUB_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "공간 서브 유형을 찾지 못했습니다.", "존재하는 공간 서브 유형인지 확인해주세요.");

	private final HttpStatus httpStatus;
	private final String message;
	private final String solution;
}
