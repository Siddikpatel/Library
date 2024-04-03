package com.librayapp.springBootLibrary.dao;

import com.librayapp.springBootLibrary.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query(value = "SELECT m FROM Message m WHERE m.userEmail = ?1")
    Page<Message> findByUserEmail(@RequestParam String userEmail, Pageable pageable);

    @Query(value = "SELECT m FROM Message m WHERE m.closed = ?1")
    Page<Message> findByClosed(@RequestParam boolean closed, Pageable pageable);
}
