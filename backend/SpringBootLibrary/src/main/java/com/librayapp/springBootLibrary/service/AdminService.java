package com.librayapp.springBootLibrary.service;

import com.librayapp.springBootLibrary.dao.BookRepository;
import com.librayapp.springBootLibrary.dao.CheckoutRepository;
import com.librayapp.springBootLibrary.dao.ReviewRepository;
import com.librayapp.springBootLibrary.entity.Book;
import com.librayapp.springBootLibrary.requestmodels.AddBookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.Optional;

@Transactional
@Service
public class AdminService {

    private BookRepository bookRepository;
    private ReviewRepository reviewRepository;
    private CheckoutRepository checkoutRepository;

    @Autowired
    public AdminService(BookRepository bookRepository, ReviewRepository reviewRepository, CheckoutRepository checkoutRepository) {
        this.bookRepository = bookRepository;
        this.reviewRepository = reviewRepository;
        this.checkoutRepository = checkoutRepository;
    }

    public void postBook(AddBookRequest addBookRequest) {
        Book book = new Book();
        book.setTitle(addBookRequest.getTitle());
        book.setAuthor(addBookRequest.getAuthor());
        book.setDescription(addBookRequest.getDescription());
        book.setCopies(addBookRequest.getCopies());
        book.setCopiesAvailable(addBookRequest.getCopies());
        book.setImg(addBookRequest.getImg());
        book.setCategory(addBookRequest.getCategory());

        bookRepository.save(book);
    }

    public void deleteBook(Long bookId) throws Exception {
        Optional<Book> book = bookRepository.findById(bookId);

        if(book.isEmpty()) {
            throw new Exception("Book not found!");
        }

        bookRepository.delete(book.get());
        checkoutRepository.deleteAllByBookId(bookId);
        reviewRepository.deleteAllByBookId(bookId);
    }

    public void increaseQuantity(Long bookId) throws Exception {

        Optional<Book> book = bookRepository.findById(bookId);

        if(book.isEmpty()) {
            throw new Exception("Book not found!");
        }

        book.get().setCopiesAvailable(book.get().getCopiesAvailable() + 1);
        book.get().setCopies(book.get().getCopies() + 1);

        bookRepository.save(book.get());
    }

    public void decreaseQuantity(Long bookId) throws Exception {

        Optional<Book> book = bookRepository.findById(bookId);

        if(book.isEmpty() || book.get().getCopiesAvailable() <= 0 || book.get().getCopies() <= 0) {
            throw new Exception("Book not found OR quantity is already 0");
        }

        book.get().setCopiesAvailable(book.get().getCopiesAvailable() - 1);
        book.get().setCopies(book.get().getCopies() - 1);

        bookRepository.save(book.get());
    }
}
