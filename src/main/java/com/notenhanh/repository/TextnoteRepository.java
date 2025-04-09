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

import com.notenhanh.domain.Textnote;
import com.notenhanh.domain.Users;
import com.notenhanh.enumation.Privacy;
@Repository
public interface TextnoteRepository extends JpaRepository<Textnote, Long>{
	Textnote save(Textnote textnote);
	
	Page<Textnote> findAll(Pageable pageable);
	
	Optional<Textnote> findById(Long id);
	List<Textnote> findByTitle(String title);
	List<Textnote> findByPrivacy(Privacy privacy);
	Optional<Textnote> findByUrl(String url);
	Page<Textnote> findByUser(Users user, Pageable pageable);
	
    @Query("SELECT t FROM Textnote t WHERE t.title LIKE %:title%")
    Page<Textnote> findByTitleAndUser_Id(@Param("title") String title, Long userId, Pageable pageable);
    @Query("SELECT t FROM Textnote t WHERE t.title LIKE %:title% AND t.privacy = :privacy AND t.user.id = :userId")
    Page<Textnote> findByTitleAndPrivacyAndUser_Id(@Param("title") String title, @Param("privacy") Privacy privacy, @Param("userId") Long userId, Pageable pageable);
    
	Page<Textnote> findByUserId(Long userId, Pageable pageable);
    
	void deleteById(Long id);
	void deleteByUrl(String url);
	
	long count();
	
	boolean existsByUrl(String url);
	
    Page<Textnote> findByPrivacyAndUser_Id(Privacy privacy, Long userId, Pageable pageable);

}