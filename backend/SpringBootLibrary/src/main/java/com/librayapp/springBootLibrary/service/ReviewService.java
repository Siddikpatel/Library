package com.librayapp.springBootLibrary.service;

import com.librayapp.springBootLibrary.dao.BookRepository;
import com.librayapp.springBootLibrary.dao.ReviewRepository;
import com.librayapp.springBootLibrary.entity.Review;
import com.librayapp.springBootLibrary.requestmodels.ReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;

@Service
@Transactional
public class ReviewService {

    private ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository1) {
        this.reviewRepository = reviewRepository1;
    }

    public void postReview(String userEmail, ReviewRequest reviewRequest) throws Exception {

        if(userReviewListed(userEmail, reviewRequest.getBookId()))
            throw new Exception("Review has already been created");

        Review review = new Review();
        review.setBookId(reviewRequest.getBookId());
        review.setRating(reviewRequest.getRating());
        review.setUserEmail(userEmail);

        if(reviewRequest.getReviewDescription().isPresent()) {
            review.setReviewDescription(reviewRequest.getReviewDescription()
                    .map(Object::toString)
                    .orElse(null));
        }

        review.setDate(Date.valueOf(LocalDate.now()));
        reviewRepository.save(review);
    }

    public boolean userReviewListed(String userEmail, Long bookId) {
        Review validateReview = reviewRepository.findByUserEmailAndBookId(userEmail, bookId);

        return validateReview != null;
    }
}
