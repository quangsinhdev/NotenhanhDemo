package com.notenhanh.domain.dto.AdminDTO;

import com.notenhanh.enumation.WebStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class SettingsDTO {
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
	public SettingsDTO(Long id, String webtitle, String description, String contactemail, String homepagenotice,
			String keywords, String phonenumber, String policy, WebStatus webstatus) {
		this.id = id;
		this.webtitle = webtitle;
		this.description = description;
		this.contactemail = contactemail;
		this.homepagenotice = homepagenotice;
		this.keywords = keywords;
		this.phonenumber = phonenumber;
		this.policy = policy;
		this.webstatus = webstatus;
	}
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
	public String getContactemail() {
		return contactemail;
	}
	public void setContactemail(String contactemail) {
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
