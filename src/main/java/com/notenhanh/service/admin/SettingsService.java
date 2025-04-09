package com.notenhanh.service.admin;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.notenhanh.domain.Settings;
import com.notenhanh.domain.Users;
import com.notenhanh.domain.dto.AdminDTO.SettingsDTO;
import com.notenhanh.enumation.Role;
import com.notenhanh.enumation.WebStatus;
import com.notenhanh.exception.userauthentication.UserNotFoundException;
import com.notenhanh.repository.SettingsRepository;
import com.notenhanh.repository.UserRepository;

import java.util.Optional;

@Service
public class SettingsService{

    private final SettingsRepository settingsRepository;
    private final UserRepository userRepository;
    
    public SettingsService(SettingsRepository settingsRepository, UserRepository userRepository) {
		this.settingsRepository = settingsRepository;
		this.userRepository = userRepository;
	}

    public Optional<Settings> findById(Long id) {
    	if(!checkPermissionAdmin()) {
    		throw new SecurityException("Bạn không có quyền truy cập tính năng này");
    	}
        return settingsRepository.findById(id);
    }
    
    public Settings WebSettings(Long id, SettingsDTO settingsDTO) {
    	if(!checkPermissionAdmin()) {
    		throw new SecurityException("Bạn không có quyền truy cập tính năng này");
    	}
        Settings settings = settingsRepository.findById(id).orElseThrow(() -> new RuntimeException("Đã xảy ra lỗi khi truy xuất thông tin"));
        settings.setWebtitle(settingsDTO.getWebtitle());
        settings.setDescription(settingsDTO.getDescription());
        settings.setContactEmail(settingsDTO.getContactemail());
        settings.setHomepagenotice(settingsDTO.getHomepagenotice());
        settings.setKeywords(settingsDTO.getKeywords());
        settings.setPhonenumber(settingsDTO.getPhonenumber());
        settings.setPolicy(settings.getPolicy());
        return settingsRepository.save(settings);
    }
    

    public Settings updateTitle(Long id, String title) {
    	if(!checkPermissionAdmin()) {
    		throw new SecurityException("Bạn không có quyền truy cập tính năng này");
    	}
        Settings settings = settingsRepository.findById(id).orElseThrow(() -> new RuntimeException("Đã xảy ra lỗi khi truy xuất thông tin"));
        settings.setWebtitle(title);
        return settingsRepository.save(settings);
    }

    public Settings updateWebStatus(Long id, WebStatus webStatus) {
    	if(!checkPermissionAdmin()) {
    		throw new SecurityException("Bạn không có quyền truy cập tính năng này");
    	}
        Settings settings = settingsRepository.findById(id).orElseThrow(() -> new RuntimeException("Đã xảy ra lỗi khi truy xuất thông tin"));
        settings.setWebstatus(webStatus);
        return settingsRepository.save(settings);
    }

    public Settings updatePolicyLink(Long id, String policyLink) {
    	if(!checkPermissionAdmin()) {
    		throw new SecurityException("Bạn không có quyền truy cập tính năng này");
    	}
        Settings settings = settingsRepository.findById(id).orElseThrow(() -> new RuntimeException("Đã xảy ra lỗi khi truy xuất thông tin"));
        settings.setPolicy(policyLink);
        return settingsRepository.save(settings);
    }

    public Settings updatePhoneNumber(Long id, String phoneNumber) {
    	if(!checkPermissionAdmin()) {
    		throw new SecurityException("Bạn không có quyền truy cập tính năng này");
    	}
        Settings settings = settingsRepository.findById(id).orElseThrow(() -> new RuntimeException("Đã xảy ra lỗi khi truy xuất thông tin"));
        settings.setPhonenumber(phoneNumber);
        return settingsRepository.save(settings);
    }

    public Settings updateMetaKeywords(Long id, String keywords) {
    	if(!checkPermissionAdmin()) {
    		throw new SecurityException("Bạn không có quyền truy cập tính năng này");
    	}
        Settings settings = settingsRepository.findById(id).orElseThrow(() -> new RuntimeException("Đã xảy ra lỗi khi truy xuất thông tin"));
        settings.setKeywords(keywords);
        return settingsRepository.save(settings);
    }

    public Settings updateMetaDescription(Long id, String metaDescription) {
    	if(!checkPermissionAdmin()) {
    		throw new SecurityException("Bạn không có quyền truy cập tính năng này");
    	}
        Settings settings = settingsRepository.findById(id).orElseThrow(() -> new RuntimeException("Đã xảy ra lỗi khi truy xuất thông tin"));
        settings.setDescription(metaDescription);
        return settingsRepository.save(settings);
    }

    public Settings updateHomepageNotice(Long id, String homepageNotice) {
    	if(!checkPermissionAdmin()) {
    		throw new SecurityException("Bạn không có quyền truy cập tính năng này");
    	}
        Settings settings = settingsRepository.findById(id).orElseThrow(() -> new RuntimeException("Đã xảy ra lỗi khi truy xuất thông tin"));
        settings.setHomepagenotice(homepageNotice);
        return settingsRepository.save(settings);
    }

    public Settings updateContactEmail(Long id, String contactEmail) {
    	if(!checkPermissionAdmin()) {
    		throw new SecurityException("Bạn không có quyền truy cập tính năng này");
    	}
        Settings settings = settingsRepository.findById(id).orElseThrow(() -> new RuntimeException("Đã xảy ra lỗi khi truy xuất thông tin"));
        settings.setContactEmail(contactEmail);
        return settingsRepository.save(settings);
    }
    
    private boolean checkPermissionAdmin() {
	    Users user = getAuthenticatedUser();
	    if(user==null) {
	    	throw new UserNotFoundException("Bạn chưa đăng nhập. Vui lòng đăng nhập và thử lại.");
	    }
	    return user.getRoleUser().getRole() == Role.ADMIN;
	}
	
	 private Users getAuthenticatedUser() {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        Users user = null;

	        if (authentication instanceof OAuth2AuthenticationToken) {
	            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
	            OAuth2User oauthUser = oauthToken.getPrincipal();
	            String providerId = (String) oauthUser.getAttributes().get("sub");
	            String provider = oauthToken.getAuthorizedClientRegistrationId();

	            Optional<Users> optionalUser = userRepository.findByProviderAndProviderId(provider, providerId);
	            if (optionalUser.isPresent()) {
	                user = optionalUser.get();
	            } else {
	                throw new UserNotFoundException("Người dùng chưa đăng nhập.");
	            }
	        } else if (authentication instanceof UsernamePasswordAuthenticationToken) {
	            String username = authentication.getName();
	            Optional<Users> optionalUser = userRepository.findByUsername(username);
	            if (optionalUser.isPresent()) {
	                user = optionalUser.get();
	            } else {
	                throw new UserNotFoundException("Người dùng chưa đăng nhập.");
	            }
	        }

	        return user;
	    }
}
