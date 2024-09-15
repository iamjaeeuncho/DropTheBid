package com.dtbid.dropthebid.chat.controller;

import java.util.HashMap;
import java.util.Map;
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
  
  @PostMapping("/room/join")
  public ChatRoom joinChatRoom(@RequestBody Map<String, Long> request) {
      Long auctionId = request.get("auctionId");
      Long memberId = request.get("memberId");

      Map<String, Object> params = new HashMap<>();
      params.put("auctionId", auctionId);
      params.put("memberId", memberId);

      ChatRoom existingChatRoom = chatRoomService.findByAuctionIdAndMemberId(params);
      
      if (existingChatRoom == null) {
          ChatRoom newChatRoom = new ChatRoom();
          newChatRoom.setAuctionId(auctionId);
          newChatRoom.setMemberId(memberId);

          chatRoomService.createChatRoom(newChatRoom);
          long chatRoomId = newChatRoom.getChatRoomId();

          return newChatRoom;
      }
      System.out.println("existingChatRoom : " + existingChatRoom);
      return existingChatRoom;
  }
  
  @MessageMapping("/send")
  @SendTo("/topic/messages")
  public ChatMessage sendMessage(ChatMessage message) {
      System.out.println(message);
      chatMessageService.createChatMessage(message);
      return message;
  }
}
