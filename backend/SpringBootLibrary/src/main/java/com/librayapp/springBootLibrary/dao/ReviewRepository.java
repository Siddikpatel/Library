package com.librayapp.springBootLibrary.dao;

import com.librayapp.springBootLibrary.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query(value = "SELECT r FROM Review r WHERE r.bookId = ?1")
    Page<Review> findByBookId(Long bookId, Pageable pageable);

    @Query(value = "SELECT r FROM Review r WHERE r.bookId = ?2 AND r.userEmail = ?1")
    Review findByUserEmailAndBookId(String email, Long bookId);

    @Modifying
    @Query("DELETE FROM Review WHERE bookId in ?1")
    void deleteAllByBookId(Long bookId);
}
