package com.librayapp.springBootLibrary.Controller;

import com.librayapp.springBootLibrary.Utils.ExtractJWT;
import com.librayapp.springBootLibrary.entity.Message;
import com.librayapp.springBootLibrary.requestmodels.AdminQuestionRequest;
import com.librayapp.springBootLibrary.service.MessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("https://localhost:3000")
@RequestMapping("/api/messages")
public class MessagesController {

    private MessagesService messagesService;
    private static final String SUB = "\"sub\"";
    private static final String USERTYPE = "\"userType\"";

    @Autowired
    public MessagesController(MessagesService service) {
        this.messagesService = service;
    }

    @PostMapping("/secure/add/message")
    public void postMessage(@RequestHeader(value = "Authorization") String token, @RequestBody Message messageRequest) {

        String email = ExtractJWT.payloadJWTExtraction(token, SUB);
        messagesService.postMessage(messageRequest, email);
    }

    @PutMapping("/secure/admin/message")
    public void putMessage(@RequestHeader(value = "Authorization") String token, @RequestBody AdminQuestionRequest adminQuestionRequest) throws Exception {

        String email = ExtractJWT.payloadJWTExtraction(token, SUB);
        String admin = ExtractJWT.payloadJWTExtraction(token, USERTYPE);

        if(admin == null || !admin.equals("admin")) {
            throw new Exception("Not Allowed!");
        }

        messagesService.putMessage(adminQuestionRequest, email);
    }
}
