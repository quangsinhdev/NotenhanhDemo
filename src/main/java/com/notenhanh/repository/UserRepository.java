package com.notenhanh.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notenhanh.domain.Review;
import com.notenhanh.domain.RoleUser;
import com.notenhanh.domain.Users;
import com.notenhanh.enumation.Role;
import com.notenhanh.enumation.UserStatus;
@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

	Users save(Users user); // Create Update entity
	
	Page<Users> findAll(Pageable pageable);
	Optional<Users> findByUsername(String username);
	Optional<Users> findById(Long id);
    Optional<Users> findByProviderAndProviderId(String provider, String providerId);
    Optional<Users> findByUsernameOrEmail(String username, String email);
    
	List<Users> findByEmail(String email);
	List<Users> findByReview(List<Review> review);
	List<Users> findByStatus(UserStatus status);
    List<Users> findByRoleUser_Role(Role role);
    
	boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByTokenRecovery(String tokenRecovery);

    Optional<Users> findByTokenRecovery(String tokenRecovery);
    Optional<Users> findByTokenRecoveryAndUsername(String tokenRecovery, String username);

    
    
    long countByStatus(UserStatus status);
}