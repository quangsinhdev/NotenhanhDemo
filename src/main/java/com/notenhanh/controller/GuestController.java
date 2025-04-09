package com.notenhanh.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.notenhanh.domain.Tasknote;
import com.notenhanh.domain.Textnote;
import com.notenhanh.domain.dto.NoteDTO.UpdateTasknoteDTO;
import com.notenhanh.domain.dto.NoteDTO.UpdateTextnoteDTO;
import com.notenhanh.exception.note.TasknoteNotFoundException;
import com.notenhanh.exception.note.TextnoteNotFoundException;
import com.notenhanh.service.note.TasknoteService;
import com.notenhanh.service.note.TextnoteService;
import jakarta.validation.Valid;

@Controller
public class GuestController {
	 private final TextnoteService textnoteService;
	 private final TasknoteService tasknoteService;
	    
	 public GuestController(TextnoteService textnoteService, TasknoteService tasknoteService) {
			this.textnoteService = textnoteService;
			this.tasknoteService = tasknoteService;
		}


	 @GetMapping("/guest/view/textnote/{url}")
	 public String viewTextnoteByUrl(@PathVariable String url, Model model) {
	     Optional<Textnote> textnoteOptional = textnoteService.ViewTextnoteByUrl(url);
	     if (textnoteOptional.isPresent()) {
	         Textnote textnote = textnoteOptional.get();
	         model.addAttribute("textnote", textnote);
	         model.addAttribute("privacy", textnote.getPrivacy());
	         return "/guest/GuestViewTextnote";
	     }
	     throw new TextnoteNotFoundException(url);
	 }
	 
	 @GetMapping("/guest/view/tasknote/{url}")
	 public String viewTasknoteByUrl(@PathVariable String url, Model model) {
	     Optional<Tasknote> tasknoteOptional = tasknoteService.ViewTasknoteByUrl(url);
	     if (tasknoteOptional.isPresent()) {
	         Tasknote tasknote = tasknoteOptional.get();
	         model.addAttribute("tasknote", tasknote);
	         model.addAttribute("privacy", tasknote.getPrivacy());
	         return "/guest/GuestViewTasknote";
	     }
	     throw new TasknoteNotFoundException(url);
	 }
	 
	 @PostMapping("/guest/update/textnote/{url}")
	 public ResponseEntity<Map<String, Object>> updateTextnoteByUrl(@PathVariable String url,
	                                                               @RequestBody @Valid UpdateTextnoteDTO updateTextnoteDTO, 
	                                                               BindingResult bindingResult) {
	     Map<String, Object> response = new HashMap<>();
	     
	     if (bindingResult.hasErrors()) {
	         response.put("status", "error");
	         response.put("message", "Cập nhật ghi chú thất bại! Vui lòng kiểm tra và thử lại.");
	         return ResponseEntity.badRequest().body(response);
	     }

	     textnoteService.UpdateTextnoteByUrl(url, updateTextnoteDTO);

	     response.put("status", "success");
	     response.put("message", "Đã cập nhật ghi chú thành công!");
	     response.put("updatedTextnote", updateTextnoteDTO);
	     return ResponseEntity.ok(response);
	 }
	 
	 @PostMapping("/guest/update/tasknote/{url}")
	 public ResponseEntity<Map<String, Object>> updateTasknoteByUrl(@PathVariable String url,
	                                                               @RequestBody @Valid UpdateTasknoteDTO updateTasknoteDTO, 
	                                                               BindingResult bindingResult) {
	     Map<String, Object> response = new HashMap<>();
	     
	     if (bindingResult.hasErrors()) {
	         response.put("status", "error");
	         response.put("message", "Cập nhật ghi chú thất bại! Vui lòng kiểm tra và thử lại.");
	         return ResponseEntity.badRequest().body(response);
	     }

	     tasknoteService.UpdateTasknoteByUrl(url, updateTasknoteDTO);

	     response.put("status", "success");
	     response.put("message", "Đã cập nhật ghi chú thành công!");
	     response.put("updatedTasknote", updateTasknoteDTO);
	     return ResponseEntity.ok(response);
	 }
}
