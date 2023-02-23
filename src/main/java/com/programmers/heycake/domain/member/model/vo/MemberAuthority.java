package com.programmers.heycake.domain.member.model.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberAuthority {
	USER("ROLE_USER"),
	STORE("ROLE_STORE"),
	ADMIN("ROLE_ADMIN");

	private final String role;
}
