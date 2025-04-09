package com.notenhanh.service.note;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.notenhanh.domain.Textnote;
import com.notenhanh.domain.Users;
import com.notenhanh.domain.dto.NoteDTO.TextnoteDTO;
import com.notenhanh.domain.dto.NoteDTO.UpdateTextnoteDTO;
import com.notenhanh.enumation.Privacy;
import com.notenhanh.exception.note.AccessDeniedUpdateTextnoteIDException;
import com.notenhanh.exception.note.AccessDeniedUpdateTextnoteURLException;
import com.notenhanh.exception.note.PrivacyAccessDeniedException;
import com.notenhanh.exception.note.TextnoteNotFoundException;
import com.notenhanh.exception.userauthentication.UserNotFoundException;
import com.notenhanh.repository.TextnoteRepository;
import com.notenhanh.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
@Service
public class TextnoteService {
	private final TextnoteRepository textnoteRepository;
	private final UserRepository userRepository;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	public TextnoteService(TextnoteRepository textnoteRepository, UserRepository userRepository) {
		this.textnoteRepository = textnoteRepository;
		this.userRepository = userRepository;
	}
	 
	public Textnote createTextnote(TextnoteDTO textnoteDTO) {
	    Users user = getAuthenticatedUser();
	    if (user == null) {
	        throw new UserNotFoundException("Bạn chưa đăng nhập. Vui lòng đăng nhập để sử dụng chức năng này");
	    }
	    
	    long currentTotalTextnote = user.getTotalTextnote();
	    user.setTotalTextnote(currentTotalTextnote + 1);

	    Textnote textnote = new Textnote();

	    if (textnoteDTO.getPrivacy() == Privacy.PUBLIC) {
	        String generatedUrl;
	        do {
	            generatedUrl = generateRandomURL();
	        } while (textnoteRepository.existsByUrl(generatedUrl));
	        textnote.setUrl(generatedUrl);
	    }

	    textnote.setTitle(textnoteDTO.getTitle());
	    textnote.setType(textnoteDTO.getType());
	    textnote.setContent(textnoteDTO.getContent());
	    textnote.setPrivacy(textnoteDTO.getPrivacy());
	    textnote.setCreatedAt(LocalDateTime.now());
	    textnote.setUpdatedAt(LocalDateTime.now());
	    textnote.setUser(user);

	    return textnoteRepository.save(textnote);
	}
	public Page<Textnote> getAllTextnote(Pageable pageable){
		return textnoteRepository.findAll(pageable);
	}
	public Page<Textnote> getTextnoteByUser(Users user, Pageable pageable){
		return textnoteRepository.findByUser(user, pageable);
	}
	public Page<Textnote> getTextnotesByUserId(String order, Long userId, Pageable pageable) {
	    Users user = getAuthenticatedUser();
	    if (user != null) {
	        if (!user.getId().equals(userId)) {
	            throw new AccessDeniedException("Bạn không có quyền truy cập vào những ghi chú này.");
	        }
	        
	        Sort sort = order.equalsIgnoreCase("desc") ? 
	                    Sort.by(Sort.Order.desc("createdAt")) : 
	                    Sort.by(Sort.Order.asc("createdAt"));
	        
	        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
	        
	        return textnoteRepository.findByUserId(userId, sortedPageable);
	    }
	    throw new UserNotFoundException("Bạn chưa đăng nhập. Vui lòng đăng nhập và thử lại!");
	}



