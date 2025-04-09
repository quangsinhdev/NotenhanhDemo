package com.notenhanh.domain.dto.NoteDTO;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import com.notenhanh.enumation.Privacy;
import com.notenhanh.service.validator.EnumValidator;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UpdateTextnoteDTO {
	@NotEmpty(message = "Tiêu đề không được bỏ trống")
	@Size(min = 1, max = 120, message = "Tiêu đề tối đa 120 ký tự")
	@Pattern(regexp = "^[\\p{L}0-9][\\p{L}0-9\\s]*$", message = "Tiêu đề ghi chú không hợp lệ")
	private String title;

	@NotEmpty(message = "Nội dung ghi chú không được bỏ trống")
	@Size(min = 1, max = 5000, message = "Tối đa 5000 ký tự 1 ghi chú")
	private String content;

	@EnumValidator(enumClass = Privacy.class, message = "Quyền riêng tư không hợp lệ")
	private Privacy privacy;

	@Pattern(regexp = "^[A-Za-z0-9]*$", message = "URL không hợp lệ")
	private String url;
	private LocalDateTime updatedAt;

	public UpdateTextnoteDTO(
			@NotEmpty(message = "Tiêu đề không được bỏ trống") @Size(min = 1, max = 120, message = "Tiêu đề tối đa 120 ký tự") String title,
			@NotEmpty(message = "Nội dung ghi chú không được bỏ trống") @Size(min = 1, max = 5000, message = "Tối đa 5000 ký tự 1 ghi chú") String content,
			@NotNull(message = "Privacy is required") Privacy privacy, String url, LocalDateTime updatedAt) {
		this.title = title;
		this.content = content;
		this.privacy = privacy;
		this.url = url;
		this.updatedAt = updatedAt;
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

	public Privacy getPrivacy() {
		return privacy;
	}

	public void setPrivacy(Privacy privacy) {
		this.privacy = privacy;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

}
