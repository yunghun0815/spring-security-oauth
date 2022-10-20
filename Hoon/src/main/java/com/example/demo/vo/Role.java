package com.example.demo.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

	ROLE_USER("USER"),
	ROLE_MANAGER("MANAGER"),
	ROLE_ADMIN("ADMIN");
	
	private final String title;
}
