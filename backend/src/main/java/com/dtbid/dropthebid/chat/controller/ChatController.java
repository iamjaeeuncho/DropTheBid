package com.dtbid.dropthebid.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.dtbid.dropthebid.chat.model.ChatMessage;
import com.dtbid.dropthebid.chat.model.ChatRoom;
import com.dtbid.dropthebid.chat.service.ChatMessageService;
import com.dtbid.dropthebid.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatController {

  private final ChatRoomService chatRoomService;
  private final ChatMessageService chatMessageService;
  
  @PostMapping("/chatrooms/create")
  public ChatRoom createChatRoom(@RequestBody ChatRoom room) {
      ChatRoom chatRoom = chatRoomService.createChatRoom(room.getAuctionId(), room.getMemberId());
      return chatRoom;
  }
  
  @MessageMapping("/send")
  @SendTo("/topic/messages")
  public ChatMessage sendMessage(ChatMessage message) {
      System.out.println("1 Received message: " + message);
      chatMessageService.createChatMessage(message);
      System.out.println("2 Received message: " + message);
      return message;
  }

}
