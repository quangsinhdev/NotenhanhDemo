package com.notenhanh.service.note;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.notenhanh.domain.Task;
import com.notenhanh.domain.Tasknote;
import com.notenhanh.domain.Users;
import com.notenhanh.domain.dto.NoteDTO.TaskDTO;
import com.notenhanh.domain.dto.NoteDTO.TasknoteDTO;
import com.notenhanh.domain.dto.NoteDTO.UpdateTasknoteDTO;
import com.notenhanh.enumation.Privacy;
import com.notenhanh.exception.note.AccessDeniedUpdateTasknoteIDException;
import com.notenhanh.exception.note.AccessDeniedUpdateTasknoteURLException;
import com.notenhanh.exception.note.PrivacyAccessDeniedException;
import com.notenhanh.exception.note.TasknoteNotFoundException;
import com.notenhanh.exception.userauthentication.UserNotFoundException;
import com.notenhanh.repository.TaskRepository;
import com.notenhanh.repository.TasknoteRepository;
import com.notenhanh.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
@Service
public class TasknoteService {
	private final TasknoteRepository tasknoteRepository;
	private final UserRepository userRepository;
	private final TaskRepository taskRepository;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	public TasknoteService(TasknoteRepository tasknoteRepository, UserRepository userRepository, TaskRepository taskRepository) {
		this.tasknoteRepository = tasknoteRepository;
		this.userRepository = userRepository;
		this.taskRepository = taskRepository;
	}
	 
	public Tasknote createTasknote(TasknoteDTO tasknoteDTO) {
	    Users user = getAuthenticatedUser();
	    
	    if (user == null) {
	        throw new UserNotFoundException("Bạn chưa đăng nhập. Vui lòng đăng nhập để sử dụng chức năng này");
	    }
	    
	    long currentTotalTasknote = user.getTotalTasknote();
	    user.setTotalTasknote(currentTotalTasknote + 1);
	    
	    Tasknote tasknote = new Tasknote();
	    List<Task> taskList = new ArrayList<>();
	    
	    for (TaskDTO taskDTO : tasknoteDTO.getTasks()) {
	        Task task = new Task();
	        task.setContent(taskDTO.getContent());
	        task.setTaskStatus(taskDTO.getTaskStatus());
	        task.setTaskNote(tasknote);
	        taskList.add(task);
	    }
	    
	    if (tasknoteDTO.getPrivacy() == Privacy.PUBLIC) {
	    	String generatedUrl;
    		do {
    			generatedUrl = generateRandomURL();
    		} while (tasknoteRepository.existsByUrl(generatedUrl));
    	tasknote.setUrl(generatedUrl);
    }
	    
	    tasknote.setTitle(tasknoteDTO.getTitle());
	    tasknote.setType(tasknoteDTO.getType());
	    tasknote.setTask(taskList);
	    tasknote.setNumbertask(tasknoteDTO.getNumbertask());
	    tasknote.setPrivacy(tasknoteDTO.getPrivacy());
	    tasknote.setCreatedAt(LocalDateTime.now());
	    tasknote.setUpdatedAt(LocalDateTime.now());
	    tasknote.setUser(user);
	    
	    return tasknoteRepository.save(tasknote);
	}

	 public Page<Tasknote> getAllTasknote(Pageable pageable){
			return tasknoteRepository.findAll(pageable);
		}
	 public Page<Tasknote> getTasknoteByUser(Users user, Pageable pageable){
			return tasknoteRepository.findByUser(user, pageable);
		}
	 
	 public Page<Tasknote> getTasknotesByUserId(String order, Long userId, Pageable pageable) {
		    Users user = getAuthenticatedUser();
		    
		    if (user != null) {
		        if (!user.getId().equals(userId)) {
		            throw new AccessDeniedException("Bạn không có quyền truy cập vào những ghi chú này.");
		        }
		        Sort sort = order.equalsIgnoreCase("desc") ? 
		                    Sort.by(Sort.Order.desc("createdAt")) : 
		                    Sort.by(Sort.Order.asc("createdAt"));

		        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		        return tasknoteRepository.findByUserId(userId, sortedPageable);
		    }
		    throw new UserNotFoundException("Bạn chưa đăng nhập. Vui lòng đăng nhập và thử lại!");
		}



	  public Page<Tasknote> getTasknotesByTitleAndUserId(String order, String title, Long userId, Pageable pageable) {
		    Users user = getAuthenticatedUser();
		    
		    if (user != null) {
		        if (!user.getId().equals(userId)) {
		            throw new AccessDeniedException("Bạn không có quyền truy cập vào những ghi chú này.");
		        }

		        Sort sort = order.equalsIgnoreCase("desc") ? 
		                    Sort.by(Sort.Order.desc("createdAt")) : 
		                    Sort.by(Sort.Order.asc("createdAt"));

		        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		        return tasknoteRepository.findByTitleAndUser_Id(title, userId, sortedPageable);
		    }
		    throw new UserNotFoundException("Bạn chưa đăng nhập. Vui lòng đăng nhập và thử lại!");
		}

	
	
