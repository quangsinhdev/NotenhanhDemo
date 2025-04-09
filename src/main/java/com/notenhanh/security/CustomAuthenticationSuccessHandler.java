package com.notenhanh.security;

import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.notenhanh.domain.Users;
import com.notenhanh.exception.userauthentication.UserNotFoundException;
import com.notenhanh.repository.UserRepository;
import com.notenhanh.service.user.LoginAttemptService;
import com.notenhanh.service.user.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final UserService userService;
	private final UserRepository userRepository;
	private final LoginAttemptService loginAttemptService;

	public CustomAuthenticationSuccessHandler(UserService userService, UserRepository userRepository,
			LoginAttemptService loginAttemptService) {
		this.userService = userService;
		this.userRepository = userRepository;
		this.loginAttemptService = loginAttemptService;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		String username = request.getParameter("username");
		Users user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException("Người dùng " + username + " không tồn tại."));

		loginAttemptService.clearLock(username);

		response.sendRedirect("/dashboard");
	}
}