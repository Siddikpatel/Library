package com.librayapp.springBootLibrary.Controller;

import com.librayapp.springBootLibrary.Utils.ExtractJWT;
import com.librayapp.springBootLibrary.dao.CheckoutRepository;
import com.librayapp.springBootLibrary.dao.ReviewRepository;
import com.librayapp.springBootLibrary.requestmodels.AddBookRequest;
import com.librayapp.springBootLibrary.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("https://localhost:3000")
@RequestMapping("/api/admin")
public class AdminController {

    private AdminService adminService;

    private static final String SUB = "\"sub\"";
    private static final String USERTYPE = "\"userType\"";

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/secure/add/book")
    public void postBook(@RequestHeader(value = "Authorization") String token, @RequestBody AddBookRequest addBookRequest) throws Exception {

        String admin = ExtractJWT.payloadJWTExtraction(token, USERTYPE);

        if(admin == null || !admin.equals("admin")) {
            throw new Exception("Not Authorized!");
        }

        adminService.postBook(addBookRequest);
    }

    @DeleteMapping("/secure/delete/book")
    public void deleteBook(@RequestHeader(value = "Authorization") String token, @RequestParam Long bookId) throws Exception {

        String admin = ExtractJWT.payloadJWTExtraction(token, USERTYPE);

        if(admin == null || !admin.equals("admin")) {
            throw new Exception("Not Authorized!");
        }

        adminService.deleteBook(bookId);
    }

    @PutMapping("/secure/increase/book/quantity")
    public void increaseBookQuantity(@RequestHeader(value = "Authorization") String token, @RequestParam Long bookId) throws Exception {

        String admin = ExtractJWT.payloadJWTExtraction(token, USERTYPE);

        if(admin == null || !admin.equals("admin")) {
            throw new Exception("Not Authorized!");
        }

        adminService.increaseQuantity(bookId);
    }

    @PutMapping("/secure/decrease/book/quantity")
    public void decreaseBookQuantity(@RequestHeader(value = "Authorization") String token, @RequestParam Long bookId) throws Exception {

        String admin = ExtractJWT.payloadJWTExtraction(token, USERTYPE);

        if(admin == null || !admin.equals("admin")) {
            throw new Exception("Not Authorized!");
        }

        adminService.decreaseQuantity(bookId);
    }
}
