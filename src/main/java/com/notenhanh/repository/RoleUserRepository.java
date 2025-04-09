package com.notenhanh.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notenhanh.domain.RoleUser;
import com.notenhanh.enumation.Role;
@Repository
public interface RoleUserRepository extends JpaRepository<RoleUser, Long> {
	Optional<RoleUser> findById(Long id);
    Optional<RoleUser> findByRole(Role role);
}
