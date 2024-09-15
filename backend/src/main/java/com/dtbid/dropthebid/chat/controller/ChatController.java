package com.dtbid.dropthebid.chat.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
      System.out.println("existingChatRoom : " + existingChatRoom);
      
      if (existingChatRoom == null) {
          ChatRoom newChatRoom = new ChatRoom();
          newChatRoom.setAuctionId(auctionId);
          newChatRoom.setMemberId(memberId);

          chatRoomService.createChatRoom(newChatRoom);

          return newChatRoom;
      }

      return existingChatRoom;
  }
  
  @GetMapping("/room/{chatRoomId}/messages")
  public List<ChatMessage> getMessages(@PathVariable("chatRoomId") Long chatRoomId) {
      return chatMessageService.findMessagesByChatRoomId(chatRoomId);
  }
  
  @MessageMapping("/send")
  @SendTo("/topic/messages")
  public ChatMessage sendMessage(ChatMessage message) {
      System.out.println("message : " + message);
      chatMessageService.createChatMessage(message);
      return message;
  }

  @GetMapping("/chatrooms/{memberId}")
  public List<ChatRoom> getChatRoomsByMemberId(@PathVariable("memberId") Long memberId) {
      return chatRoomService.findByMemberId(memberId);
  }
}
