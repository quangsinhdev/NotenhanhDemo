package com.notenhanh.domain.dto.UserFeaturesDTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;

public class ReviewDTO {
	private Long id;
	@Email(message = "Email không hợp lệ")
	@NotEmpty(message = "Email không được bỏ trống")
	private String email;

	@NotEmpty(message = "Họ tên hoặc nickname không thể bỏ trống")
	@Size(min = 4, max = 80, message = "Họ tên hoặc nickname tối thiểu 4 ký tự")
	@Pattern(regexp = "^[\\p{L}0-9\\s.-]+$", message = "Họ tên hoặc nickname không hợp lệ")
	private String fullname;

	@NotEmpty(message = "Tiêu đề không được bỏ trống")
	@Size(min = 15, max = 100, message = "Tiêu đề phản hồi ít nhất 15 ký tự và tối đa 100 ký tự")
	@Pattern(regexp = "^[\\p{L}0-9][\\p{L}0-9\\s:.-]*$", message = "Tiêu đề Feedback không hợp lệ")
	private String title;

	@NotEmpty(message = "Nội dung không được bỏ trống")
	@Pattern(regexp = "^[\\p{L}0-9\\s\\n,.!?\\+]*$", message = "Nội dung đánh giá không hợp lệ")
	@Size(max = 500, message = "Nội dung đánh giá không được vượt quá 500 ký tự")
	private String content;

	public ReviewDTO(Long id,
			@Email(message = "Email không hợp lệ") @NotEmpty(message = "Email không được bỏ trống") String email,
			@NotEmpty(message = "Họ tên hoặc nickname không thể bỏ trống") @Size(min = 4, max = 80, message = "Họ tên hoặc nickname tối thiểu 4 ký tự") @Pattern(regexp = "^[\\p{L}\\s]+$", message = "Họ tên hoặc nickname không hợp lệ") String fullname,
			@NotEmpty(message = "Tiêu đề không được bỏ trống") @Size(min = 15, max = 100, message = "Tiêu đề phản hồi ít nhất 15 ký tự và tối đa 100 ký tự") @Pattern(regexp = "^[A-Za-z0-9áàảãạăắằẳẵặâấầẩẫậéèẻẽẹêếềểễệíìỉĩịóòỏõọôốồổỗộơớờởỡợúùủũụưứừửữựýỳỷỹỵ ,.]*$", message = "Tiêu đề ghi chú không hợp lệ") String title,
			@NotEmpty(message = "Nội dung không được bỏ trống") @Pattern(regexp = "^[A-Za-z0-9áàảãạăắằẳẵặâấầẩẫậéèẻẽẹêếềểễệíìỉĩịóòỏõọôốồổỗộơớờởỡợúùủũụưứừửữựýỳỷỹỵ \n,.!?+]*$", message = "Nội dung đánh giá không hợp lệ") String content) {
		this.id = id;
		this.email = email;
		this.fullname = fullname;
		this.title = title;
		this.content = content;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return fullname;
	}

	public void setFullName(String fullname) {
		this.fullname = fullname;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
