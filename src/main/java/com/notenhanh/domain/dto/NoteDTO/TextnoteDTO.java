package com.notenhanh.domain.dto.NoteDTO;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

import com.notenhanh.enumation.Privacy;
import com.notenhanh.service.validator.EnumValidator;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class TextnoteDTO {
	private Long id;
	
	@NotEmpty(message = "Tiêu đề không được bỏ trống")
	@Size(min = 1, max = 120, message ="Tiêu đề tối đa 120 ký tự")
	@Pattern(regexp = "^[A-Za-z0-9áàảãạăắằẳẵặâấầẩẫậéèẻẽẹêếềểễệíìỉĩịóòỏõọôốồổỗộơớờởỡợúùủũụưứừửữựýỳỷỹỵ ]*$", message = "Tiêu đề ghi chú không hợp lệ")
	private String title;
	
	@NotEmpty(message = "Loại ghi chú không được bỏ trống")
	@Pattern(regexp = "^[a-zA-Z]+$", message = "Loại ghi chú không hợp lệ")
	private String type;
	
	@NotEmpty(message = "Nội dung ghi chú không được bỏ trống")
	@Size(min = 1, max = 5000, message ="Tối đa 5000 ký tự 1 ghi chú")
	private String content;
	
	@Pattern(regexp = "^[A-Za-z0-9]*$", message = "URL không hợp lệ")
	private String url;
	
	@NotNull(message = "Privacy is required")
	@EnumValidator(enumClass = Privacy.class, message = "Quyền riêng tư không hợp lệ")
	private Privacy privacy;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
		
	public TextnoteDTO(Long id,
			@NotEmpty(message = "Tiêu đề không được bỏ trống") @Size(min = 1, max = 120, message = "Tiêu đề tối đa 120 ký tự") String title,
			@NotEmpty(message = "Loại ghi chú không được bỏ trống") @Pattern(regexp = "^[a-zA-Z]+$", message = "Loại ghi chú không hợp lệ") String type,
			@NotEmpty(message = "Nội dung ghi chú không được bỏ trống") @Size(min = 1, max = 5000, message = "Tối đa 5000 ký tự 1 ghi chú") String content,
			String url, @NotNull(message = "Privacy is required") Privacy privacy, LocalDateTime createdAt,
			LocalDateTime updatedAt) {
		this.id = id;
		this.title = title;
		this.type = type;
		this.content = content;
		this.url = url;
		this.privacy = privacy;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Privacy getPrivacy() {
		return privacy;
	}
	public void setPrivacy(Privacy privacy) {
		this.privacy = privacy;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	

	
}
