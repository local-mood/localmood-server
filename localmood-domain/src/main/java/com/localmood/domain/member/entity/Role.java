package com.localmood.domain.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
	ROLE_USER("회원"),
	ROLE_ADMIN("관리자");

	private final String roleName;
}
