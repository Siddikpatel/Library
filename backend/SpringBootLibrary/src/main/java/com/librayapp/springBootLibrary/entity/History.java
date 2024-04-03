package com.librayapp.springBootLibrary.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "history")
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="user_email")
    private String userEmail;

    @Column(name="checkout_date")
    private String checkoutDate;

    @Column(name="returned_date")
    private String returnDate;

    @Column(name="title")
    private String title;

    @Column(name="author")
    private String author;

    @Column(name="description")
    private String description;

    @Column(name="img")
    private String image;


    public History() {}

    public History(String userEmail, String checkoutDate, String returnDate
                    , String title, String author, String description, String image) {

        this.userEmail = userEmail;
        this.author = author;
        this.title = title;
        this.checkoutDate = checkoutDate;
        this.returnDate = returnDate;
        this.image = image;
        this.description = description;
    }
}
