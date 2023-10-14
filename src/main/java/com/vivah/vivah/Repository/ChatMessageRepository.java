package com.vivah.vivah.Repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.twilio.rest.chat.v1.service.User;
import com.vivah.vivah.model.ChatMessage;
import com.vivah.vivah.model.Userchat;




public interface ChatMessageRepository extends CrudRepository<ChatMessage, Long> {
    List<ChatMessage> findBySenderAndReceiver(User sender, User receiver);

	List<ChatMessage> findBySenderAndReceiver(Userchat senderUser, Userchat receiverUser);
}