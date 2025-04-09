package com.notenhanh.service.user;

import java.util.Collections;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.notenhanh.domain.RoleUser;
import com.notenhanh.domain.Users;
import com.notenhanh.enumation.Role;
import com.notenhanh.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LoginUserService implements UserDetailsService {
	private final UserRepository userRepository;
	private final UserService userService;
	private final LoginAttemptService loginAttemptService;
	private static final Logger logger = LoggerFactory.getLogger(LoginUserService.class);

	public LoginUserService(UserRepository userRepository, UserService userService,
			LoginAttemptService loginAttemptService) {
		super();
		this.userRepository = userRepository;
		this.userService = userService;
		this.loginAttemptService = loginAttemptService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			Users user = userRepository.findByUsername(username)
					.orElseThrow(() -> new UsernameNotFoundException("Tài khoản không tồn tại"));

			// Kiểm tra trạng thái tài khoản
			if (!userService.getStatusUser(user)) {
				throw new UsernameNotFoundException("Tài khoản của bạn đã bị khóa. Vui lòng liên hệ Admin.");
			}

			// Kiểm tra tạm khóa
			if (loginAttemptService.CheckAccountLocked(username)) {
				long remainingMinutes = (loginAttemptService.getLockTime(username) - System.currentTimeMillis() + 59999)
						/ 60000;
				throw new UsernameNotFoundException(
						"Tài khoản bị tạm khóa vì đăng nhập sai nhiều lần. Thử lại sau: " + remainingMinutes + " phút");
			}

			if (user.getRoleUser() == null) {
				logger.warn("Người dùng {} không có roleUser, gán mặc định là ROLE_USER", username);
				RoleUser roleUser = new RoleUser();
				roleUser.setRole(Role.USER);
				user.setRoleUser(roleUser);
			}

			String role = user.getRoleUser().getRole().name();

			return new User(user.getUsername(), user.getPassword(),
					Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role)));
		} catch (Exception e) {
			logger.error("Lỗi khi tìm người dùng {}: {}", username, e.getMessage(), e);
			throw new UsernameNotFoundException("Đã xảy ra lỗi khi đăng nhập: " + e.getMessage(), e);
		}
	}
}
