package com.librayapp.springBootLibrary.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name = "checkout")
@Data
public class Checkout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "checkout_date")
    private String checkoutDate;

    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "return_date")
    private String returnDate;

    public Checkout() {
    }

    public Checkout(String userEmail, String checkoutDate, String returnDate, Long bookId) {
        this.userEmail = userEmail;
        this.checkoutDate = checkoutDate;
        this.bookId = bookId;
        this.returnDate = returnDate;
    }
}
