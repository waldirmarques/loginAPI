package com.br.waldir.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.br.waldir.security.UserSS;

public class UserServiceService {
	
	public static UserSS authenticated() {
		try {
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		catch (Exception e) {
			return null;
		}
	}
}
