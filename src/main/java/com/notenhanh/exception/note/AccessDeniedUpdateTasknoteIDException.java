package com.notenhanh.exception.note;

import org.springframework.security.access.AccessDeniedException;

public class AccessDeniedUpdateTasknoteIDException extends AccessDeniedException {
	private static final long serialVersionUID = 1L;
	public AccessDeniedUpdateTasknoteIDException(String message) {
	super(message);
	}
}