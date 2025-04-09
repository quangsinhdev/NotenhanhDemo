package com.notenhanh.repository;

import org.springframework.stereotype.Repository;

import com.notenhanh.domain.Task;
import com.notenhanh.domain.Tasknote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    void deleteAllByTaskNote(Tasknote taskNote);
    long count();
}
