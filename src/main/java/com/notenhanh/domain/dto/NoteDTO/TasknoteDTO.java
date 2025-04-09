package com.notenhanh.domain.dto.NoteDTO;

import java.time.LocalDateTime;
import java.util.List;

import com.notenhanh.enumation.Privacy;
import com.notenhanh.service.validator.EnumValidator;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class TasknoteDTO {
	private Long id;
	@NotEmpty(message = "Tiêu đề không được bỏ trống")
	@Size(min = 1, max = 120, message = "Tiêu đề tối đa 120 ký tự")
	@Pattern(regexp = "^[\\p{L}0-9][\\p{L}0-9\\s]*$", message = "Tiêu đề ghi chú không hợp lệ")
	private String title;
	@NotEmpty(message = "Loại ghi chú không được bỏ trống")
	@Pattern(regexp = "^[a-zA-Z]+$", message = "Loại ghi chú không hợp lệ")
	private String type;

	@Pattern(regexp = "^[A-Za-z0-9]*$", message = "URL không hợp lệ")
	private String url;

	@NotNull(message = "Quyền riêng tư không được bỏ trống")
	@EnumValidator(enumClass = Privacy.class, message = "Quyền riêng tư không hợp lệ")
	private Privacy privacy;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	@NotNull(message = "Số công việc không thể trống")
	@Min(value = 1, message = "Số công việc phải lớn hơn 0")
	@Max(value = 50, message = "Số công việc không thể vượt quá 50")
	private int numbertask;

	@NotEmpty(message = "Danh sách công việc không được để trống")
	private List<TaskDTO> tasks;

	public TasknoteDTO(Long id,
			@NotEmpty(message = "Tiêu đề không được bỏ trống") @Size(min = 1, max = 120, message = "Tiêu đề tối đa 120 ký tự") @Pattern(regexp = "^[A-Za-z0-9áàảãạăắằẳẵặâấầẩẫậéèẻẽẹêếềểễệíìỉĩịóòỏõọôốồổỗộơớờởỡợúùủũụưứừửữựýỳỷỹỵ ]*$", message = "Tiêu đề ghi chú không hợp lệ") String title,
			@NotEmpty(message = "Loại ghi chú không được bỏ trống") @Pattern(regexp = "^[a-zA-Z]+$", message = "Loại ghi chú không hợp lệ") String type,
			@Pattern(regexp = "^[A-Za-z0-9]*$", message = "URL không hợp lệ") String url,
			@NotNull(message = "Quyền riêng tư không được bỏ trống") Privacy privacy, LocalDateTime createdAt,
			LocalDateTime updatedAt,
			@NotNull(message = "Số công việc không thể trống") @Min(value = 1, message = "Số công việc phải lớn hơn 0") @Max(value = 50, message = "Số công việc không thể vượt quá 50") int numbertask,
			@NotEmpty(message = "Danh sách công việc không được để trống") List<TaskDTO> tasks) {
		this.id = id;
		this.title = title;
		this.type = type;
		this.url = url;
		this.privacy = privacy;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.numbertask = numbertask;
		this.tasks = tasks;
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

	public int getNumbertask() {
		return numbertask;
	}

	public void setNumbertask(int numbertask) {
		this.numbertask = numbertask;
	}

	public List<TaskDTO> getTasks() {
		return tasks;
	}

	public void setTasks(List<TaskDTO> tasks) {
		this.tasks = tasks;
	}

}
