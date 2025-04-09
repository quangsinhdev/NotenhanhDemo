package com.notenhanh.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="Textnote")
public class Textnote extends Note{
	@NotEmpty(message = "Nội dung ghi chú không được bỏ trống")
	@Size(min = 1, max = 5000, message ="Tối đa 5000 ký tự 1 ghi chú")
	private String content;
	@ManyToOne
	@JoinColumn(name="user_id")
	private Users user;

	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}
	
}
