package com.notenhanh.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.notenhanh.domain.Users;
import com.notenhanh.domain.dto.UserAccountDTO.RecoveryDTO;
import com.notenhanh.domain.dto.UserAccountDTO.RecoveryNewPasswordDTO;
import com.notenhanh.domain.dto.UserAccountDTO.RegisterDTO;
import com.notenhanh.exception.userauthentication.LogoutException;
import com.notenhanh.service.user.RecoveryService;
import com.notenhanh.service.user.RegisterUserService;

import jakarta.validation.Valid;



@Controller
public class HomepageController {
	private final RegisterUserService registerUserService;
	private final RecoveryService recoveryService;

	public HomepageController(RegisterUserService registerUserService, RecoveryService recoveryService) {
		this.registerUserService = registerUserService;
		this.recoveryService = recoveryService;
	}
	@GetMapping("/")
	public String HomePage(Model model) {
		model.addAttribute("homepage","homepage");
		return "index";
	}
	@GetMapping("/policy")
	   public String Policy() {
		   return "policy";
	}
	@GetMapping("/login")
    public String loginPage(HttpServletRequest request) {
        if (request.getSession().getAttribute("SPRING_SECURITY_CONTEXT") != null) {
            return "redirect:/dashboard";
        }
        return "login";
    }
	
	@GetMapping("/logout")
	 public String handleLogoutException(@RequestParam String username) {
	     if (username == null || username.isEmpty()) {
	         throw new LogoutException("Đã xảy ra lỗi khi đăng xuất. Vui lòng tải lại trang.");
	     }
	     return "redirect:/login?logout";
	 }
	 
	 @GetMapping("/recovery")
	    public String Recovery(HttpServletRequest request, Model model) {
		 if (request.getSession().getAttribute("SPRING_SECURITY_CONTEXT") != null) {
	            return "redirect:/dashboard";
	     }
		 return "recovery";
	 }
	 
	 @GetMapping("/updatepassword")
  	public String updatePassword(@RequestParam String username, @RequestParam String tokenrecovery, HttpServletRequest request, Model model) {
		 if (request.getSession().getAttribute("SPRING_SECURITY_CONTEXT") != null) {
	            return "redirect:/dashboard";
	        }
	     Users user = recoveryService.findUserbyTokenRecoveryAndUsername(tokenrecovery, username);
	     model.addAttribute("tokenrecovery", tokenrecovery);
	     return "updatepassword";
	 }
	
	 @PostMapping("/login/registry")
	 @ResponseBody
	 public Map<String, Object> Registry(@ModelAttribute("registerUser") @Valid RegisterDTO registerDTO,
	                        BindingResult bindingResult) {

		    Map<String, Object> response = new HashMap<>();
	     
		   if (bindingResult.hasErrors()) {
		        List<String> errorMessages = bindingResult.getAllErrors().stream()
		                .map(objectError -> objectError.getDefaultMessage())
		                .collect(Collectors.toList());

		        response.put("success", false);
		        response.put("errorMessages", errorMessages);
		        return response;
		    }


		   else if (this.registerUserService.checkUsernameValid(registerDTO.getUsername())) {
	    	 response.put("success", false);
	         response.put("errorMessage", "Tên tài khoản đã tồn tại! Hãy thử lại");
	         return response;
	     }
		   else if (this.registerUserService.checkEmailValid(registerDTO.getEmail())) {
	    	 response.put("success", false);
	         response.put("errorMessage", "Email đã tồn tại! Hãy thử lại email khác");
	         return response;
	     }
		   else if (this.registerUserService.UsernameAndPasswordMatch(registerDTO.getUsername(), registerDTO.getPassword())) {
	    	 response.put("success", false);
	         response.put("errorMessage", "Vì lý do bảo mật. Mật khẩu không được giống tài khoản!");
	         return response;
	     }
		   else if (!registerUserService.validatePassword(registerDTO.getPassword(), registerDTO.getConfirmPassword())) {
	            response.put("success", false);
	            response.put("errorMessage", "Mật khẩu chưa trùng khớp");
	            return response;
	        }
		   else {
	     
	     registerUserService.registerUser(registerDTO);
	     response.put("success", true);
	     response.put("successMessage", "Đăng ký tài khoản thành công! Chuyển về trang đăng nhập trong vài giây...");
	     return response;
		   }
	 }

