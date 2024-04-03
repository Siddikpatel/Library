package com.librayapp.springBootLibrary.dao;

import com.librayapp.springBootLibrary.entity.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestParam;

public interface HistoryRepository extends JpaRepository<History, Long> {

    @Query(value = "SELECT h from History h WHERE h.userEmail = ?1")
    Page<History> findBooksByUserEmail(String userEmail, Pageable pageable);

    @Query(value = "SELECT COUNT(*) from History h WHERE h.userEmail = ?1 AND h.title = ?2")
    int wasBoughtByUser(String userEmail, String title);
}
