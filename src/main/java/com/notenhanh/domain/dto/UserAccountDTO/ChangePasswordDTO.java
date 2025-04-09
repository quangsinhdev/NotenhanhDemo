package com.notenhanh.domain.dto.UserAccountDTO;


import com.notenhanh.service.validator.NoWhitespace;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ChangePasswordDTO {
	@NotEmpty(message = "Mật khẩu không được bỏ trống")
	@NotBlank(message="Mật khẩu không được bỏ trống hoặc chỉ là khoảng trắng")
	@Size(min = 8, max = 100, message = "Mật khẩu có độ dài tối thiểu là 8 ký tự và tối đa 100 ký tự")
	@NoWhitespace(message = "Mật khẩu không được chứa khoảng trắng")
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "Mật khẩu phải chứa ít nhất một chữ cái và một chữ số.")
	private String currentPassword;
	
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
	
	public ChangePasswordDTO(
			@NotEmpty(message = "Mật khẩu không được bỏ trống") @NotBlank(message = "Mật khẩu không được bỏ trống hoặc chỉ là khoảng trắng") @Size(min = 8, max = 100, message = "Mật khẩu có độ dài tối thiểu là 8 ký tự và tối đa 100 ký tự") @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "Mật khẩu phải chứa ít nhất một chữ cái và một chữ số.") String currentPassword,
			@NotEmpty(message = "Mật khẩu không được bỏ trống") @NotBlank(message = "Mật khẩu không được bỏ trống hoặc chỉ là khoảng trắng") @Size(min = 8, max = 100, message = "Mật khẩu có độ dài tối thiểu là 8 ký tự và tối đa 100 ký tự") @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "Mật khẩu phải chứa ít nhất một chữ cái và một chữ số.") String newPassword,
			@NotEmpty(message = "Mật khẩu không được bỏ trống") @NotBlank(message = "Mật khẩu không được bỏ trống hoặc chỉ là khoảng trắng") @Size(min = 8, max = 100, message = "Mật khẩu có độ dài tối thiểu là 8 ký tự và tối đa 100 ký tự") @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "Mật khẩu phải chứa ít nhất một chữ cái và một chữ số.") String confirmNewPassword) {
		this.currentPassword = currentPassword;
		this.newPassword = newPassword;
		this.confirmNewPassword = confirmNewPassword;
	}
	public String getCurrentPassword() {
		return currentPassword;
	}
	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
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
