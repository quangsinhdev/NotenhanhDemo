package com.notenhanh.service.user;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import com.notenhanh.domain.Users;
import com.notenhanh.exception.userauthentication.RecoveryException;
import com.notenhanh.repository.UserRepository;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class RecoveryService {
	private static final Logger log = LoggerFactory.getLogger(RecoveryService.class);
	private final UserRepository userRepository;
	private JavaMailSender mailSender;
	private BCryptPasswordEncoder passwordEncoder;
	private TemplateEngine templateEngine;
	private LoginAttemptService loginAttemptService;
	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static final long TOKEN_EXPIRY_TIME = 3600000;

	public RecoveryService(UserRepository userRepository, LoginAttemptService loginAttemptService,
			JavaMailSender mailSender, BCryptPasswordEncoder passwordEncoder, TemplateEngine templateEngine) {
		this.userRepository = userRepository;
		this.loginAttemptService = loginAttemptService;
		this.mailSender = mailSender;
		this.passwordEncoder = passwordEncoder;
		this.templateEngine = templateEngine;
	}

	public boolean sendRecoveryMail(String username, String email, HttpServletRequest httpServletRequest) {
		String userAgent = httpServletRequest.getHeader("User-Agent");
		String timerequest = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"));

		Users user = getUserByUsername(username);
		if (user == null || !user.getEmail().equals(email)) {
			return false;
		}

		String tokenCreate = createTokenForUser(user);
		if (checkExistTokenRecovery(tokenCreate)) {
			log.error("Error sending email: Duplicated token");
			throw new RecoveryException("Đã xảy ra lỗi khi khôi phục mật khẩu");
		}

		if (!saveUserWithNewToken(user)) {
			throw new RecoveryException("Đã xảy ra lỗi khi lưu thông tin khôi phục");
		}

		String recoveryURL = buildRecoveryURL(username, tokenCreate);
		String emailContent = buildEmailContent(recoveryURL, userAgent, timerequest);

		return sendRecoveryEmail(email, emailContent);
	}

	private Users getUserByUsername(String username) {
		Optional<Users> userOptional = userRepository.findByUsername(username);
		return userOptional.orElse(null);
	}

	private String createTokenForUser(Users user) {
		String tokenCreate;

		do {
			tokenCreate = generateRandomToken();
		} while (userRepository.existsByTokenRecovery(tokenCreate));

		user.setTokenRecovery(tokenCreate);
		user.setTokenExpireDate(new Date(System.currentTimeMillis() + TOKEN_EXPIRY_TIME));
		return tokenCreate;
	}

	private boolean saveUserWithNewToken(Users user) {
		try {
			userRepository.save(user);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private String buildRecoveryURL(String username, String tokenCreate) {
		return "https://localhost:8443/updatepassword?username=" + username + "&tokenrecovery=" + tokenCreate;
	}

	private String buildEmailContent(String recoveryURL, String userAgent, String timerequest) {
		Date expireTime = new Date(System.currentTimeMillis() + TOKEN_EXPIRY_TIME);
		String TimeTokenExpire = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(expireTime);

		Context context = new Context();
		context.setVariable("recoveryURL", recoveryURL);
		context.setVariable("device", getDevice(userAgent));
		context.setVariable("browser", getBrowser(userAgent));
		context.setVariable("os", getOS(userAgent));
		context.setVariable("timerequest", timerequest);
		context.setVariable("TimeTokenExpire", TimeTokenExpire);
		return templateEngine.process("MailReceiver", context);
	}

	private boolean sendRecoveryEmail(String email, String contentMail) {
		try {
			sendMail(email, "Khôi phục mật khẩu tại Notenhanh", contentMail);
			return true;
		} catch (Exception e) {
			throw new RecoveryException("Đã xảy ra lỗi khi khôi phục mật khẩu");
		}
	}

	private void sendMail(String to, String subject, String text) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(text, true);
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			log.error("Error sending email: " + e.getMessage(), e);
			throw new RecoveryException("Đã xảy ra lỗi khi gửi email khôi phục mật khẩu. Vui lòng thử lại sau.");
		} catch (MailAuthenticationException e) {
			log.error("Authentication error sending email: " + e.getMessage(), e);
			throw new RecoveryException("Đã xảy ra lỗi khi gửi email khôi phục mật khẩu. Vui lòng thử lại sau.");
		} catch (MailException e) {
			log.error("Mail error: " + e.getMessage(), e);
			throw new RecoveryException("Đã xảy ra lỗi khi gửi email khôi phục mật khẩu. Vui lòng thử lại sau.");
		}
	}

	public boolean checkAvailableTokenRecovery(String tokenrecovery) {
		Optional<Users> userOptional = userRepository.findByTokenRecovery(tokenrecovery);
		if (userOptional.isPresent()) {
			Users user = userOptional.get();
			if (user.getTokenExpireDate().after(new Date())) {
				return true;
			}
		}
		return false;
	}

	public boolean checkExistTokenRecovery(String tokenrecovery) {
		return userRepository.existsByTokenRecovery(tokenrecovery);
	}

	public boolean checkNewPasswordAndCurrentPassword(String tokenrecovery, String NewPassword) {
		Optional<Users> userOptional = userRepository.findByTokenRecovery(tokenrecovery);

		if (!userOptional.isPresent()) {
			throw new NoSuchElementException("Không tìm thấy người dùng với token này");
		}

		Users user = userOptional.get();

		if (passwordEncoder.matches(NewPassword, user.getPassword())) {
			return false;
		}

		return true;
	}

	public Users findUserbyTokenRecoveryAndUsername(String tokenRecovery, String username) {
		Optional<Users> userOptional = userRepository.findByTokenRecoveryAndUsername(tokenRecovery, username);

		if (!userOptional.isPresent()) {
			throw new RecoveryException("Token không hợp lệ hoặc không khớp với username.");
		}

		return userOptional.get();
	}

	public Users findUserbyTokenRecovery(String tokenRecovery) {
		Optional<Users> userOptional = userRepository.findByTokenRecovery(tokenRecovery);

		if (!userOptional.isPresent()) {
			throw new RecoveryException("Không tìm thấy người dùng: Invalid token.");
		}

		return userOptional.get();
	}

	public boolean checkUsernameAvailable(String Username) {
		return userRepository.existsByUsername(Username);
	}

	public boolean checkEmailAvailable(String email) {
		return userRepository.existsByEmail(email);
	}

	public boolean UsernameAndPasswordMatch(String username, String password) {
		return username.equals(password);
	}

	public boolean UpdatePassword(String tokenrecovery, String newPassword) {
		Optional<Users> userOptional = userRepository.findByTokenRecovery(tokenrecovery);
		if (userOptional.isPresent()) {
			Users user = userOptional.get();
			user.setPassword(passwordEncoder.encode(newPassword));
			user.setTokenRecovery(null);
			user.setTokenExpireDate(null);
			userRepository.save(user);
			loginAttemptService.clearLock(user.getUsername());
			return true;
		}
		return false;
	}

	public boolean CheckConfirmNewPassword(String newPassword, String confirmNewPassword) {
		return (newPassword.equals(confirmNewPassword));
	}

	private static String getBrowser(String userAgent) {
		if (userAgent.contains("Chrome")) {
			return "Google Chrome";
		} else if (userAgent.contains("Firefox")) {
			return "Mozilla Firefox";
		} else if (userAgent.contains("Safari")) {
			return "Safari";
		} else if (userAgent.contains("Edge")) {
			return "Microsoft Edge";
		} else {
			return "Unknown Browser";
		}
	}

	private static String getOS(String userAgent) {
		if (userAgent.contains("Windows NT")) {
			return "Windows";
		} else if (userAgent.contains("Mac OS X")) {
			return "Mac OS";
		} else if (userAgent.contains("Linux")) {
			return "Linux";
		} else if (userAgent.contains("Android")) {
			return "Android";
		} else if (userAgent.contains("iPhone") || userAgent.contains("iPad")) {
			return "iOS";
		} else {
			return "Unknown OS";
		}
	}

	private static String getDevice(String userAgent) {
		if (userAgent.contains("Mobi")) {
			return "Mobile Device";
		} else if (userAgent.contains("Tablet")) {
			return "Tablet";
		} else {
			return "Desktop";
		}
	}

	private static String generateRandomToken() {
		SecureRandom random = new SecureRandom();
		int length = random.nextInt(6) + 15;

		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			int index = random.nextInt(CHARACTERS.length());
			sb.append(CHARACTERS.charAt(index));
		}

		return sb.toString();
	}

}
