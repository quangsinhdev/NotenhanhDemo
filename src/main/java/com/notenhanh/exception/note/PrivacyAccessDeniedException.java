package com.notenhanh.exception.note;

import java.nio.file.AccessDeniedException;

public class PrivacyAccessDeniedException extends RuntimeException {
	public PrivacyAccessDeniedException(String message) {
		super(message);
	}
}
