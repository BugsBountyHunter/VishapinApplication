package com.ahmedsr.task.repository;

import com.ahmedsr.task.model.Comment;
import com.ahmedsr.task.utils.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "SELECT c FROM Comment c ORDER BY c.id DESC LIMIT 5", nativeQuery=true)
    List<Comment> getTopComments();
}
