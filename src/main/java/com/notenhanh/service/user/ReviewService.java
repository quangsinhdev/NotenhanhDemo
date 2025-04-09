package com.notenhanh.service.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.notenhanh.domain.Review;
import com.notenhanh.domain.Users;
import com.notenhanh.domain.dto.UserFeaturesDTO.ReviewDTO;
import com.notenhanh.enumation.Role;
import com.notenhanh.exception.review.ReviewNotFoundException;
import com.notenhanh.exception.userauthentication.UserNotFoundException;
import com.notenhanh.repository.ReviewRepository;

@Service
public class ReviewService {
	private final ReviewRepository reviewRepository;
	private final UserService userService;
	public ReviewService(ReviewRepository reviewRepository, UserService userService) {
		this.reviewRepository = reviewRepository;
		this.userService = userService;
	}
	
	public Review createFeedback(ReviewDTO reviewDTO) {
		Users user = getAuthenticatedUser();
		if(user == null) {
			throw new UserNotFoundException("Người dùng chưa đăng nhập");
		}
		Review review = new Review();
		review.setEmail(reviewDTO.getEmail());
		review.setFullName(reviewDTO.getFullName());
		review.setTitle(reviewDTO.getTitle());
		review.setContent(reviewDTO.getContent());
		review.setUser(user);
		return reviewRepository.save(review);
	}
	public List<Review> getReviewByUser(Users user){
		return reviewRepository.findByUser(user);
	}
	public Review ViewFeedBackById(Long reviewId) {
		Users user = getAuthenticatedUser();
		if(user == null) {
			throw new UserNotFoundException("Bạn chưa đăng nhập. Vui lòng đăng nhập và thử lại.");
		}
		else if(!CheckPermissionById(reviewId, user.getId()) && user.getRoleUser().getRole() != Role.ADMIN){
			throw new AccessDeniedException("Bạn không có quyền xem feedback này");
		}
		else {
			return reviewRepository.findById(reviewId)
		            .orElseThrow(() -> new ReviewNotFoundException("Không tìm thấy đánh giá với ID: " + reviewId));
		}
	}
	
	public List<Review> getAllFeedBackByUserId(){
		Users user = getAuthenticatedUser();
		if(user == null) {
			throw new UserNotFoundException("Bạn chưa đăng nhập. Vui lòng đăng nhập và thử lại.");
		}
			return reviewRepository.findByUser(user);
	}
	
	public Page<Review> getAllFeedBack(Pageable pageable){
		Users user = getAuthenticatedUser();
		if(user == null) {
			throw new UserNotFoundException("Bạn chưa đăng nhập. Vui lòng đăng nhập và thử lại.");
		}
		else if(user.getRoleUser().getRole() != Role.ADMIN) {
			throw new AccessDeniedException("Bạn không có quyền truy cập dữ liệu này");
		}
		else
			return reviewRepository.findAll(pageable);
	}
	
	public long getReviewTotal() {
		return reviewRepository.count();
	}
	
	private boolean CheckPermissionById(Long reviewId, Long userId) {
		Review review = reviewRepository.findById(reviewId)
		        .orElseThrow(() -> new ReviewNotFoundException("Không tìm thấy feedback"));
		if (!review.getUser().getId().equals(userId)) {
		    return false;
		}
		return true;
	}

	 private Users getAuthenticatedUser() {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        Users user = null;

	        if (authentication instanceof OAuth2AuthenticationToken) {
	            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
	            OAuth2User oauthUser = oauthToken.getPrincipal();
	            String providerId = (String) oauthUser.getAttributes().get("sub");
	            String provider = oauthToken.getAuthorizedClientRegistrationId();

	            Optional<Users> optionalUser = userService.getUserByProviderAndProviderId(provider, providerId);
	            if (optionalUser.isPresent()) {
	                user = optionalUser.get();
	            } else {
	                throw new UserNotFoundException("Người dùng chưa đăng nhập.");
	            }
	        } else if (authentication instanceof UsernamePasswordAuthenticationToken) {
	            String username = authentication.getName();
	            Optional<Users> optionalUser = userService.getUserByUsername(username);
	            if (optionalUser.isPresent()) {
	                user = optionalUser.get();
	            } else {
	                throw new UserNotFoundException("Người dùng chưa đăng nhập.");
	            }
	        }

	        return user;
	    }
}