     @PostMapping("/request-recovery")
   	 @ResponseBody
   	 public Map<String, Object> Recover(@ModelAttribute("recoveryUser") @Valid RecoveryDTO recoveryDTO,
   	                        BindingResult bindingResult, HttpServletRequest request) {

   		    Map<String, Object> response = new HashMap<>();
   	     
   		 if (bindingResult.hasErrors()) {
		        List<String> errorMessages = bindingResult.getAllErrors().stream()
		                .map(objectError -> objectError.getDefaultMessage())
		                .collect(Collectors.toList());

		        response.put("success", false);
		        response.put("errorMessages", errorMessages);
		        return response;
		    }
   		 	
   		   if (!recoveryService.checkUsernameAvailable(recoveryDTO.getUsername()) || !recoveryService.checkEmailAvailable(recoveryDTO.getEmail())) {
   			response.put("success", false);
   	        response.put("errorMessage", "Tài khoản hoặc Email không tồn tại");
   	         return response;
   		   }

   		   else if (!this.recoveryService.sendRecoveryMail(recoveryDTO.getUsername(), recoveryDTO.getEmail(), request)) {
   	    	 response.put("success", false);
   	         response.put("errorMessage", "Tài khoản hoặc Email không khớp nhau");
   	         return response;
   	     
   	     }
   	     else {
   	    	 response.put("success", true);
   	         response.put("successMessage", "Thông tin khôi phục đã được gửi đến email: " + recoveryDTO.getEmail());
   	         return response;
   	     }
   	         
   	
     }
      
	 @PostMapping("/change-newpassword")
	 @ResponseBody
	   	public Map<String, Object> changenewpassword(@ModelAttribute @Valid RecoveryNewPasswordDTO recoveryNewPasswordDTO,
	                                                   BindingResult bindingResult, 
	                                                   @RequestParam String tokenrecovery) {
	         Users user = recoveryService.findUserbyTokenRecovery(tokenrecovery);
	         Map<String, Object> response = new HashMap<>();
	         
	         if (bindingResult.hasErrors()) {
			        List<String> errorMessages = bindingResult.getAllErrors().stream()
			                .map(objectError -> objectError.getDefaultMessage())
			                .collect(Collectors.toList());

			        response.put("success", false);
			        response.put("errorMessages", errorMessages);
			        return response;
			    }
	         
	         else if (!this.recoveryService.checkAvailableTokenRecovery(recoveryNewPasswordDTO.getTokenrecovery())) {
	             response.put("success", false);
	             response.put("errorMessage", "Liên kết khôi phục đã hết hạn hoặc không hợp lệ. Hãy thử lại!");
	             return response;
	         }
	         
	         else if (recoveryNewPasswordDTO.getNewPassword().equals(user.getUsername())) {
	        	 response.put("success", false);
	        	 response.put("errorMessage", "Vì lý do bảo mật. Mật khẩu và tài khoản không được giống nhau!");
	        	 return response;
}

	         else if (!this.recoveryService.CheckConfirmNewPassword(recoveryNewPasswordDTO.getNewPassword(), 
	                                                           recoveryNewPasswordDTO.getConfirmNewPassword())) {
	             response.put("success", false);
	             response.put("errorMessage", "Mật khẩu mới và mật khẩu xác nhận chưa trùng khớp.");
	             return response;
	         }
	         
	         else if(!this.recoveryService.checkNewPasswordAndCurrentPassword(tokenrecovery, recoveryNewPasswordDTO.getNewPassword())) {

	        	 response.put("success", false);
	        	 response.put("errorMessage", "Mật khẩu mới và mật khẩu hiện tại không được trùng nhau");
	        	 return response;
	        }
	         else if (this.recoveryService.UsernameAndPasswordMatch(user.getUsername(), recoveryNewPasswordDTO.getNewPassword())) {
		    	 response.put("success", false);
		         response.put("errorMessage", "Tài khoản và mật khẩu không được trùng khớp");
		         return response;
		     }
	         
	         else {
	        	 this.recoveryService.UpdatePassword(recoveryNewPasswordDTO.getTokenrecovery(), 
                 recoveryNewPasswordDTO.getNewPassword());

	     	response.put("success", true);
	     	response.put("successMessage", "Đã cập nhật mật khẩu mới thành công. Vui lòng đợi vài giây...");
	     	response.put("successCode", "PASSWORD_UPDATED_SUCCESSFULLY");
	     	return response;
	         }
	     }

}