	  public Page<Tasknote> getTasknotesByPrivacyAndUserId(String order, Privacy privacy, Long userId, Pageable pageable) {
		    Users user = getAuthenticatedUser();
		    if (user != null) {
		        if (!user.getId().equals(userId)) {
		            throw new AccessDeniedException("Bạn không có quyền truy cập vào những ghi chú này.");
		        }
		        Sort sort = order.equalsIgnoreCase("desc") ? 
		                    Sort.by(Sort.Order.desc("createdAt")) : 
		                    Sort.by(Sort.Order.asc("createdAt"));
		        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

		        return tasknoteRepository.findByPrivacyAndUser_Id(privacy, userId, sortedPageable);
		    }
		    throw new UserNotFoundException("Bạn chưa đăng nhập. Vui lòng đăng nhập và thử lại!");
		}

	  
	  public Page<Tasknote> getTasknotesByTitleAndPrivacyAndUserId(String order, String title, Privacy privacy, Long userId, Pageable pageable) {
		    Users user = getAuthenticatedUser();
		    
		    if (user != null) {
		        if (!user.getId().equals(userId)) {
		            throw new AccessDeniedException("Bạn không có quyền truy cập vào những ghi chú này.");
		        }
		        Sort sort = order.equalsIgnoreCase("desc") ? 
		                    Sort.by(Sort.Order.desc("createdAt")) : 
		                    Sort.by(Sort.Order.asc("createdAt"));
		        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

		        if (title != null && !title.isEmpty() && privacy != null) {
		            return tasknoteRepository.findByTitleAndPrivacyAndUser_Id(title, privacy, userId, sortedPageable);
		        }

		        if (title != null && !title.isEmpty()) {
		            return tasknoteRepository.findByTitleAndUser_Id(title, userId, sortedPageable);
		        }

		        if (privacy != null) {
		            return tasknoteRepository.findByPrivacyAndUser_Id(privacy, userId, sortedPageable);
		        }
		    }
		    throw new UserNotFoundException("Bạn chưa đăng nhập. Vui lòng đăng nhập và thử lại!");
		}


	  public Optional<Tasknote> ViewTasknoteById(Long id) {
	        Users user = getAuthenticatedUser();
	        if(user!=null) {

	        Optional<Tasknote> tasknoteOptional = tasknoteRepository.findById(id);
	        if (!tasknoteOptional.isPresent()) {
	            throw new TasknoteNotFoundException(id);
	        }


	        if (!CheckPermissionById(id, user.getId())) {
	            throw new AccessDeniedException("Bạn không có quyền truy cập vào ghi chú này.");
	        }

	        return tasknoteOptional;
	    }
	        throw new UserNotFoundException("Bạn chưa đăng nhập. Vui lòng đăng nhập và thử lại!");
	  }
	  
