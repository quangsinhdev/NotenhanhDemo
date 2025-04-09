package com.notenhanh.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notenhanh.domain.Review;
import com.notenhanh.domain.Users;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
	Review save(Review review);
	Page<Review> findAll(Pageable pageable);
	Optional<Review> findById(Long id);
	List<Review> findByUser(Users user);
	void deleteById(Long id);
	long count();
}