package com.notenhanh.exception.note;

import org.springframework.security.access.AccessDeniedException;

public class AccessDeniedUpdateTasknoteURLException extends AccessDeniedException {

	private static final long serialVersionUID = 1L;
	public AccessDeniedUpdateTasknoteURLException(String message) {
	super(message);
	}
}
