package com.notenhanh.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notenhanh.domain.Settings;
@Repository
public interface SettingsRepository extends JpaRepository<Settings, Long> {
	Settings save(Settings settings);
	List<Settings> findAll();
	Optional<Settings> findById(Long id);
}
