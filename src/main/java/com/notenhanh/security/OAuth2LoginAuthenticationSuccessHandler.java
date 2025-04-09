package com.notenhanh.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import com.notenhanh.domain.RoleUser;
import com.notenhanh.domain.Users;
import com.notenhanh.enumation.Role;
import com.notenhanh.repository.RoleUserRepository;
import com.notenhanh.repository.UserRepository;
import com.notenhanh.service.user.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@Component
public class OAuth2LoginAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final UserRepository userRepository;
	private final RoleUserRepository roleUserRepository;
	private final UserService userService;

	public OAuth2LoginAuthenticationSuccessHandler(UserRepository userRepository, RoleUserRepository roleUserRepository,
			UserService userService) {
		this.userRepository = userRepository;
		this.roleUserRepository = roleUserRepository;
		this.userService = userService;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
		Map<String, Object> attributes = oauth2User.getAttributes();

		if (authentication instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken oauth2AuthToken = (OAuth2AuthenticationToken) authentication;
			String provider = oauth2AuthToken.getAuthorizedClientRegistrationId();
			String providerId = getProviderId(provider, attributes);
			String email = (String) attributes.get("email");
			String fullname = (String) attributes.get("name");
			String subname = "notenhanh";

			Users user = userRepository.findByProviderAndProviderId(provider, providerId)
					.orElseGet(() -> createNewUser(provider, providerId, email, fullname, subname));

			if (!userService.getStatusUser(user)) {
				logoutAndRedirect(request, response);
				return;
			}

			updateUserInfo(user, fullname);
		}

		response.sendRedirect("/dashboard");
	}

	private void logoutAndRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		new SecurityContextLogoutHandler().logout(request, response, auth);
		SecurityContextHolder.clearContext();
		response.sendRedirect("/login?locked=accountlocked");
	}

	private Users createNewUser(String provider, String providerId, String email, String fullname, String subname) {
		Users newUser = new Users();
		newUser.setEmail(email);
		newUser.setProvider(provider);
		newUser.setProviderId(providerId);
		newUser.setUsername(subname.concat(providerId));
		newUser.setFullname(fullname);
		newUser.setPassword("OAUTH2_USER");
		RoleUser roleUser = roleUserRepository.findByRole(Role.USER)
				.orElseThrow(() -> new IllegalStateException("Không tìm thấy Role của người dùng"));

		newUser.setRoleUser(roleUser);
		return userRepository.save(newUser);
	}

	private void updateUserInfo(Users user, String fullname) {
		user.setFullname(fullname);

		if (user.getRoleUser() == null) {
			RoleUser roleUser = roleUserRepository.findByRole(Role.USER)
					.orElseThrow(() -> new IllegalStateException("Không tìm thấy Role của người dùng"));
			user.setRoleUser(roleUser);
		}

		userRepository.save(user);
	}

	private String getProviderId(String provider, Map<String, Object> attributes) {
		switch (provider) {
		case "google":
			return (String) attributes.get("sub");
		case "facebook":
			return (String) attributes.get("id");
		default:
			throw new IllegalArgumentException("Unknown provider: " + provider);
		}
	}
}