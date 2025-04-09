package com.notenhanh.domain.dto.UserAccountDTO;

import jakarta.validation.constraints.Pattern;

import com.notenhanh.service.validator.NoWhitespace;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class RecoveryNewPasswordDTO {
	@NotEmpty(message = "Token không được bỏ trống")
	@NotBlank(message="Token không được bỏ trống hoặc chỉ là khoảng trắng")
	@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Mật khẩu phải chứa ít nhất một chữ cái và một chữ số.")
	private String tokenrecovery;
	
	@NotEmpty(message = "Mật khẩu không được bỏ trống")
	@NotBlank(message="Mật khẩu không được bỏ trống hoặc chỉ là khoảng trắng")
	@Size(min = 8, max = 100, message = "Mật khẩu có độ dài tối thiểu là 8 ký tự và tối đa 100 ký tự")
	@NoWhitespace(message = "Mật khẩu không được chứa khoảng trắng")
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "Mật khẩu phải chứa ít nhất một chữ cái và một chữ số.")
	private String newPassword;
	
	@NotEmpty(message = "Mật khẩu không được bỏ trống")
	@NotBlank(message="Mật khẩu không được bỏ trống hoặc chỉ là khoảng trắng")
	@Size(min = 8, max = 100, message = "Mật khẩu có độ dài tối thiểu là 8 ký tự và tối đa 100 ký tự")
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "Mật khẩu phải chứa ít nhất một chữ cái và một chữ số.")
	private String confirmNewPassword;
	
	public RecoveryNewPasswordDTO(
			@NotEmpty(message = "Token không được bỏ trống") @NotBlank(message = "Token không được bỏ trống hoặc chỉ là khoảng trắng") @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Mật khẩu phải chứa ít nhất một chữ cái và một chữ số.") String tokenrecovery,
			@NotEmpty(message = "Mật khẩu không được bỏ trống") @NotBlank(message = "Mật khẩu không được bỏ trống hoặc chỉ là khoảng trắng") @Size(min = 8, max = 100, message = "Mật khẩu có độ dài tối thiểu là 8 ký tự và tối đa 100 ký tự") @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "Mật khẩu phải chứa ít nhất một chữ cái và một chữ số.") String newPassword,
			@NotEmpty(message = "Mật khẩu không được bỏ trống") @NotBlank(message = "Mật khẩu không được bỏ trống hoặc chỉ là khoảng trắng") @Size(min = 8, max = 100, message = "Mật khẩu có độ dài tối thiểu là 8 ký tự và tối đa 100 ký tự") @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "Mật khẩu phải chứa ít nhất một chữ cái và một chữ số.") String confirmNewPassword) {
		this.tokenrecovery = tokenrecovery;
		this.newPassword = newPassword;
		this.confirmNewPassword = confirmNewPassword;
	}
	public String getTokenrecovery() {
		return tokenrecovery;
	}
	public void setTokenrecovery(String tokenrecovery) {
		this.tokenrecovery = tokenrecovery;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getConfirmNewPassword() {
		return confirmNewPassword;
	}
	public void setConfirmNewPassword(String confirmNewPassword) {
		this.confirmNewPassword = confirmNewPassword;
	}
	
	
}
