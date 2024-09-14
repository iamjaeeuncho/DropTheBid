package com.dtbid.dropthebid.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import com.dtbid.dropthebid.chat.model.ChatMessage;

@Controller
public class ChatController {

  @MessageMapping("/send")
  @SendTo("/topic/messages")
  public ChatMessage sendMessage(ChatMessage message) {
      System.out.println("Received message: " + message);
      return message;
  }

}
