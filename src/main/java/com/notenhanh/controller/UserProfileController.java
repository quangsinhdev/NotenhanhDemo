package com.notenhanh.controller;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.notenhanh.domain.Users;
import com.notenhanh.domain.dto.UserAccountDTO.ChangePasswordDTO;
import com.notenhanh.exception.userauthentication.UserNotFoundException;
import com.notenhanh.repository.UserRepository;
import com.notenhanh.service.user.ChangePasswordService;
import com.notenhanh.service.user.UserService;

import jakarta.validation.Valid;

@Controller
public class UserProfileController {
	private final UserRepository userRepository;
	private	final UserService userService;
	private final ChangePasswordService changePasswordService;
    public UserProfileController(UserRepository userRepository, UserService userService, ChangePasswordService changePasswordService) {
    	this.changePasswordService = changePasswordService;
		this.userRepository = userRepository;
		this.userService = userService;
	}

    @GetMapping("/client/myprofile")
    public String UserProfile(Model model, Authentication authentication, @AuthenticationPrincipal OAuth2User principal) {
		 getUserInfo(model, authentication, principal);
	        return "client/myprofile";
    }
    @GetMapping("/client/changepassword")
    public String showChangePasswordProfile(Model model, Authentication authentication, @AuthenticationPrincipal OAuth2User principal) {
		if (authentication != null && principal != null){
		       return "redirect:/dashboard";
		    }
    	getUserInfo(model, authentication, principal);
        return "client/changepassword";
    }

	
	@PostMapping("/changepasswordmyprofile")
  	@ResponseBody
	public Map<String, Object> ChangePasswordProfile(@ModelAttribute @Valid ChangePasswordDTO changePasswordDTO, BindingResult bindingResult, Model model, Authentication authentication) {
		
		String username = authentication.getName();
		Users user = userService.getUserByUsername(username).orElseThrow(() -> new UserNotFoundException("Người dùng không tồn tại."));
        Map<String, Object> response = new HashMap<>();
			if (!(authentication instanceof UsernamePasswordAuthenticationToken) || authentication instanceof OAuth2AuthenticationToken) {
		      throw new UsernameNotFoundException("Không tìm thấy người dùng!");
		    }
		    if (bindingResult.hasErrors()) {
		        List<String> errorMessages = bindingResult.getAllErrors().stream()
		                .map(objectError -> objectError.getDefaultMessage())
		                .collect(Collectors.toList());

		        response.put("success", false);
		        response.put("error", errorMessages);
		        return response;
		    }
		    else if(changePasswordDTO.getNewPassword().equals(username)) {
		    	response.put("success", false);
		        response.put("error", "Vì lý do bảo mật. Mật khẩu không được giống tài khoản!");
		        return response;
		    }
		    else if(!changePasswordService.CheckCurrentPassword(changePasswordDTO.getCurrentPassword(), user.getPassword())) {
		    	response.put("success", false);
		        response.put("error", "Mật khẩu hiện tại không chính xác. Hãy kiểm tra lại!");
		        return response;
		    }
		    else if(changePasswordService.CheckCurrentPasswordAndNewPassword(changePasswordDTO.getNewPassword(), user.getPassword())) {
		    	response.put("success", false);
		        response.put("error", "Mật khẩu mới và mật khẩu hiện tại không được giống nhau");
		        return response;
		    }
		    else if(!changePasswordService.CheckConfirmNewPassword(changePasswordDTO.getNewPassword(), changePasswordDTO.getConfirmNewPassword())){
		    	response.put("success", false);
		        response.put("error", "Mật khẩu mới và xác nhận lại mật khẩu mới không khớp");
		        return response;
		    }
		    else {
		    changePasswordService.UpdateNewPassword(username, changePasswordDTO.getNewPassword());
		    response.put("success", true);
	        response.put("successMessage", "Đã cập nhật mật khẩu mới thành công. Vui lòng đợi vài giây...");
	        return response;
		    }
	}
	
	private void getUserInfo(Model model, Authentication authentication, OAuth2User principal) {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");

	    if (authentication != null && principal == null) {
	        String username = authentication.getName();
	        Users user = userService.getUserByUsername(username).orElseThrow(() -> new UserNotFoundException("Người dùng không tồn tại."));
	        if (user != null) {
	            model.addAttribute("fullname", user.getFullname());
	            model.addAttribute("username", user.getUsername());
	            model.addAttribute("avatarphoto", user.getAvatarphoto());
	            model.addAttribute("email", user.getEmail());
	            model.addAttribute("totaltasknote", user.getTotalTasknote());
	            model.addAttribute("totaltextnote", user.getTotalTextnote());
	            model.addAttribute("totalnote", user.getTotalnote());
	            model.addAttribute("timecreateat", user.getCreateAt().format(formatter));
	            model.addAttribute("role", user.getRoleUser().getRole().name().equals("USER") ? "Người dùng tại Notenhanh.com" : "Admin");
	            model.addAttribute("status", user.getStatus().name() != null && user.getStatus().name().equals("ACTIVE") ? "Đang hoạt động" : "Vô hiệu hóa");
	            model.addAttribute("loginMethod", "Đăng nhập qua tài khoản Notenhanh");
	            model.addAttribute("authenticator", "Chưa bật phương thức Xác minh 2 bước");
	        }
	    } else if (principal != null) {
	        String provider = principal.getAuthorities().stream()
	            .map(GrantedAuthority::getAuthority)
	            .filter(authority -> authority.startsWith("ROLE_"))
	            .findFirst()
	            .map(authority -> authority.substring(5).toLowerCase())
	            .orElse("google");

	        String providerId = null;

	        if ("google".equals(provider)) {
	            providerId = principal.getAttribute("sub");
	        } else if ("facebook".equals(provider)) {
	            providerId = principal.getAttribute("id");
	        } else {
	            providerId = "unknown";
	        }

	        Optional<Users> userOptional = userRepository.findByProviderAndProviderId(provider, providerId);

	        Users user = userOptional.orElseThrow(() -> new UserNotFoundException("Không tìm thấy thông tin người dùng!"));

	            model.addAttribute("fullname", user.getFullname());
	            model.addAttribute("username", user.getProvider().equals("facebook") ? "Đăng nhập qua Facebook" : "Đăng nhập qua Google");
	            model.addAttribute("avatarphoto", user.getAvatarphoto());
	            model.addAttribute("email", user.getEmail());
	            model.addAttribute("totaltasknote", user.getTotalTasknote());
	            model.addAttribute("totaltextnote", user.getTotalTextnote());
	            model.addAttribute("totalnote", user.getTotalnote());
	            model.addAttribute("timecreateat", user.getCreateAt().format(formatter));
	            model.addAttribute("role", user.getRoleUser().getRole().name().equals("USER") ? "Người dùng tại Notenhanh.com" : "Admin");
	            model.addAttribute("status", user.getStatus().name() != null && user.getStatus().name().equals("ACTIVE") ? "Đang hoạt động" : "Vô hiệu hóa");
	            model.addAttribute("loginMethod", user.getProvider().equals("facebook") ? "Đăng nhập qua Facebook" : "Đăng nhập qua Google");
	            model.addAttribute("authenticator", "Chưa bật phương thức Xác minh 2 bước");
	        }
	    else {
	    	throw new UsernameNotFoundException("Không xác thực được người dùng");
	    }
	 }
}

