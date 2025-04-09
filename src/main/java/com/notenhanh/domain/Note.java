package com.notenhanh.domain;
import java.time.LocalDateTime;

import com.notenhanh.enumation.Privacy;
import com.notenhanh.service.validator.EnumValidator;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@MappedSuperclass
public abstract class Note {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		@NotEmpty(message = "Tiêu đề không được bỏ trống")
		private String title;
		@NotEmpty(message = "Loại ghi chú không được bỏ trống")
		@Pattern(regexp = "^[a-zA-Z]+$", message = "Loại ghi chú không hợp lệ")
		private String type;
		private String url;
		@NotNull(message = "Privacy is required")
		@EnumValidator(enumClass = Privacy.class, message = "Quyền riêng tư không hợp lệ")
		private Privacy privacy;
		private LocalDateTime createdAt;
		private LocalDateTime updatedAt;
	    private String formattedCreatedAt;
	    private String formattedUpdatedAt;
		public void setId(Long id) {
		this.id = id;
		}
		public Long getId() {
			return id;
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
		
		  public String getFormattedCreatedAt() {
			return formattedCreatedAt;
		}
		public void setFormattedCreatedAt(String formattedCreatedAt) {
			this.formattedCreatedAt = formattedCreatedAt;
		}
		public String getFormattedUpdatedAt() {
			return formattedUpdatedAt;
		}
		public void setFormattedUpdatedAt(String formattedUpdatedAt) {
			this.formattedUpdatedAt = formattedUpdatedAt;
		}
		@PrePersist
		    public void prePersist() {
		        this.createdAt = LocalDateTime.now();
		    }

		    @PreUpdate
		    public void preUpdate() {
		        this.updatedAt = LocalDateTime.now();
		    }
		
		
}
