package com.librayapp.springBootLibrary.Controller;

import com.librayapp.springBootLibrary.ResponseModels.CatalogModel;
import com.librayapp.springBootLibrary.ResponseModels.ShelfCurrentLoansResponse;
import com.librayapp.springBootLibrary.Utils.ExtractJWT;
import com.librayapp.springBootLibrary.entity.Book;
import com.librayapp.springBootLibrary.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("https://localhost:3000")
@RequestMapping("/api/books")
public class BookController {

    private static final String SUB = "\"sub\"";

    private BookService bookService;

    @Autowired
    public BookController(BookService b) {
        this.bookService = b;
    }

    @GetMapping("/secure/currentloans")
    public List<ShelfCurrentLoansResponse> currentLoans(@RequestHeader(value = "Authorization") String token) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, SUB);
        return bookService.currentLoans(userEmail);
    }

    @GetMapping("/secure/currentloans/count")
    public int currentLoansCount(@RequestHeader(value = "Authorization") String token) {
        String email = ExtractJWT.payloadJWTExtraction(token, SUB);
        return bookService.currentLoansCount(email);
    }

    @GetMapping("/secure/ischeckedout/byuser")
    public boolean isCheckedOutBookByUser(@RequestHeader(value = "Authorization") String token, Long bookId) {
        String email = ExtractJWT.payloadJWTExtraction(token, SUB);
        return bookService.isCheckedOutByUser(email, bookId);
    }

    @PutMapping("/secure/checkout")
    public Book checkoutBook(@RequestHeader(value = "Authorization") String token, @RequestParam Long bookId) throws Exception {
        String email = ExtractJWT.payloadJWTExtraction(token, SUB);
        return bookService.checkoutBook(email, bookId);
    }

    @PutMapping("/secure/return")
    public void returnBook(@RequestHeader(value = "Authorization") String token, @RequestParam Long bookId) throws Exception {

        String email = ExtractJWT.payloadJWTExtraction(token, SUB);
        bookService.returnBook(email, bookId);
    }

    @PutMapping("/secure/renew/loan")
    public void renewLoan(@RequestHeader(value = "Authorization") String token, @RequestParam Long bookId) throws Exception {
        String email = ExtractJWT.payloadJWTExtraction(token, SUB);
        bookService.renewLoan(email, bookId);
    }

    @GetMapping("/catalog")
    public CatalogModel<Book> getBooks(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size) {
        Page<Book> books = bookService.fetchBooks(page, size);
        return new CatalogModel<>(books.stream().toList(), books.getTotalElements(), books.getTotalPages());
    }

    @GetMapping("/catalog/type")
    public CatalogModel<Book> getBooksByCategory(@RequestParam(value = "cat") String category, @RequestParam(value = "page") int page, @RequestParam(value = "size") int size) {
        Page<Book> books = bookService.fetchBooksByCategory(category, page, size);
        return new CatalogModel<>(books.stream().toList(), books.getTotalElements(), books.getTotalPages());
    }

    @GetMapping("/catalog/search")
    public CatalogModel<Book> getBooksByTitle(@RequestParam(value = "title") String title, @RequestParam(value = "page") int page, @RequestParam(value = "size") int size) {
        Page<Book> books = bookService.fetchBooksByTitle(title, page, size);
        return new CatalogModel<>(books.stream().toList(), books.getTotalElements(), books.getTotalPages());
    }

    @GetMapping("/catalog/{bookId}")
    public ResponseEntity<Book> getBookById(@PathVariable("bookId") long id) {
        Optional<Book> book = bookService.findBookById(id);
        return book.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }
}
