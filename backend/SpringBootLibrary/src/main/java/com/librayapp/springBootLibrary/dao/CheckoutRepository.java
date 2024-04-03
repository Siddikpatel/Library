package com.librayapp.springBootLibrary.dao;

import com.librayapp.springBootLibrary.entity.Checkout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface CheckoutRepository extends JpaRepository<Checkout, Long> {

    @Query(value = "SELECT c FROM Checkout c WHERE c.userEmail = ?1 AND c.bookId = ?2")
    Checkout findByUserEmailAndBookId(String userEmail, Long bookId);

    @Query(value = "SELECT c FROM Checkout c WHERE c.userEmail = ?1")
    List<Checkout> findBooksByUserEmail(String email);

    @Modifying
    @Query("DELETE FROM Checkout WHERE bookId in ?1")
    void deleteAllByBookId(Long bookId);
}
