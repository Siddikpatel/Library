package com.librayapp.springBootLibrary.Controller;

import com.librayapp.springBootLibrary.Utils.ExtractJWT;
import com.librayapp.springBootLibrary.requestmodels.PaymentInfoRequest;
import com.librayapp.springBootLibrary.service.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/payment/secure")
@CrossOrigin("https://localhost:3000")
@RestController
public class PaymentController {

    private PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/payment-intent")
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentInfoRequest paymentInfoRequest) throws StripeException {

        PaymentIntent paymentIntent = paymentService.paymentIntent(paymentInfoRequest);
        String paymentString = paymentIntent.toJson();

        return new ResponseEntity<>(paymentString, HttpStatus.OK);
    }

    @PutMapping("/payment-complete")
    public ResponseEntity<String> stripePaymentComplete(@RequestHeader("Authorization") String token) throws Exception {

        String email = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        if(email == null) {
            throw new Exception("User email is missing!");
        }

        return paymentService.stripePayment(email);
    }
}
