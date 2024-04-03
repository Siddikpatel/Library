package com.librayapp.springBootLibrary.service;

import com.librayapp.springBootLibrary.dao.MessageRepository;
import com.librayapp.springBootLibrary.entity.Message;
import com.librayapp.springBootLibrary.requestmodels.AdminQuestionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class MessagesService {

    private MessageRepository messageRepository;

    @Autowired
    public MessagesService(MessageRepository repo) {
        this.messageRepository = repo;
    }

    public void postMessage(Message msgRequest, String userEmail) {
        Message message = new Message(msgRequest.getTitle(), msgRequest.getQuestion());
        message.setUserEmail(userEmail);
        messageRepository.save(message);
    }

    public void putMessage(AdminQuestionRequest adminQuestionRequest, String userEmail) throws Exception {
        Optional<Message> message = messageRepository.findById(adminQuestionRequest.getId());

        if(message.isEmpty()) {
            throw new Exception("Message not found!");
        }

        message.get().setAdminEmail(userEmail);
        message.get().setResponse(adminQuestionRequest.getResponse());
        message.get().setClosed(true);

        messageRepository.save(message.get());
    }
}
