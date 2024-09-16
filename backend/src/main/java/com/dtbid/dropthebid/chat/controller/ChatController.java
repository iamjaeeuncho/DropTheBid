package com.dtbid.dropthebid.chat.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
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
  
  @Autowired
  private final ChatRoomService chatRoomService;
  @Autowired
  private final ChatMessageService chatMessageService;
  
  // 메세지 보내기
  @MessageMapping("/chat/{auctionId}/{memberId}")
  @SendTo("/topic/chat/{auctionId}/{memberId}")
  public ChatMessage sendMessage(
      @DestinationVariable("auctionId") Long auctionId,
      @DestinationVariable("memberId") Long memberId,
      ChatMessage message) {
      System.out.println("Received message: " + message);
      chatMessageService.createChatMessage(message);
      return message;
  }
  
  // 채팅방 참여
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
  
  // 사용자 ID로 채팅방 목록 조회
  @GetMapping("/rooms/{memberId}")
  public List<ChatRoom> getChatRooms(@PathVariable("memberId") Long memberId) {
      List<ChatRoom> chatRooms = chatRoomService.findChatRoomsByMemberId(memberId);
      return chatRooms;
  }
  
  // 채팅방의 기존 메시지 로드
  @GetMapping("/messages/{chatRoomId}")
  public List<ChatMessage> getChatMessages(@PathVariable("chatRoomId") Long chatRoomId) {

      List<ChatMessage> chatMessages = chatMessageService.getMessagesByChatRoomId(chatRoomId);
      System.out.println("chatMessages" + chatMessages);
      return chatMessages;
  }
  
}
