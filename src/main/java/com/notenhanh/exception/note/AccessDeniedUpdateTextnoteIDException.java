package com.notenhanh.exception.note;

import org.springframework.security.access.AccessDeniedException;

public class AccessDeniedUpdateTextnoteIDException extends AccessDeniedException {

	private static final long serialVersionUID = 1L;
	public AccessDeniedUpdateTextnoteIDException(String message) {
	super(message);
	}
}
