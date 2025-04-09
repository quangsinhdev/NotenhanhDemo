package com.notenhanh.security;

import java.io.IOException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.security.core.AuthenticationException;
import com.notenhanh.service.user.LoginAttemptService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler  {
	 private final LoginAttemptService loginAttemptService;
	    public CustomAuthenticationFailureHandler(LoginAttemptService loginAttemptService) {
	        this.loginAttemptService = loginAttemptService;
	    }
	    @Override
	    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
	                                         AuthenticationException exception) throws IOException, ServletException {

	        String username = request.getParameter("username");
	        loginAttemptService.loginFailed(username);

	        if (loginAttemptService.CheckAccountLocked(username)) {
	            long lockTime = loginAttemptService.getLockTime(username);
	            long currentTime = System.currentTimeMillis();
	            long remainingLockTime = lockTime - currentTime;

	            if (remainingLockTime > 0) {
	                long remainingMinutes = remainingLockTime / 60000;
	                if (remainingLockTime % 60000 != 0) {
	                    remainingMinutes += 1;
	                }
	                
	                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
	                    response.setContentType("application/json");
	                    response.getWriter().write("{\"status\": \"error\", \"message\": \"Tài khoản bị tạm khóa vì đăng nhập sai nhiều lần. Thử lại sau: " + remainingMinutes + " phút\", \"lockTime\": " + lockTime + "}");
	            }
	        } else if (exception instanceof BadCredentialsException) {
	            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
	                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	                response.setContentType("application/json");
	                response.getWriter().write("{\"status\": \"error\", \"message\": \"Thông tin đăng nhập không chính xác. Vui lòng thử lại.\"}");
	            } else {
	                request.getSession().setAttribute("invalidMessage", "Thông tin đăng nhập không chính xác. Vui lòng thử lại.");
	                response.sendRedirect("/login");
	            }
	        } else {
	            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
	                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	                response.setContentType("application/json");
	                response.getWriter().write("{\"status\": \"error\", \"message\": \"Đã xảy ra lỗi khi đăng nhập.\"}");
	            } else {
	                request.getSession().setAttribute("loginfailedMessage", "Đã xảy ra lỗi khi đăng nhập.");
	                response.sendRedirect("/login");
	            }
	        }
	    }

}