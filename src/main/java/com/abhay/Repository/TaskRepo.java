package com.abhay.Repository;

import com.abhay.Dto.TaskDto;
import com.abhay.Entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface TaskRepo extends JpaRepository<Task,Integer> {
    public List<Task> findAllTaskByUserId(Integer userid);
    public Optional<Task> findByUserId(Integer userId);
    public Page<Task> findByUserIdAndCompletedTrue(Integer userId, Pageable pageable);
    public Page<Task> findByUserIdAndCompletedFalse(Integer userId, Pageable pageable);
    @Query("""
    SELECT t
    FROM Task t
    WHERE t.completed = :completed
      AND t.due_date > CURRENT_TIMESTAMP
      AND t.due_date <= :timeLimit
""")
    List<Task> findPendingTasksDueWithin(@Param("completed") boolean completed,
                                         @Param("timeLimit") LocalDateTime timeLimit);

}


