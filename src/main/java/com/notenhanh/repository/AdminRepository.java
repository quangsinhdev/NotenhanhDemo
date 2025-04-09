package com.notenhanh.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.notenhanh.domain.RoleUser;
import com.notenhanh.domain.Users;
import com.notenhanh.enumation.UserStatus;

public interface AdminRepository extends JpaRepository<Users, Long> {
	   Page<Users> findAll(Pageable pageable);
	   List<Users> findByRoleUser(RoleUser roleUser);
	   List<Users> findByUsername(String username);
	   void deleteById(Long id);
	   void delete(Users user);
	   long count();
	    @Modifying
	    @Query("UPDATE Users u SET u.status = :status WHERE u.id = :id")
	    void updateStatus(Long id, UserStatus status);
	    @Modifying
	    @Query("UPDATE Users u SET u.roleUser = :roleUser WHERE u.id = :id")
	    void updateRoleUser(Long id, RoleUser roleUser);
}