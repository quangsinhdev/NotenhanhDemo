package com.notenhanh.domain;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.notenhanh.enumation.UserStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;


@Entity
@Table(name="Users")
public class Users {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotEmpty(message="Tài khoản không thể bỏ trống")
	@NotBlank(message="Tài khoản không được chứa khoảng trắng")
	private String username;
	@NotEmpty(message="Mật khẩu không thể bỏ trống")
	@NotBlank(message="Mật khẩu không được chứa khoảng trắng")
	private String password;
    @Email(message = "Email không hợp lệ", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
	private String email;
    @NotEmpty(message="Họ tên hoặc nickname không thể bỏ trống")
    private String fullname;
	@Enumerated(EnumType.STRING)
	private UserStatus status = UserStatus.ACTIVE;
	private long totalnote = 0;
	private long totalTextnote = 0;
	private long totalTasknote = 0;
	private String avatarphoto ="/images/default-avatar.png";
	private LocalDateTime createAt = LocalDateTime.now();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Textnote> textnote;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Tasknote> tasknote;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Review> review;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private Premium premium;
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinColumn(name="roleID")
	private RoleUser roleUser;
	private String tokenRecovery;
    private Date tokenExpireDate;
    private String provider;
    private String providerId;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public UserStatus getStatus() {
		return status;
	}
	public void setStatus(UserStatus status) {
		this.status = status;
	}
	public long getTotalnote() {
		return totalnote;
	}
	public void setTotalnote(Long totalnote) {
		this.totalnote = totalnote;
	}
	public long getTotalTextnote() {
		return totalTextnote;
	}
	public void setTotalTextnote(Long totalTextnote) {
		this.totalTextnote = totalTextnote;
	}
	public long getTotalTasknote() {
		return totalTasknote;
	}
	public void setTotalTasknote(Long totalTasknote) {
		this.totalTasknote = totalTasknote;
	}
	public String getAvatarphoto() {
		return avatarphoto;
	}
	public void setAvatarphoto(String avatarphoto) {
		this.avatarphoto = avatarphoto;
	}
	public LocalDateTime getCreateAt() {
		return createAt;
	}
	public void setCreateAt(LocalDateTime createAt) {
		this.createAt = createAt;
	}
	public List<Textnote> getTextnote() {
		return textnote;
	}
	public void setTextnote(List<Textnote> textnote) {
		this.textnote = textnote;
	}
	public List<Tasknote> getTasknote() {
		return tasknote;
	}
	public void setTasknote(List<Tasknote> tasknote) {
		this.tasknote = tasknote;
	}
	
	public List<Review> getReview() {
		return review;
	}

	public void setReview(List<Review> review) {
		this.review = review;
	}

	public Premium getPremium() {
		return premium;
	}
	public void setPremium(Premium premium) {
		this.premium = premium;
	}
	public RoleUser getRoleUser() {
		return roleUser;
	}
	public void setRoleUser(RoleUser roleUser) {
		this.roleUser = roleUser;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getTokenRecovery() {
		return tokenRecovery;
	}
	public void setTokenRecovery(String tokenRecovery) {
		this.tokenRecovery = tokenRecovery;
	}
	public Date getTokenExpireDate() {
		return tokenExpireDate;
	}
	public void setTokenExpireDate(Date tokenExpireDate) {
		this.tokenExpireDate = tokenExpireDate;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getProviderId() {
		return providerId;
	}
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	
	
	
}
