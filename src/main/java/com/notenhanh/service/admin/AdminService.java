package com.notenhanh.service.admin;

import org.springframework.security.access.AccessDeniedException;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.notenhanh.domain.Review;
import com.notenhanh.domain.RoleUser;
import com.notenhanh.domain.Users;
import com.notenhanh.enumation.Role;
import com.notenhanh.exception.review.ReviewNotFoundException;
import com.notenhanh.exception.userauthentication.RoleNotFoundException;
import com.notenhanh.exception.userauthentication.UserNotFoundException;
import com.notenhanh.repository.AdminRepository;
import com.notenhanh.repository.RoleUserRepository;
import com.notenhanh.service.user.ReviewService;
import com.notenhanh.service.user.UserService;

@Service
public class AdminService {
	private final AdminRepository adminRepository;
	private final UserService userService;
	private final RoleUserRepository roleUserRepository;
	private final ReviewService reviewService;

	public AdminService(AdminRepository adminRepository, UserService userService, RoleUserRepository roleUserRepository, ReviewService reviewService) {
		this.adminRepository = adminRepository;
		this.userService = userService;
		this.roleUserRepository = roleUserRepository;
		this.reviewService = reviewService;
	}

	public Page<Users> getAllUsers(Pageable pageable) {
		if (checkPermissionAdmin()) {
			return userService.getAllUser(pageable);
		} else {
			throw new SecurityException("Không đủ quyền truy cập");
		}
	}

	public List<Users> getUsersByRole(Role role) {
	    if (!checkPermissionAdmin()) {
	        throw new SecurityException("Không đủ quyền truy cập");
	    }
	    List<Users> users = userService.getUsersByRole(role);
	    if (users == null || users.isEmpty()) {
	        throw new RoleNotFoundException("RoleUser không tồn tại.");
	    }
	    return users;
	}

	@Transactional
	public void updateUserRole(Long userId, Role role) {
		if (!checkPermissionAdmin()) {
			throw new SecurityException("Bạn không có quyền truy cập tính năng này.");
		}

		RoleUser roleUser = userService.getRoleUserByRole(role)
				.orElseThrow(() -> new RoleNotFoundException("RoleUser không tồn tại."));

		adminRepository.updateRoleUser(userId, roleUser);
	}

	@Transactional
	public void deleteUser(Long userId) {
		if (!checkPermissionAdmin()) {
			throw new SecurityException("Bạn không có quyền truy cập tính năng này.");
		}

		Optional<Users> userOptional = userService.getUserById(userId);
		if (!userOptional.isPresent()) {
			throw new RuntimeException("Người dùng cần xóa không tồn tại.");
		}

		adminRepository.deleteById(userId);
	}

	public Page<Review> getAllFeedback(Pageable pageable) {
		if (!checkPermissionAdmin()) {
			throw new SecurityException("Bạn không có quyền truy cập tính năng này.");
		}
		return reviewService.getAllFeedBack(pageable);
	}

	public Review ViewFeedBackById(Long reviewId)
			throws AccessDeniedException, ReviewNotFoundException, UserNotFoundException {
		return reviewService.ViewFeedBackById(reviewId);
	}

	private boolean checkPermissionAdmin() {
		Users user = getAuthenticatedUser();
		if (user == null) {
			throw new UserNotFoundException("Bạn chưa đăng nhập. Vui lòng đăng nhập và thử lại.");
		}
		return user.getRoleUser().getRole() == Role.ADMIN;
	}

	private Users getAuthenticatedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Users user = null;

		if (authentication instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
			OAuth2User oauthUser = oauthToken.getPrincipal();
			String providerId = (String) oauthUser.getAttributes().get("sub");
			String provider = oauthToken.getAuthorizedClientRegistrationId();

			Optional<Users> optionalUser = userService.getUserByProviderAndProviderId(provider, providerId);
			if (optionalUser.isPresent()) {
				user = optionalUser.get();
			} else {
				throw new UserNotFoundException("Người dùng chưa đăng nhập.");
			}
		} else if (authentication instanceof UsernamePasswordAuthenticationToken) {
			String username = authentication.getName();
			Optional<Users> optionalUser = userService.getUserByUsername(username);
			if (optionalUser.isPresent()) {
				user = optionalUser.get();
			} else {
				throw new UserNotFoundException("Người dùng chưa đăng nhập.");
			}
		}

		return user;
	}
}