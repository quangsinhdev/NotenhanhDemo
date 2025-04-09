package com.notenhanh.controller;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.notenhanh.domain.Review;
import com.notenhanh.domain.Tasknote;
import com.notenhanh.domain.Textnote;
import com.notenhanh.domain.Users;
import com.notenhanh.domain.dto.NoteDTO.TasknoteDTO;
import com.notenhanh.domain.dto.NoteDTO.TextnoteDTO;
import com.notenhanh.domain.dto.NoteDTO.UpdateTasknoteDTO;
import com.notenhanh.domain.dto.NoteDTO.UpdateTextnoteDTO;
import com.notenhanh.domain.dto.UserFeaturesDTO.ReviewDTO;
import com.notenhanh.enumation.Privacy;
import com.notenhanh.exception.note.TasknoteNotFoundException;
import com.notenhanh.exception.note.TextnoteNotFoundException;
import com.notenhanh.exception.userauthentication.UserNotFoundException;
import com.notenhanh.repository.UserRepository;
import com.notenhanh.service.note.TasknoteService;
import com.notenhanh.service.note.TextnoteService;
import com.notenhanh.service.user.ReviewService;

import jakarta.validation.Valid;

@Controller
public class MemberController {

    private UserRepository userRepository;
    private TextnoteService textnoteService;
    private TasknoteService tasknoteService;
    private ReviewService reviewService;
    public MemberController(UserRepository userRepository, TextnoteService textnoteService, TasknoteService tasknoteService, ReviewService reviewService) {
		this.userRepository = userRepository;
		this.textnoteService = textnoteService;
		this.tasknoteService = tasknoteService;
		this.reviewService = reviewService;
	}


    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");

        Users user = getAuthenticatedUser();

