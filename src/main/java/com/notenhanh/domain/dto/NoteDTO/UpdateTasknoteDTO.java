package com.notenhanh.domain.dto.NoteDTO;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.validation.constraints.Pattern;

import com.notenhanh.enumation.Privacy;
import com.notenhanh.service.validator.EnumValidator;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UpdateTasknoteDTO {

	@NotEmpty(message = "Tiêu đề không được bỏ trống")
	@Size(min = 1, max = 120, message = "Tiêu đề tối đa 120 ký tự")
	@Pattern(regexp = "^[\\p{L}0-9][\\p{L}0-9\\s]*$", message = "Tiêu đề ghi chú không hợp lệ")
	private String title;

	@Max(value = 50, message = "Số công việc không thể vượt quá 50")
	private int numbertask;

	@NotEmpty(message = "Danh sách công việc không được bỏ trống")
	private List<TaskDTO> tasks;

	@EnumValidator(enumClass = Privacy.class, message = "Quyền riêng tư không hợp lệ")
	private Privacy privacy;

	@Pattern(regexp = "^[A-Za-z0-9]*$", message = "URL không hợp lệ")
	private String url;
	private LocalDateTime updatedAt;

	public UpdateTasknoteDTO(
			@NotEmpty(message = "Tiêu đề không được bỏ trống") @Size(min = 1, max = 120, message = "Tiêu đề tối đa 120 ký tự") @Pattern(regexp = "^[A-Za-z0-9áàảãạăắằẳẵặâấầẩẫậéèẻẽẹêếềểễệíìỉĩịóòỏõọôốồổỗộơớờởỡợúùủũụưứừửữựýỳỷỹỵ ]*$", message = "Tiêu đề ghi chú không hợp lệ") String title,
			@Max(value = 50, message = "Số công việc không thể vượt quá 50") int numbertask,
			@NotEmpty(message = "Danh sách công việc không được bỏ trống") List<TaskDTO> tasks, Privacy privacy,
			@Pattern(regexp = "^[A-Za-z0-9]*$", message = "URL không hợp lệ") String url, LocalDateTime updatedAt) {
		this.title = title;
		this.numbertask = numbertask;
		this.tasks = tasks;
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

	public List<TaskDTO> getTasks() {
		return tasks;
	}

	public void setTasks(List<TaskDTO> tasks) {
		this.tasks = tasks;
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
