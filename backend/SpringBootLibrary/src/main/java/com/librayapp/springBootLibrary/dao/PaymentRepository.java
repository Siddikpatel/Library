package com.librayapp.springBootLibrary.dao;

import com.librayapp.springBootLibrary.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query(value = "SELECT p FROM Payment p WHERE p.userEmail = ?1")
    Payment findByUserEmail(@RequestParam String userEmail);
}