        if (user != null) {
            model.addAttribute("fullname", user.getFullname());
            model.addAttribute("avatarphoto", user.getAvatarphoto());
            model.addAttribute("email", user.getEmail());
            model.addAttribute("totaltasknote", user.getTotalTasknote());
            model.addAttribute("totaltextnote", user.getTotalTextnote());
            model.addAttribute("timecreateat", user.getCreateAt().format(formatter));
        }
        else {
           throw new UserNotFoundException("Không tìm thấy thông tin người dùng");
        }
        return "dashboard";
    }
    
    @GetMapping("/notestatistics")
    public String showNotes(@RequestParam(defaultValue = "desc") String order, 
                            @RequestParam(required = false) String title, 
                            @RequestParam(required = false) String privacy, 
                            @RequestParam(defaultValue = "textnote") String type, 
                            @RequestParam(defaultValue = "0") int page, 
                            @RequestParam(defaultValue = "15") int size,
                            Model model) {

        Users user = getAuthenticatedUser();

        if (user == null) {
            throw new UserNotFoundException("Không tìm thấy thông tin người dùng");
        }

        Privacy privacyEnum = null;
        if (privacy != null && !privacy.isEmpty()) {
            try {
                privacyEnum = Privacy.valueOf(privacy);
            } catch (IllegalArgumentException e) {
                privacyEnum = null;
            }
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(order.equals("desc") ? Sort.Order.desc("createdAt") : Sort.Order.asc("createdAt")));
        Page<Textnote> showPageTextnote = Page.empty();
        Page<Tasknote> showPageTasknote = Page.empty();

        if ("textnote".equals(type)) {
            if (title != null && !title.isEmpty() && privacyEnum != null) {
            	showPageTextnote = textnoteService.getTextnotesByTitleAndPrivacyAndUserId(order, title, privacyEnum, user.getId(), pageable);
            } else if (title != null && !title.isEmpty()) {
            	showPageTextnote = textnoteService.getTextnotesByTitleAndUserId(order, title, user.getId(), pageable);
            } else if (privacyEnum != null) {
            	showPageTextnote = textnoteService.getTextnotesByPrivacyAndUserId(order, privacyEnum, user.getId(), pageable);
            } else {
            	showPageTextnote = textnoteService.getTextnotesByUserId(order, user.getId(),pageable);
            }
            model.addAttribute("showListTextnote", showPageTextnote.getContent());
            model.addAttribute("totalPagesTextnote", showPageTextnote.getTotalPages());
            model.addAttribute("currentPageTextnote", showPageTextnote.getNumber() + 1);
            model.addAttribute("type", "textnote");
            model.addAttribute("order", order);
            model.addAttribute("privacy", privacy);
        }

        else if ("tasknote".equals(type)) {
        	if (title != null && !title.isEmpty() && privacyEnum != null) {
        		showPageTasknote = tasknoteService.getTasknotesByTitleAndPrivacyAndUserId(order, title, privacyEnum, user.getId(), pageable);
            } else if (title != null && !title.isEmpty()) {
            	showPageTasknote = tasknoteService.getTasknotesByTitleAndUserId(order, title, user.getId(), pageable);
            } else if (privacyEnum != null) {
            	showPageTasknote = tasknoteService.getTasknotesByPrivacyAndUserId(order, privacyEnum, user.getId(), pageable);
            } else {
            	showPageTasknote = tasknoteService.getTasknotesByUserId(order, user.getId(), pageable);
            }
            model.addAttribute("showListTasknote", showPageTasknote.getContent());
            model.addAttribute("totalPagesTasknote", showPageTasknote.getTotalPages());
            model.addAttribute("currentPageTasknote", showPageTasknote.getNumber() + 1);
            model.addAttribute("type", "tasknote");
            model.addAttribute("order", order);
            model.addAttribute("privacy", privacy);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

        showPageTextnote.getContent().forEach(notelist -> {
            if (notelist.getCreatedAt() != null) {
                notelist.setFormattedCreatedAt(notelist.getCreatedAt().format(formatter));
            } else {
                notelist.setFormattedCreatedAt("Chưa có thời gian");
            }

            if (notelist.getUpdatedAt() != null) {
                notelist.setFormattedUpdatedAt(notelist.getUpdatedAt().format(formatter));
            } else {
                notelist.setFormattedUpdatedAt("Chưa có thời gian");
            }
        });

        showPageTasknote.getContent().forEach(notelist -> {
            if (notelist.getCreatedAt() != null) {
                notelist.setFormattedCreatedAt(notelist.getCreatedAt().format(formatter));
            } else {
                notelist.setFormattedCreatedAt("Chưa có thời gian");
            }

            if (notelist.getUpdatedAt() != null) {
                notelist.setFormattedUpdatedAt(notelist.getUpdatedAt().format(formatter));
            } else {
                notelist.setFormattedUpdatedAt("Chưa có thời gian");
            }
        });

        model.addAttribute("user", user);
        return "NoteStatistics";
    }



    
    @GetMapping("/view/textnote/{id}")
    public String viewTextnoteById(@PathVariable Long id, Model model) {
    	Users user = getAuthenticatedUser();
        Optional<Textnote> textnoteOptional = textnoteService.ViewTextnoteById(id);
		Textnote textnote = textnoteOptional.orElseThrow(() -> new TextnoteNotFoundException(id));
			model.addAttribute("user", user);
            model.addAttribute("textnote", textnote);
            model.addAttribute("privacy", textnote.getPrivacy());
            return "viewtextnote";
    }
    
    @GetMapping("/view/tasknote/{id}")
    public String viewTasknoteById(@PathVariable Long id, Model model) {
    	Users user = getAuthenticatedUser();
        Optional<Tasknote> tasknoteOptional = tasknoteService.ViewTasknoteById(id);
		Tasknote tasknote = tasknoteOptional.orElseThrow(() -> new TasknoteNotFoundException(id));
			model.addAttribute("user", user);
            model.addAttribute("tasknote", tasknote);
            model.addAttribute("task",tasknote.getTask());
            model.addAttribute("privacy", tasknote.getPrivacy());
            return "viewtasknote";
    }

    @GetMapping("/feedback")
    public String WriteFeedback(Model model, RedirectAttributes redirectAttributes) {
    	Users user = getAuthenticatedUser();
		model.addAttribute("user", user);
        return "feedback";
    }
    	
    @PostMapping("/features/createtextnote")
    public String createTextnote(@ModelAttribute @Valid TextnoteDTO textnoteDTO, 
                                 BindingResult bindingResult, 
                                 Model model, 
                                 RedirectAttributes redirectAttributes, 
                                 @RequestParam(defaultValue = "desc") String order,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "15") int size) {

        Users user = getAuthenticatedUser();
        
        if (user == null) {
            throw new UserNotFoundException("Không tìm thấy thông tin người dùng");
        }
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("fieldErrors", "Validate failed: Tiêu đề hoặc nội dung không hợp lệ");
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi tạo ghi chú! Vui lòng kiểm tra lại.");
            return "redirect:/notestatistics";
        }

        Textnote createTextnote = textnoteService.createTextnote(textnoteDTO);

        if (createTextnote != null) {
            Pageable pageable = PageRequest.of(page, size, Sort.by(order.equals("desc") ? Sort.Order.desc("createdAt") : Sort.Order.asc("createdAt")));
            
            Page<Textnote> showPageTextnote = textnoteService.getTextnotesByUserId(order, user.getId(), pageable);
            
            model.addAttribute("showListTextnote", showPageTextnote.getContent());
            model.addAttribute("order", order);
            model.addAttribute("totalPagesTextnote", showPageTextnote.getTotalPages());
            
            redirectAttributes.addFlashAttribute("successMessage", "Ghi chú đã được tạo thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Tạo ghi chú thất bại!");
        }

        return "redirect:/notestatistics";
    }


    @PostMapping("/features/createtasknote")
    @ResponseBody
    public Map<String, Object> createTasknote(@RequestBody @Valid TasknoteDTO tasknoteDTO, BindingResult bindingResult, Model model) {
        Map<String, Object> response = new HashMap<>();
        
        Users user = getAuthenticatedUser();
        if (user == null) {
            throw new UserNotFoundException("Không tìm thấy thông tin người dùng");
        }

        if (bindingResult.hasErrors()) {
            List<String> errorMessages = new ArrayList<>();
            for (ObjectError error : bindingResult.getAllErrors()) {
                if (error instanceof FieldError) {
                    FieldError fieldError = (FieldError) error;
                    errorMessages.add("Field: " + fieldError.getField() + " - " + fieldError.getDefaultMessage());
                } else {
                    errorMessages.add(error.getDefaultMessage());
                }
            }
            
            model.addAttribute("fieldErrors", errorMessages);
            
            response.put("status", "error");
            response.put("message", "Tạo ghi chú thất bại! Vui lòng kiểm tra và thử lại.");
            response.put("errors", errorMessages);
            return response;
        }


        Tasknote createdTasknote = tasknoteService.createTasknote(tasknoteDTO);

        if (createdTasknote != null) {
        	response.put("status", "success");
            response.put("message", "Tạo ghi chú thành công. Chuyển hướng đến danh sách ghi chú Tasknote trong vài giây...");
            return response;
        } else {
        	response.put("status", "error");
            response.put("message", "Tạo ghi chú thất bại! Vui lòng thử lại");
            return response;
        }
    }


    @PostMapping("/update/textnote/{id}")
    public ResponseEntity<Map<String, Object>> updateTextnoteById(@PathVariable Long id,
                                                                  @RequestBody @Valid UpdateTextnoteDTO updateTextnoteDTO, 
                                                                  BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();
        
        if (bindingResult.hasErrors()) {
            response.put("status", "error");
            response.put("message", "Cập nhật ghi chú thất bại! Vui lòng kiểm tra và thử lại.");
            return ResponseEntity.badRequest().body(response);
        }

        textnoteService.UpdateTextnoteById(id, updateTextnoteDTO);

        response.put("status", "success");
        response.put("message", "Đã cập nhật ghi chú thành công!");
        response.put("updatedTextnote", updateTextnoteDTO);
        return ResponseEntity.ok(response);
    }

    
    @PostMapping("/update/tasknote/{id}")
    public ResponseEntity<Map<String, Object>> updateTasknoteById(@PathVariable Long id,
                                                                  @RequestBody @Valid UpdateTasknoteDTO updateTasknoteDTO, 
                                                                  BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();
        
        if (bindingResult.hasErrors()) {
            response.put("status", "error");
            response.put("message", "Cập nhật ghi chú thất bại! Vui lòng kiểm tra và thử lại.");
            return ResponseEntity.badRequest().body(response);
        }

        tasknoteService.UpdateTasknoteById(id, updateTasknoteDTO);

        response.put("status", "success");
        response.put("message", "Đã cập nhật ghi chú thành công!");
        response.put("updatedTasknote", updateTasknoteDTO);
        return ResponseEntity.ok(response);
    }
    
    @RequestMapping("/features/textnote/delete/{noteId}")
    public String deleteTextnoteById(@PathVariable Long noteId, RedirectAttributes redirectAttributes, Model model) {
    	
    	Users user = getAuthenticatedUser();
    
        if(user!=null) {
            textnoteService.deleteTextnoteById(noteId);
            redirectAttributes.addFlashAttribute("DeletesuccessMessage", "Ghi chú đã được xóa thành công!");
            return "redirect:/notestatistics";
        }
        throw new AccessDeniedException("Bạn không được phép sử dụng tính năng này.");
      }
    
    @RequestMapping("/features/tasknote/delete/{noteId}")
    public String deleteTasknoteById(@PathVariable Long noteId, RedirectAttributes redirectAttributes, Model model) {
    	
    	Users user = getAuthenticatedUser();
    
        if(user!=null) {
            tasknoteService.deleteTasknoteById(noteId);
            redirectAttributes.addFlashAttribute("DeletesuccessMessage", "Ghi chú đã được xóa thành công!");
            return "redirect:/notestatistics?type=tasknote";
        }
            throw new UserNotFoundException("Không tìm thấy thông tin người dùng");
        }
    
    @PostMapping("/features/createfeedback")
    public String createTextnote(@ModelAttribute @Valid ReviewDTO reviewDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        Review review = reviewService.createFeedback(reviewDTO);
        if (bindingResult.hasErrors()) {
           redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi gửi phản hồi! Vui lòng kiểm tra lại.");
            return "redirect:/feedback";
        }

      
        else if (review != null) {
            redirectAttributes.addFlashAttribute("successMessage", "Gửi phản hồi thành công! Chúng tôi sẽ liên hệ qua Email của bạn.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Gửi phản hồi thất bại");
        }

        return "redirect:/feedback";
    }
    
    
    private Users getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            if (authentication instanceof OAuth2AuthenticationToken) {
                OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
                OAuth2User oauthUser = oauthToken.getPrincipal();
                String providerId = (String) oauthUser.getAttributes().get("sub");
                String provider = oauthToken.getAuthorizedClientRegistrationId();

                Optional<Users> optionalUser = userRepository.findByProviderAndProviderId(provider, providerId);
                return optionalUser.orElse(null);
            } 
            else if (authentication instanceof UsernamePasswordAuthenticationToken) {
                String username = authentication.getName();
                Optional<Users> optionalUser = userRepository.findByUsername(username);
                return optionalUser.orElse(null);
            }
        }
        throw new UserNotFoundException("Không tìm thấy thông tin người dùng!");
    }

}