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
	CURATION_SPACE_NOT_FOUND(HttpStatus.NOT_FOUND, "큐레이션에 저장된 해당 장소를 찾지 못했습니다.", "큐레이션에 저장된 장소인지 확인해주세요."),
	SPACE_NOT_FOUND(HttpStatus.NOT_FOUND, "공간을 찾지 못했습니다.", "존재하는 공간인지 확인해주세요."),
	SPACE_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "공간 정보를 찾지 못했습니다.", "존재하는 공간 정보인지 확인해주세요."),
	SPACE_MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "공간 메뉴를 찾지 못했습니다.", "존재하는 공간 메뉴인지 확인해주세요."),
	SPACE_SUB_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "공간 서브 유형을 찾지 못했습니다.", "존재하는 공간 서브 유형인지 확인해주세요."),
	FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.", "파일 업로드를 다시 시도해주세요."),
	INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "유효하지 않은 이메일 형식입니다.", "올바른 이메일 주소를 입력하세요."),
	INVALID_PASSWORD_FORMAT(HttpStatus.BAD_REQUEST, "유효하지 않은 비밀번호 형식입니다.", "비밀번호는 8~16자리여야 하며, 영문, 숫자, 특수문자를 포함해야 합니다."),
	ALREADY_SCRAP_SPACE(HttpStatus.BAD_REQUEST, "이미 스크랩된 공간입니다.", "중복 스크랩은 불가합니다."),
	ALREADY_SCRAP_CURATION(HttpStatus.BAD_REQUEST, "이미 스크랩된 큐레이션입니다.", "중복 스크랩은 불가합니다.");

	private final HttpStatus httpStatus;
	private final String message;
	private final String solution;
}
