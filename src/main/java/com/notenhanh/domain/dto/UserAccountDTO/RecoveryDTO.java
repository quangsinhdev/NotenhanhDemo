package com.notenhanh.domain.dto.UserAccountDTO;

import com.notenhanh.service.validator.NoWhitespace;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RecoveryDTO {
	
	@NoWhitespace(message = "Tài khoản không được chứa khoảng trắng")
	@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Tài khoản chỉ có thể chứa số và chữ cái")
	@NotBlank(message="Email không được bỏ trống hoặc chỉ chứa khoảng trắng")
	@Size(min = 8, max = 50, message = "Tài khoản có độ dài tối thiểu 8 ký tự và tối đa 50 ký tự")
	private String username;
	
	@NoWhitespace(message = "Email không được chứa khoảng trắng")
	@Pattern(regexp = "^[A-Za-z0-9]+@[A-Za-z0-9]+\\.[A-Za-z]{2,}$", message = "Email không hợp lệ")
	@Email(message="Email không hợp lệ. Vui lòng thử lại")
	@NotBlank(message="Email không được bỏ trống hoặc chỉ chứa khoảng trắng")
	@Size(min = 6, max = 80, message = "Email không hợp lệ. Vui lòng thử lại")
	private String email;
	
	public RecoveryDTO(
			@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Tài khoản chỉ có thể chứa số và chữ cái") @NotBlank(message = "Email không được bỏ trống hoặc chỉ chứa khoảng trắng") @Size(min = 8, max = 50, message = "Tài khoản có độ dài tối thiểu 8 ký tự và tối đa 50 ký tự") String username,
			@Pattern(regexp = "^[A-Za-z0-9]+@[A-Za-z0-9]+\\.[A-Za-z]{2,}$", message = "Email không hợp lệ") @Email(message = "Email không hợp lệ. Vui lòng thử lại") @NotBlank(message = "Email không được bỏ trống hoặc chỉ chứa khoảng trắng") @Size(min = 6, max = 80, message = "Email không hợp lệ. Vui lòng thử lại") String email) {
		this.username = username;
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}

