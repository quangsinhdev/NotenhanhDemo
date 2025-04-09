package com.notenhanh.domain;

import java.util.ArrayList;
import java.util.List;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="Tasknote")
public class Tasknote extends Note{
	@NotNull(message = "Số công việc không thể trống")
	@Min(value = 1, message = "Số công việc phải lớn hơn 0")
	@Max(value = 50, message = "Số công việc không thể vượt quá 50")
	private int numbertask;
	@ManyToOne
	@JoinColumn(name="user_id")
	private Users user;
	
	@OneToMany(mappedBy="taskNote",cascade = CascadeType.ALL)
	private List<Task> task = new ArrayList<>();

	
	public int getNumbertask() {
		return numbertask;
	}

	public void setNumbertask(int numbertask) {
		this.numbertask = numbertask;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public List<Task> getTask() {
		return task;
	}

	public void setTask(List<Task> task) {
		this.task = task;
	}
	
}
