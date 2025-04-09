package com.notenhanh.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.notenhanh.domain.Tasknote;
import com.notenhanh.domain.Textnote;
import com.notenhanh.domain.Users;
import com.notenhanh.enumation.Privacy;
@Repository
public interface TasknoteRepository extends JpaRepository<Tasknote, Long>{
	Textnote save(Textnote textnote);
	
	Page<Tasknote> findAll(Pageable pageable);
	
	Optional<Tasknote> findById(Long id);
	List<Tasknote> findByTitle(String title);
	List<Tasknote> findByPrivacy(Privacy privacy);
	Optional<Tasknote> findByUrl(String url);
	Page<Tasknote> findByUser(Users user, Pageable pageable);
	
	@Query("SELECT t FROM Tasknote t WHERE t.title LIKE %:title%")
	Page<Tasknote> findByTitleAndUser_Id(@Param("title") String title, Long userId, Pageable pageable);
	@Query("SELECT t FROM Tasknote t WHERE t.title LIKE %:title% AND t.privacy = :privacy AND t.user.id = :userId")
	Page<Tasknote> findByTitleAndPrivacyAndUser_Id(@Param("title") String title, @Param("privacy") Privacy privacy, @Param("userId") Long userId, Pageable pageable);
	
	Page<Tasknote> findByUserId(Long userId, Pageable pageable);
    
	void deleteById(Long id);
	void deleteByUrl(String url);
	
	long count();
	boolean existsByUrl(String url);
	
    Page<Tasknote> findByPrivacyAndUser_Id(Privacy privacy, Long userId, Pageable pageable);
}