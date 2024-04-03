package com.librayapp.springBootLibrary.service;

import com.librayapp.springBootLibrary.ResponseModels.ShelfCurrentLoansResponse;
import com.librayapp.springBootLibrary.dao.BookRepository;
import com.librayapp.springBootLibrary.dao.CheckoutRepository;
import com.librayapp.springBootLibrary.dao.HistoryRepository;
import com.librayapp.springBootLibrary.dao.PaymentRepository;
import com.librayapp.springBootLibrary.entity.Book;
import com.librayapp.springBootLibrary.entity.Checkout;
import com.librayapp.springBootLibrary.entity.History;
import com.librayapp.springBootLibrary.entity.Payment;
import org.hibernate.annotations.Check;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class BookService {

    private BookRepository bookRepository;
    private CheckoutRepository checkoutRepository;
    private HistoryRepository historyRepository;
    private PaymentRepository paymentRepository;

    @Autowired
    public BookService(BookRepository b, CheckoutRepository c, HistoryRepository h, PaymentRepository p) {
        this.bookRepository = b;
        this.checkoutRepository = c;
        this.historyRepository = h;
        this.paymentRepository = p;
    }

    public Book checkoutBook(String userEmail, Long bookId) throws Exception {

        Optional<Book> book = bookRepository.findById(bookId);
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if(book.isEmpty() || validateCheckout != null || book.get().getCopiesAvailable() <= 0) {
            throw new Exception("Book doesn't exist or already checked out by user");
        }

        List<Checkout> currentCheckedOutBooks = checkoutRepository.findBooksByUserEmail(userEmail);
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");
        boolean bookNeedsReturned = false;

        for(Checkout checkout : currentCheckedOutBooks) {
            Date d1 = sdf.parse(checkout.getReturnDate());
            Date d2 = sdf.parse(LocalDate.now().toString());

            TimeUnit time = TimeUnit.DAYS;

            double diffInTime = time.convert(d1.getTime() - d2.getTime(), TimeUnit.MILLISECONDS);

            if(diffInTime < 0) {
                bookNeedsReturned = true;
                break;
            }
        }

        Payment payment = paymentRepository.findByUserEmail(userEmail);
        if((payment != null && payment.getAmount() > 0) || (payment != null && bookNeedsReturned)) {
            throw new Exception("Outstanding fees!");
        }

        if(payment == null) {
            Payment payment1 = new Payment();
            payment1.setAmount(00.00);
            payment1.setUserEmail(userEmail);
            paymentRepository.save(payment1);
        }

        book.get().setCopiesAvailable(book.get().getCopiesAvailable() - 1);
        bookRepository.save(book.get());

        Checkout checkout = new Checkout(
                userEmail,
                LocalDate.now().toString(),
                LocalDate.now().plusDays(7).toString(),
                book.get().getId()
        );

        checkoutRepository.save(checkout);
        return book.get();
    }

    public int currentLoansCount(String email) {
        return checkoutRepository.findBooksByUserEmail(email).size();
    }

    public List<ShelfCurrentLoansResponse> currentLoans(String userEmail) throws Exception {
        List<ShelfCurrentLoansResponse> list = new ArrayList<>();

        List<Checkout> checkoutList = checkoutRepository.findBooksByUserEmail(userEmail);
        List<Long> bookIdList = new ArrayList<>();

        for(Checkout i : checkoutList) {
            bookIdList.add(i.getBookId());
        }

        List<Book> books = bookRepository.findBooksByBookIds(bookIdList);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for(Book book : books) {
            Optional<Checkout> checkout = checkoutList.stream()
                    .filter(x -> Objects.equals(x.getBookId(), book.getId())).findFirst();

            if(checkout.isPresent()) {
                Date returnDate = sdf.parse(checkout.get().getReturnDate());
                Date todaysDate = sdf.parse(LocalDate.now().toString());

                TimeUnit time = TimeUnit.DAYS;

                long diff_in_time = time.convert(returnDate.getTime() - todaysDate.getTime(), TimeUnit.MILLISECONDS);

                list.add(new ShelfCurrentLoansResponse(book, (int) diff_in_time));
            }
        }

        return list;
    }

    public boolean isCheckedOutByUser(String email, Long bookId) {
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(email, bookId);

        return validateCheckout != null;
    }

    public void returnBook(String email, Long bookId) throws Exception {
        Optional<Book> book = bookRepository.findById(bookId);

        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(email, bookId);

        if(book.isEmpty() || validateCheckout == null) {
            throw new Exception("Book doesn't exist or checked out by user!");
        }

        book.get().setCopiesAvailable(book.get().getCopiesAvailable() + 1);
        bookRepository.save(book.get());

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDateTime date1 = LocalDate.parse(validateCheckout.getReturnDate(), dtf).atStartOfDay();
        LocalDateTime date2 = LocalDate.parse(LocalDate.now().toString(), dtf).atStartOfDay();

        long daysBetween = Duration.between(date1, date2).toDays();

        if(daysBetween > 0) {
            Payment payment = paymentRepository.findByUserEmail(email);

            payment.setAmount(payment.getAmount() + daysBetween);
            paymentRepository.save(payment);
        }

        checkoutRepository.deleteById(validateCheckout.getId());

        History history = new History(
                email,
                validateCheckout.getCheckoutDate(),
                LocalDate.now().toString(),
                book.get().getTitle(),
                book.get().getAuthor(),
                book.get().getDescription(),
                book.get().getImg()
        );

        historyRepository.save(history);
    }

    public void renewLoan(String email, Long bookId) throws Exception {
        Checkout validate = checkoutRepository.findByUserEmailAndBookId(email, bookId);

        if(validate == null)
            throw new Exception("Book doesn't exist or not checked out by the user");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = sdf.parse(validate.getReturnDate());
        Date d2 = sdf.parse(LocalDate.now().toString());

        if(d1.compareTo(d2) >= 0) {
            validate.setReturnDate(LocalDate.now().plusDays(7).toString());
            checkoutRepository.save(validate);
        }
    }

    public Page<Book> fetchBooks(int page, int size) {
        Page<Book> books = bookRepository.findAllCustom(PageRequest.of(page, size));
        return books;
    }

    public Page<Book> fetchBooksByCategory(String category, int page, int size) {
        Page<Book> books = bookRepository.findByCategory(category, PageRequest.of(page, size));
        return books;
    }

    public Page<Book> fetchBooksByTitle(String title, int page, int size) {
        Page<Book> books = bookRepository.findByTitleContaining(title, PageRequest.of(page, size));
        return books;
    }

    public Optional<Book> findBookById(long id) {
        return bookRepository.findById(id);
    }
}
