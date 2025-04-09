package com.notenhanh.service.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.notenhanh.domain.*;
import com.notenhanh.enumation.Role;
import com.notenhanh.enumation.UserStatus;
import com.notenhanh.exception.userauthentication.UpdateUserStatusException;
import com.notenhanh.repository.RoleUserRepository;
import com.notenhanh.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final RoleUserRepository roleUserRepository;
	public UserService(UserRepository userRepository, RoleUserRepository roleUserRepository) {
		this.userRepository = userRepository;
		this.roleUserRepository = roleUserRepository;
	}
	public Optional<Users> getUserById(Long id) {
		return userRepository.findById(id);
	}
	public Optional<Users> getUserByUsername (String username) {
		return userRepository.findByUsername(username);
	}
	public Optional<Users> getUserByProviderAndProviderId (String provider, String providerId) {
		return userRepository.findByProviderAndProviderId(provider, providerId);
	}
	public List<Users> getUsersByRole(Role role){
		return userRepository.findByRoleUser_Role(role);
	}
	public Page<Users> getAllUser(Pageable pageable){
		return userRepository.findAll(pageable);
	}
	public boolean getStatusUser(Users user) {
	    return user.getStatus() == UserStatus.ACTIVE;
	}
	public long getCountUsers() {
		return userRepository.count();
	}

	public long getCountUserFollowStatus(UserStatus userstatus) {
		return userRepository.countByStatus(userstatus);
	}
	
	public Optional<RoleUser> getRoleUserById(Long id){
		return roleUserRepository.findById(id);
	}
	public Optional<RoleUser> getRoleUserByRole(Role role){
		return roleUserRepository.findByRole(role);
	}
	@Transactional
	public void updateStatusUser(Long userId, UserStatus userStatus) {
	Users user = userRepository.findById(userId).orElseThrow(() -> new UpdateUserStatusException("Không tìm được ID tài khoản cần khóa."));
	user.setStatus(userStatus);
	userRepository.save(user);
	}
}