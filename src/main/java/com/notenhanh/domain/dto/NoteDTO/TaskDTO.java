package com.notenhanh.domain.dto.NoteDTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class TaskDTO {

    private Long id;
	@NotEmpty(message = "Nội dung Task không được bỏ trống")
	@Size(min = 1, max = 500, message ="Tối thiểu 1 ký tự, Tối đa 500 ký tự 1 task")
	@Pattern(regexp = "^[A-Za-z0-9áàảãạăắằẳẵặâấầẩẫậéèẻẽẹêếềểễệíìỉĩịóòỏõọôốồổỗộơớờởỡợúùủũụưứừửữựýỳỷỹỵ ]*$", message = "Nội dung task không hợp lệ")
    private String content;
	@NotEmpty(message = "Tình trạng task không được bỏ trống")
    private boolean taskStatus;
	public TaskDTO(Long id,
			@NotEmpty(message = "Nội dung Task không được bỏ trống") @Size(min = 1, max = 500, message = "Tối thiểu 1 ký tự, Tối đa 500 ký tự 1 task") String content,
			@NotEmpty(message = "Tình trạng task không được bỏ trống") boolean taskStatus) {
		this.id = id;
		this.content = content;
		this.taskStatus = taskStatus;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public boolean getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(boolean taskStatus) {
		this.taskStatus = taskStatus;
	}

}
