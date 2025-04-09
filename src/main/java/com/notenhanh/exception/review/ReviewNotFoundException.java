package com.notenhanh.exception.review;

public class ReviewNotFoundException extends RuntimeException{
	public ReviewNotFoundException(String message) {
		super(message);
	}
}
