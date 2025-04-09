package com.notenhanh.service.user;

import org.springframework.stereotype.Service;

import com.notenhanh.domain.Users;
import com.notenhanh.repository.UserRepository;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@Service
public class ChangePasswordService {
	private final UserRepository userRepository;
	private BCryptPasswordEncoder passwordEncoder;
	public ChangePasswordService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	public boolean CheckCurrentPassword(String currentPasswordToChange, String currentPassword) {
		return passwordEncoder.matches(currentPasswordToChange, currentPassword);
	}
	public boolean CheckCurrentPasswordAndNewPassword(String newPassword, String currentPassword) {
		return passwordEncoder.matches(newPassword, currentPassword);
	}
	public boolean CheckConfirmNewPassword(String newPassword, String confirmNewPassword) {
		return(newPassword.equals(confirmNewPassword));
	}
	public void UpdateNewPassword(String username, String newPassword) {
		Users user = userRepository.findByUsername(username).get();
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
	}
}
