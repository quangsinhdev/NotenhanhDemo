package com.notenhanh.domain.dto.UserAccountDTO;

import com.notenhanh.service.validator.NoWhitespace;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterDTO {
	@NotEmpty(message = "Họ tên hoặc nickname không thể bỏ trống")
	@Size(min = 4, max = 80, message = "Họ tên hoặc nickname tối thiểu 4 ký tự")
	@Pattern(regexp = "^[\\p{L}\\s]+$", message = "Họ tên hoặc nickname không hợp lệ")
	private String fullname;
	
	@Email(message="Email không hợp lệ. Vui lòng thử lại")
	@NotBlank(message="Email không hợp lệ. Vui lòng thử lại")
	@Size(min = 6, max = 80, message = "Email không hợp lệ. Vui lòng thử lại")
	private String email;
	
	@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Tài khoản chỉ có thể chứa số và chữ cái")
	@NotEmpty(message = "Tài khoản không được bỏ trống")
	@NotBlank(message="Tài khoản không được bỏ trống hoặc chứa khoảng trắng")
	@Size(min = 8, max = 50, message = "Tài khoản có độ dài tối thiểu 8 ký tự và tối đa 50 ký tự")
	private String username;
	
	@NotEmpty(message = "Mật khẩu không được bỏ trống")
	@NotBlank(message="Mật khẩu không được bỏ trống hoặc chỉ là khoảng trắng")
	@Size(min = 8, max = 100, message = "Mật khẩu có độ dài tối thiểu là 8 ký tự và tối đa 100 ký tự")
	@NoWhitespace(message = "Mật khẩu không được chứa khoảng trắng")
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "Mật khẩu phải chứa ít nhất một chữ cái và một chữ số.")
	private String password;
	
	@NotEmpty(message = "Mật khẩu không được bỏ trống")
	@NotBlank(message="Mật khẩu không được bỏ trống hoặc chỉ là khoảng trắng")
	@Size(min = 8, max = 100, message = "Mật khẩu có độ dài tối thiểu là 8 ký tự và tối đa 100 ký tự")
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "Mật khẩu phải chứa ít nhất một chữ cái và một chữ số.")
	private String confirmPassword;
	public RegisterDTO(
			@NotEmpty(message = "Họ tên hoặc nickname không thể bỏ trống") @Size(min = 4, max = 80, message = "Họ tên hoặc nickname tối thiểu 4 ký tự") @Pattern(regexp = "^[\\p{L}\\s]+$", message = "Họ tên hoặc nickname không hợp lệ") String fullname,
			@Email(message = "Email không hợp lệ. Vui lòng thử lại") @NotBlank(message = "Email không hợp lệ. Vui lòng thử lại") @Size(min = 6, max = 80, message = "Email không hợp lệ. Vui lòng thử lại") String email,
			@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Tài khoản chỉ có thể chứa số và chữ cái") @NotEmpty(message = "Tài khoản không được bỏ trống") @NotBlank(message = "Tài khoản không được bỏ trống hoặc chứa khoảng trắng") @Size(min = 8, max = 50, message = "Tài khoản có độ dài tối thiểu 8 ký tự và tối đa 50 ký tự") String username,
			@NotEmpty(message = "Mật khẩu không được bỏ trống") @NotBlank(message = "Mật khẩu không được bỏ trống hoặc chỉ là khoảng trắng") @Size(min = 8, max = 100, message = "Mật khẩu có độ dài tối thiểu là 8 ký tự và tối đa 100 ký tự") @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "Mật khẩu phải chứa ít nhất một chữ cái và một chữ số.") String password,
			@NotEmpty(message = "Mật khẩu không được bỏ trống") @NotBlank(message = "Mật khẩu không được bỏ trống hoặc chỉ là khoảng trắng") @Size(min = 8, max = 100, message = "Mật khẩu có độ dài tối thiểu là 8 ký tự và tối đa 100 ký tự") @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "Mật khẩu phải chứa ít nhất một chữ cái và một chữ số.") String confirmPassword) {
		this.fullname = fullname;
		this.email = email;
		this.username = username;
		this.password = password;
		this.confirmPassword = confirmPassword;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
}
