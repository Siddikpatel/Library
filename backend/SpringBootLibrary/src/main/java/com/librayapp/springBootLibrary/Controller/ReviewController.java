package com.librayapp.springBootLibrary.Controller;

import com.librayapp.springBootLibrary.Utils.ExtractJWT;
import com.librayapp.springBootLibrary.requestmodels.ReviewRequest;
import com.librayapp.springBootLibrary.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("https://localhost:3000")
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private ReviewService reviewService;
    private static final String SUB = "\"sub\"";

    @Autowired
    public ReviewController(ReviewService reviewService1) {
        this.reviewService = reviewService1;
    }

    @GetMapping("/secure/user/book")
    public boolean reviewBookByUser(@RequestHeader(value = "Authorization") String token, @RequestParam Long bookId) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, SUB);

        if(userEmail == null)
            throw new Exception("User email is missing!");

        return reviewService.userReviewListed(userEmail, bookId);

    }

    @PostMapping("/secure")
    public void postReview(@RequestHeader(value = "Authorization") String token, @RequestBody ReviewRequest reviewRequest) throws Exception {

        String userEmail = ExtractJWT.payloadJWTExtraction(token, SUB);
        if(userEmail == null) {
            throw new Exception("User email is missing!");
        }

        reviewService.postReview(userEmail, reviewRequest);
    }
}
