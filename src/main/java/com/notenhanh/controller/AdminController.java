package com.notenhanh.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.notenhanh.domain.Review;
import com.notenhanh.domain.Settings;
import com.notenhanh.domain.Users;
import com.notenhanh.domain.dto.AdminDTO.SettingsDTO;
import com.notenhanh.enumation.Role;
import com.notenhanh.enumation.UserStatus;
import com.notenhanh.exception.review.ReviewNotFoundException;
import com.notenhanh.exception.settings.SettingWebException;
import com.notenhanh.service.admin.AdminService;
import com.notenhanh.service.admin.SettingsService;
import com.notenhanh.service.note.TasknoteService;
import com.notenhanh.service.note.TextnoteService;
import com.notenhanh.service.user.ReviewService;
import com.notenhanh.service.user.UserService;

import jakarta.validation.Valid;

@Controller
public class AdminController {
	private final AdminService adminService;
	private final UserService userService;
	private final TextnoteService textnoteService;
	private final TasknoteService tasknoteService;
	private final SettingsService settingsService;
	private final ReviewService reviewService;
	public AdminController(AdminService adminService, UserService userService, TextnoteService textnoteService,
			TasknoteService tasknoteService, SettingsService settingsService, ReviewService reviewService) {
		this.adminService = adminService;
		this.userService = userService;
		this.textnoteService = textnoteService;
		this.tasknoteService = tasknoteService;
		this.settingsService = settingsService;
		this.reviewService = reviewService;
	}

	@GetMapping("/admin")
	public String PanelAdmin(Model model) {
	long usersTotal = userService.getCountUsers();
	long textnoteTotal = textnoteService.getCountTextnote();
	long tasknoteTotal = tasknoteService.getCountTasknote();
	List<Users> Admin = adminService.getUsersByRole(Role.ADMIN);
	model.addAttribute("usersTotal", usersTotal);
	model.addAttribute("textnoteTotal", textnoteTotal);
	model.addAttribute("tasknoteTotal", tasknoteTotal);
	model.addAttribute("admin", Admin);
		return "/admin/Paneladmin";
	}
	
	@GetMapping("/admin/users")
	public String getUsersStatistics(@RequestParam(defaultValue = "0") int page, 
	                                 @RequestParam(defaultValue = "15") int size, 
	                                 Model model) {
	    Pageable pageable = PageRequest.of(page, size);
	    Page<Users> users = adminService.getAllUsers(pageable);
	    
	    long usersTotal = userService.getCountUsers();
	    long usersActiveTotal = userService.getCountUserFollowStatus(UserStatus.ACTIVE);
	    long usersDisabledTotal = userService.getCountUserFollowStatus(UserStatus.DISABLED);
	    int currentPage = users.getPageable().getPageNumber();
	    int totalPages = users.getTotalPages();
	    
	    model.addAttribute("usersTotal", usersTotal);
	    model.addAttribute("usersActiveTotal", usersActiveTotal);
	    model.addAttribute("usersDisabledTotal", usersDisabledTotal);
	    model.addAttribute("users", users);
	    
	    model.addAttribute("currentPageUserAdmin", currentPage);
	    model.addAttribute("totalPageUserAdmin", totalPages);
	    return "/admin/UserStatistics";
	}
	@GetMapping("/admin/notes")
	public String getNotesStatistics(Model model) {		
		long textnoteTotal = textnoteService.getCountTextnote();
		long tasknoteTotal = tasknoteService.getCountTasknote();
		long noteTotal = textnoteTotal + tasknoteTotal;
		
		model.addAttribute("textnoteTotal", textnoteTotal);
		model.addAttribute("tasknoteTotal", tasknoteTotal);
		model.addAttribute("noteTotal", noteTotal);
		return "/admin/Notes";
	}
	
	@GetMapping("/admin/settings")
	public String getSettings(Model model) {
	    Settings settings = settingsService.findById(1L)
	                                      .orElseThrow(() -> new RuntimeException("Đã xảy ra lỗi khi truy xuất thông tin"));
	    model.addAttribute("settings", settings);
	    return "/admin/WebSettings";
	}
	
	@GetMapping("/admin/feedback")
	public String getFeedback(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size, Model model) {
	    Pageable pageable = PageRequest.of(page, size);
	    Page<Review> feedbacks = adminService.getAllFeedback(pageable);      
	    
		long usersTotal = userService.getCountUsers();
		long noteTotal = tasknoteService.getCountTasknote() + tasknoteService.getCountTasknote();
		long reviewTotal = reviewService.getReviewTotal();
		int currentPage = feedbacks.getPageable().getPageNumber();
		int totalPages = feedbacks.getTotalPages();
		
		model.addAttribute("usersTotal", usersTotal);
	    model.addAttribute("noteTotal", noteTotal);
	    model.addAttribute("reviewTotal", reviewTotal);
	    model.addAttribute("AllFeedback", feedbacks);
	    
	    model.addAttribute("currentPageReviewsAdmin", currentPage);
	    model.addAttribute("totalPageReviewsAdmin", totalPages);
		return "/admin/Feedback";
	}
	
	@GetMapping("/admin/feedback/feedbackdetails/{id}")
	public String FeedbackDetails(@PathVariable Long id, Model model) {
		Review feedback = adminService.ViewFeedBackById(id);
		if(feedback == null) {
			throw new ReviewNotFoundException("Không tìm thấy đánh giá");
		}
	    long usersTotal = userService.getCountUsers();
		long noteTotal = tasknoteService.getCountTasknote() + tasknoteService.getCountTasknote();
		long reviewTotal = reviewService.getReviewTotal();
		
		model.addAttribute("usersTotal", usersTotal);
	    model.addAttribute("noteTotal", noteTotal);
	    model.addAttribute("reviewTotal", reviewTotal);
	    model.addAttribute("feedback", feedback);
	    
		return "/admin/feedback/FeedbackDetails";
	}

	
	@PostMapping("/admin/users/updaterole")
	public String updateUserRole(Long userId, Role role, RedirectAttributes redirectAttributes) {
	    adminService.updateUserRole(userId, role);
	    redirectAttributes.addFlashAttribute("message", "Cập nhật vai trò thành công!");
	    return "redirect:/admin/users";
	}


	 @PostMapping("/admin/users/delete")
	 	public String deleteUser(Long userId, RedirectAttributes redirectAttributes) {
	            adminService.deleteUser(userId);
	            redirectAttributes.addFlashAttribute("message", "Xóa người dùng thành công!");
	        return "redirect:/admin/users";
	    }
	 @PostMapping("/admin/users/changeStatus")
	 public String changeUserStatus(Long userId, UserStatus status, RedirectAttributes redirectAttributes) {
	     userService.updateStatusUser(userId, status);
	     return "redirect:/admin/users";
	 }
	 
	 @PostMapping("/admin/users/websettings")
	 public String WebSettings(@ModelAttribute @Valid SettingsDTO settingsDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		 if (bindingResult.hasErrors()) {
			throw new SettingWebException("Cập nhật thông tin web thất bại");
		 }
		 settingsService.WebSettings(1L, settingsDTO);
	     redirectAttributes.addFlashAttribute("successMessage","Cập nhật thông tin trang web thành công!");
	     return "redirect:/admin/settings";

	 }
}