	public Page<Textnote> getTextnotesByTitleAndUserId(String order, String title, Long userId, Pageable pageable) {
	    Users user = getAuthenticatedUser();
	    if (user != null) {
	        if (!user.getId().equals(userId)) {
	            throw new AccessDeniedException("Bạn không có quyền truy cập vào những ghi chú này.");
	        }

	        Sort sort = order.equalsIgnoreCase("desc") ? 
	                    Sort.by(Sort.Order.desc("createdAt")) : 
	                    Sort.by(Sort.Order.asc("createdAt"));

	        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
	        return textnoteRepository.findByTitleAndUser_Id(title, userId, sortedPageable);
	    }
	    throw new UserNotFoundException("Bạn chưa đăng nhập. Vui lòng đăng nhập và thử lại!");
	}
	  
	
	public Page<Textnote> getTextnotesByPrivacyAndUserId(String order, Privacy privacy, Long userId, Pageable pageable) {
	    Users user = getAuthenticatedUser();
	    
	    if (user != null) {
	        if (!user.getId().equals(userId)) {
	            throw new AccessDeniedException("Bạn không có quyền truy cập vào những ghi chú này.");
	        }
	        Sort sort = order.equalsIgnoreCase("desc") ? 
	                    Sort.by(Sort.Order.desc("createdAt")) : 
	                    Sort.by(Sort.Order.asc("createdAt"));

	        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
	        return textnoteRepository.findByPrivacyAndUser_Id(privacy, userId, sortedPageable);
	    }

	    throw new UserNotFoundException("Bạn chưa đăng nhập. Vui lòng đăng nhập và thử lại!");
	}

	  
	  public Page<Textnote> getTextnotesByTitleAndPrivacyAndUserId(String order, String title, Privacy privacy, Long userId, Pageable pageable) {
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
		            return textnoteRepository.findByTitleAndPrivacyAndUser_Id(title, privacy, userId, sortedPageable);
		        }
		        if (title != null && !title.isEmpty()) {
		            return textnoteRepository.findByTitleAndUser_Id(title, userId, sortedPageable);
		        }
		        if (privacy != null) {
		            return textnoteRepository.findByPrivacyAndUser_Id(privacy, userId, sortedPageable);
		        }
		    }

		    throw new UserNotFoundException("Bạn chưa đăng nhập. Vui lòng đăng nhập và thử lại!");
		}
	  


	  public Optional<Textnote> ViewTextnoteById(Long id) {
	        Users user = getAuthenticatedUser();
	        if(user!=null) {

	        Optional<Textnote> textnoteOptional = textnoteRepository.findById(id);
	        if (!textnoteOptional.isPresent()) {
	            throw new TextnoteNotFoundException(id);
	        }


	        if (!CheckPermissionById(id, user.getId())) {
	            throw new AccessDeniedException("Bạn không có quyền truy cập vào ghi chú này.");
	        }

	        return textnoteOptional;
	    }
	        throw new UserNotFoundException("Bạn chưa đăng nhập. Vui lòng đăng nhập và thử lại!");
	  }
	  
	  public Optional<Textnote> ViewTextnoteByUrl(String url) throws PrivacyAccessDeniedException, TextnoteNotFoundException {
		    Users user = getAuthenticatedUser();

		    Optional<Textnote> textnoteOptional = textnoteRepository.findByUrl(url);
			Textnote textnote = textnoteOptional.orElseThrow(() -> new TextnoteNotFoundException(url));
		    
		    if (user == null) {
		        if (textnote.getPrivacy() != Privacy.PUBLIC) {
		            throw new PrivacyAccessDeniedException("Bạn không có quyền truy cập vào ghi chú này.");
		        }
		    } else {
		        if (textnote.getPrivacy() != Privacy.PUBLIC && !textnote.getUser().getId().equals(user.getId())) {
		            throw new PrivacyAccessDeniedException("Bạn không có quyền truy cập vào ghi chú này.");
		        }
		    }
		    return textnoteOptional;
		}

	
	
	  public Textnote UpdateTextnoteById(Long id, UpdateTextnoteDTO updateTextnoteDTO) throws AccessDeniedUpdateTextnoteIDException{
		    Users user = getAuthenticatedUser();
		    if(user!=null) {
		    	Optional<Textnote> textnoteOptional = textnoteRepository.findById(id);
			    Textnote textnote = textnoteOptional.orElseThrow(() -> new TextnoteNotFoundException(id));
			    
		    		if (CheckPermissionById(id, user.getId())) {
		        
		    			if (updateTextnoteDTO.getPrivacy() == Privacy.PUBLIC && textnote.getUrl()==null) {
		                String generatedUrl;
		                do {
		                    generatedUrl = generateRandomURL();
		                } while (textnoteRepository.existsByUrl(generatedUrl));
		                updateTextnoteDTO.setUrl(generatedUrl);
			            textnote.setUrl(updateTextnoteDTO.getUrl());
		            }

		    		textnote.setTitle(updateTextnoteDTO.getTitle());
		            textnote.setContent(updateTextnoteDTO.getContent());
		            textnote.setPrivacy(updateTextnoteDTO.getPrivacy());
		            textnote.setUpdatedAt(LocalDateTime.now());
		            return textnoteRepository.save(textnote);
		        }
		        throw new AccessDeniedUpdateTextnoteIDException("Bạn không được quyền xem hoặc cập nhật dữ liệu này");
		}
	        throw new UserNotFoundException("Bạn chưa đăng nhập. Vui lòng đăng nhập và thử lại!");
	  }

	  public Textnote UpdateTextnoteByUrl(String url, UpdateTextnoteDTO updateTextnoteDTO) throws AccessDeniedUpdateTextnoteURLException, TextnoteNotFoundException {
		    Users user = getAuthenticatedUser();
		    Optional<Textnote> textnoteOptional = textnoteRepository.findByUrl(url);
		    Textnote textnote = textnoteOptional.orElseThrow(() -> new TextnoteNotFoundException(url));
		    
		   if(user!=null) {
			   if(CheckPermissionByUrl(url, user.getId())) {
				   textnote.setTitle(updateTextnoteDTO.getTitle());
				   textnote.setContent(updateTextnoteDTO.getContent());
				   textnote.setUpdatedAt(updateTextnoteDTO.getUpdatedAt());
	            return textnoteRepository.save(textnote);
			  }
			   throw new AccessDeniedUpdateTextnoteURLException("Bạn không được phép cập nhật dữ liệu này");
		   }
		   else if (textnote.getPrivacy() == Privacy.PUBLIC && user==null) {
			   textnote.setTitle(updateTextnoteDTO.getTitle());
			   textnote.setContent(updateTextnoteDTO.getContent());
			   textnote.setUpdatedAt(updateTextnoteDTO.getUpdatedAt());
	        return textnoteRepository.save(textnote);
		   	}
		   else
		   throw new AccessDeniedUpdateTextnoteURLException("Bạn không được phép cập nhật dữ liệu này");
	  }


	
	 public void deleteTextnoteById(Long id) {
	        Users user = getAuthenticatedUser();
	        if(user!=null) {
	        	Optional<Textnote> textnote = textnoteRepository.findById(id);
	        	if (!textnote.isPresent()) {
	            throw new TextnoteNotFoundException("Không tìm thấy ghi chú cần xóa.");
	        	}

	        	if (!CheckPermissionById(id, user.getId())) {
	            throw new AccessDeniedException("Bạn không có quyền xóa ghi chú này.");
	        	}

	        	textnoteRepository.deleteById(id);
	    }
	        else
		      throw new UserNotFoundException("Bạn chưa đăng nhập. Vui lòng đăng nhập và thử lại!");
	 }
	 
		public long getCountTextnote() {
			return textnoteRepository.count();
		}

	
	
	private boolean CheckPermissionById(Long textnoteId, Long userId) {
		Optional<Textnote> textnoteOptional = textnoteRepository.findById(textnoteId);
		Textnote textnote = textnoteOptional.orElseThrow(() -> new TextnoteNotFoundException(textnoteId));
			if(!textnote.getUser().getId().equals(userId)) {
				return false;
			}
            return true;
		
	}
	
	private boolean CheckPermissionByUrl(String url, Long userId) {
	    Optional<Textnote> textnoteOptional = textnoteRepository.findByUrl(url);
		Textnote textnote = textnoteOptional.orElseThrow(() -> new TextnoteNotFoundException(url));
	        if (textnote.getUser() == null) {
	            throw new TextnoteNotFoundException("Thông tin ghi chú không hợp lệ.");
	        }
	        if (textnote.getPrivacy() == Privacy.PUBLIC || textnote.getUser().getId().equals(userId)) {
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