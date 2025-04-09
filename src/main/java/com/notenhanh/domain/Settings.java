package com.notenhanh.domain;
import com.notenhanh.enumation.*;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name="Settings")
public class Settings {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String webtitle;
	private String description;
	private String contactemail;
	private String homepagenotice;
	private String keywords;
	private String phonenumber;
	private String policy;
	@Enumerated(EnumType.STRING)
	private WebStatus webstatus;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getWebtitle() {
		return webtitle;
	}
	public void setWebtitle(String webtitle) {
		this.webtitle = webtitle;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getContactEmail() {
		return contactemail;
	}
	public void setContactEmail(String contactemail) {
		this.contactemail = contactemail;
	}
	public String getHomepagenotice() {
		return homepagenotice;
	}
	public void setHomepagenotice(String homepagenotice) {
		this.homepagenotice = homepagenotice;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getPhonenumber() {
		return phonenumber;
	}
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
	public String getPolicy() {
		return policy;
	}
	public void setPolicy(String policy) {
		this.policy = policy;
	}
	public WebStatus getWebstatus() {
		return webstatus;
	}
	public void setWebstatus(WebStatus webstatus) {
		this.webstatus = webstatus;
	}
	
}
