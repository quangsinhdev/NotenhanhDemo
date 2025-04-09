package com.notenhanh.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.notenhanh.exception.note.AccessDeniedUpdateTasknoteIDException;
import com.notenhanh.exception.note.AccessDeniedUpdateTasknoteURLException;
import com.notenhanh.exception.note.AccessDeniedUpdateTextnoteIDException;
import com.notenhanh.exception.note.AccessDeniedUpdateTextnoteURLException;
import com.notenhanh.exception.note.PrivacyAccessDeniedException;
import com.notenhanh.exception.note.TextnoteNotFoundException;
import com.notenhanh.exception.settings.SettingWebException;
import com.notenhanh.exception.userauthentication.LogoutException;
import com.notenhanh.exception.userauthentication.RecoveryException;
import com.notenhanh.exception.userauthentication.RegisterException;
import com.notenhanh.exception.userauthentication.UserNotFoundException;
@ControllerAdvice
public class ExceptionHandleController {
	
	public static final Logger log = LoggerFactory.getLogger(ExceptionHandleController.class);
    @ExceptionHandler(LogoutException.class)
    public ResponseEntity<String> handleLogoutException(LogoutException ex) {
        return new ResponseEntity<>("Đã xảy ra lỗi khi đăng xuất: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RegisterException.class)
    @ResponseBody
    public Map<String, Object> handleRegisterException(RegisterException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("errorMessage", "Đã xảy ra lỗi khi đăng ký: " + ex.getMessage());
        return response;
    }
    

    @ExceptionHandler(RecoveryException.class)
    public String handleRecoveryException(RecoveryException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        	return "redirect:/recovery";
    }
    
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseBody
    public Map<String, Object> handleNoSuchElementException(NoSuchElementException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("errorMessage", "Thông tin không hợp lệ");
        log.error("Lỗi khi xử lý yêu cầu: ", ex);
        return response;
    }
    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDenied(AccessDeniedException ex, Model model) {
        model.addAttribute("titleErrorMessage", "Access Denied");
        model.addAttribute("errorMessage", "Bạn không đủ quyền truy cập vào tính năng hoặc dữ liệu này.");
        return "error";
    }

    @ExceptionHandler(TextnoteNotFoundException.class)
    public String handleTextnoteNotFound(TextnoteNotFoundException ex, Model model) {
        model.addAttribute("titleErrorMessage", "Textnote Not Found.");
        model.addAttribute("errorMessage", "Không tìm thấy Textnote! Vui lòng kiểm tra lại.");
        return "error";
    }
    
    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(UserNotFoundException ex, Model model) {
        return "redirect:/login";
    }
    
    @ExceptionHandler(AccessDeniedUpdateTextnoteIDException.class)
    public String handleAccessDeniedUpdateTextnoteIDException(AccessDeniedUpdateTextnoteIDException ex, Model model) {
        model.addAttribute("titleResponseMessage","Textnote Update Failed");
        model.addAttribute("responseMessage","Bạn không có quyền cập nhật ghi chú này.");
        return "redirect:/notestatistics?type=textnote";
    }
    
    @ExceptionHandler(AccessDeniedUpdateTextnoteURLException.class)
    public String handleAccessDeniedUpdateTextnoteURLxception(AccessDeniedUpdateTextnoteURLException ex, Model model) {
    	model.addAttribute("titleErrorMessage","Textnote Update Failed");
        model.addAttribute("responseMessage","Bạn không có quyền cập nhật ghi chú này.");
        return "error";
    }
    
    @ExceptionHandler(AccessDeniedUpdateTasknoteIDException.class)
    public String handleAccessDeniedUpdateTasknoteIDException(AccessDeniedUpdateTasknoteIDException ex, Model model) {
        model.addAttribute("titleResponseMessage","Tasknote Update Failed");
        model.addAttribute("responseMessage","Bạn không có quyền cập nhật ghi chú này.");
        return "redirect:/notestatistics?type=tasknote";
    }
    
    @ExceptionHandler(AccessDeniedUpdateTasknoteURLException.class)
    public String handleAccessDeniedUpdateTasknoteURLException(AccessDeniedUpdateTasknoteURLException ex, Model model) {
    	model.addAttribute("titleErrorMessage","Tasknote Update Failed");
        model.addAttribute("responseMessage","Bạn không có quyền cập nhật ghi chú này.");
        return "error";
    }    
    @ExceptionHandler(PrivacyAccessDeniedException.class)
    public String handlePrivacyAccessDeniedException(PrivacyAccessDeniedException ex, Model model) {
        model.addAttribute("titleErrorMessage","Privacy Access Denied.");
        model.addAttribute("errorMessage","Bạn không có quyền truy cập vào ghi chú này");
        return "error";
    }
    
    @ExceptionHandler(SecurityException.class)
    public String handleSecurityException(SecurityException ex, Model model) {
    	return "redirect:/notestatistics?type=tasknote";
    }
    
    @ExceptionHandler(SettingWebException.class)
    public String handleSettingWebException(SettingWebException ex, Model model, RedirectAttributes redirectAttributes) {
    	redirectAttributes.addFlashAttribute("errorMessage","Cập nhật thông tin Web thất bại!");
    	return "redirect:/admin/settings";
    }
    
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
    	log.error("Đã xảy ra lỗi: {}", e.getMessage(), e);
        model.addAttribute("error", "Đã xảy ra lỗi: " + e.getMessage());
        return "error";
    }
    
}