	  public Optional<Tasknote> ViewTasknoteByUrl(String url) throws PrivacyAccessDeniedException, TasknoteNotFoundException {
		    Users user = getAuthenticatedUser();

		    Optional<Tasknote> tasknoteOptional = tasknoteRepository.findByUrl(url);
		    Tasknote tasknote = tasknoteOptional.orElseThrow(() -> new TasknoteNotFoundException("Tasknote không tồn tại."));
		    
		    if (user == null) {
		        if (tasknote.getPrivacy() != Privacy.PUBLIC) {
		            throw new PrivacyAccessDeniedException("Bạn không có quyền truy cập vào ghi chú này.");
		        }
		    } else {
		        if (tasknote.getPrivacy() != Privacy.PUBLIC && !tasknote.getUser().getId().equals(user.getId())) {
		            throw new PrivacyAccessDeniedException("Bạn không có quyền truy cập vào ghi chú này.");
		        }
		    }
		    return tasknoteOptional;
		}

	
	  @Transactional
	  public Tasknote UpdateTasknoteById(Long id, UpdateTasknoteDTO updateTasknoteDTO) throws AccessDeniedUpdateTasknoteIDException, TasknoteNotFoundException{
		    Users user = getAuthenticatedUser();
		    
		    Optional<Tasknote> tasknoteOptional = tasknoteRepository.findById(id);
		    Tasknote tasknote = tasknoteOptional.orElseThrow(() -> new TasknoteNotFoundException(id));
		    
		    taskRepository.deleteAllByTaskNote(tasknote);

		    List<Task> TaskList = new ArrayList<>();
			 for(TaskDTO updatetaskDTO : updateTasknoteDTO.getTasks()) {
				 Task task = new Task();
				 task.setContent(updatetaskDTO.getContent());
				 task.setTaskStatus(updatetaskDTO.getTaskStatus());
				 task.setTaskNote(tasknote);
				 TaskList.add(task);
			 }
			 
		    if(user!=null) {
		    	if (tasknote!=null) {

		    		if (CheckPermissionById(id, user.getId())) {
		        	
		    			if (updateTasknoteDTO.getPrivacy() == Privacy.PUBLIC && tasknote.getUrl()==null) {
		                String generatedUrl;
		                do {
		                    generatedUrl = generateRandomURL();
		                } while (tasknoteRepository.existsByUrl(generatedUrl));
		                updateTasknoteDTO.setUrl(generatedUrl);
		                tasknote.setUrl(updateTasknoteDTO.getUrl());
		            }

		    		tasknote.setTitle(updateTasknoteDTO.getTitle());
		    		tasknote.setTask(TaskList);
		    		tasknote.setPrivacy(updateTasknoteDTO.getPrivacy());
		    		tasknote.setUpdatedAt(LocalDateTime.now());
		            return tasknoteRepository.save(tasknote);
		        }
		        throw new AccessDeniedUpdateTasknoteIDException("Bạn không được phép sử dụng tính năng này.");
		    }
		    throw new TasknoteNotFoundException("Tasknote không tồn tại.");
		}
	        throw new UserNotFoundException("Bạn chưa đăng nhập. Vui lòng đăng nhập và thử lại!");
	  }
	  @Transactional
	  public Tasknote UpdateTasknoteByUrl(String url, UpdateTasknoteDTO updateTasknoteDTO) throws AccessDeniedUpdateTasknoteURLException, TasknoteNotFoundException {
		    Users user = getAuthenticatedUser();
		    
		    Optional<Tasknote> tasknoteOptional = tasknoteRepository.findByUrl(url);
		    Tasknote tasknote = tasknoteOptional.orElseThrow(() -> new TasknoteNotFoundException(url));
		    
		    taskRepository.deleteAllByTaskNote(tasknote);

		    List<Task> TaskList = new ArrayList<>();
		    for (TaskDTO updatetaskDTO : updateTasknoteDTO.getTasks()) {
		        Task task = new Task();
		        task.setContent(updatetaskDTO.getContent());
		        task.setTaskStatus(updatetaskDTO.getTaskStatus());
		        task.setTaskNote(tasknote);
		        TaskList.add(task);
		    }
		   if(user!=null) {
			   if(CheckPermissionByUrl(url, user.getId())) {
				   tasknote.setTitle(updateTasknoteDTO.getTitle());
				   tasknote.setTask(TaskList);
				   tasknote.setUpdatedAt(LocalDateTime.now());
		            return tasknoteRepository.save(tasknote);
			  }
			   throw new AccessDeniedUpdateTasknoteURLException("Bạn không được phép cập nhật dữ liệu này");
		   }
		   else if (tasknote.getPrivacy() == Privacy.PUBLIC && user==null) {
			   	tasknote.setTitle(updateTasknoteDTO.getTitle());
	    		tasknote.setTask(TaskList);
	    		tasknote.setUpdatedAt(LocalDateTime.now());
	            return tasknoteRepository.save(tasknote);
		   	}
		   else
		   throw new AccessDeniedUpdateTasknoteURLException("Bạn không được phép cập nhật dữ liệu này");
	  }


	
	 public void deleteTasknoteById(Long id) {
	        Users user = getAuthenticatedUser();
	        if(user!=null) {
	        	Optional<Tasknote> tasknote = tasknoteRepository.findById(id);
	        	if (!tasknote.isPresent()) {
	            throw new TasknoteNotFoundException("Không tìm thấy ghi chú cần xóa.");
	        	}

	        	if (!CheckPermissionById(id, user.getId())) {
	            throw new AccessDeniedException("Bạn không có quyền xóa ghi chú này.");
	        	}

	        	tasknoteRepository.deleteById(id);
	    }
	        else
		        throw new UserNotFoundException("Bạn chưa đăng nhập. Vui lòng đăng nhập và thử lại!");
	 }
	 
		public long getCountTasknote() {
			return tasknoteRepository.count();
		}
	 
	 private boolean CheckPermissionById(Long tasknoteId, Long userId) {
		Optional<Tasknote> tasknoteOptional = tasknoteRepository.findById(tasknoteId);
		Tasknote tasknote = tasknoteOptional.orElseThrow(() -> new TasknoteNotFoundException(tasknoteId));
			if(!tasknote.getUser().getId().equals(userId)) {
				return false;
			}
            return true;
	}
	
	private boolean CheckPermissionByUrl(String url, Long userId) {
	    Optional<Tasknote> tasknoteOptional = tasknoteRepository.findByUrl(url);
		Tasknote tasknote = tasknoteOptional.orElseThrow(() -> new TasknoteNotFoundException(url));
	        if (tasknote.getUser() == null) {
	            throw new TasknoteNotFoundException("Thông tin ghi chú không hợp lệ.");
	        }
	        if (tasknote.getPrivacy() == Privacy.PUBLIC || tasknote.getUser().getId().equals(userId)) {
	            return true;
	        }
	        return false;
	}

	
	private static String generateRandomURL() {
			SecureRandom random = new SecureRandom();
	        int length = random.nextInt(4) + 13;

	        StringBuilder sb = new StringBuilder(length);
	        for (int i = 0; i < length; i++) {
	            int index = random.nextInt(CHARACTERS.length());
	            sb.append(CHARACTERS.charAt(index));
	        }

	        return sb.toString();
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