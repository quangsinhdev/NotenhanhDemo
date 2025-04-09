package com.notenhanh.service.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.notenhanh.domain.RoleUser;
import com.notenhanh.domain.Users;
import com.notenhanh.domain.dto.UserAccountDTO.RegisterDTO;
import com.notenhanh.exception.userauthentication.RegisterException;
import com.notenhanh.repository.RoleUserRepository;
import com.notenhanh.repository.UserRepository;

@Service
public class RegisterUserService {
	private final UserRepository userRepository;
	private final RoleUserRepository roleUserRepository;
    private final PasswordEncoder passwordEncoder;


	public RegisterUserService(UserRepository userRepository,
			RoleUserRepository roleUserRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleUserRepository = roleUserRepository;
		this.passwordEncoder = passwordEncoder;
	}
	public Users register(Users user) {
		return userRepository.save(user);
	}
	 public boolean checkEmailValid(String email) {
	        return userRepository.existsByEmail(email);
	    }
	 public boolean checkUsernameValid(String username) {
	        return userRepository.existsByUsername(username);
	    }
	 public boolean UsernameAndPasswordMatch(String username, String password) {
	        return username.equals(password);
	    }
	  public boolean validatePassword(String password, String confirmPassword) {
		  return password.equals(confirmPassword);
	    }
	  public Users registerUser(RegisterDTO registerDTO) {
		  
		  	try {
				Users user = new Users();

				 String encodePassword = passwordEncoder.encode(registerDTO.getPassword());
				 user.setFullname(registerDTO.getFullname());
				 user.setEmail(registerDTO.getEmail());
				 user.setUsername(registerDTO.getUsername());
				 user.setPassword(encodePassword);
				 
				 RoleUser roleUser = roleUserRepository.findById(2L)
				           .orElseThrow(() -> new RegisterException("Không tìm thấy type Role"));
				 user.setRoleUser(roleUser);

				 return this.userRepository.save(user);
			} catch (Exception e) {
		        throw new RegisterException("Đã xảy ra lỗi khi đăng ký");
			}
	    }
	 
}

