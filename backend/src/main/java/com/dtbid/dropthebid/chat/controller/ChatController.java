package com.dtbid.dropthebid.chat.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
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
  @MessageMapping("/send/{chatRoomId}")
  @SendTo("/topic/messages/{chatRoomId}")
  public ChatMessage sendMessage(ChatMessage message) {
      System.out.println("message : " + message);
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

//  @GetMapping("/member/{auctionId}")
//  public ResponseEntity<Long> getMemberId(@PathVariable Long auctionId) {
//      Long memberId = chatRoomService.getMemberIdForAuction(auctionId);
//      return ResponseEntity.ok(memberId);
//  }
  
  // 사용자 ID로 채팅방 목록 조회
  @GetMapping("/room/{memberId}")
  public List<ChatRoom> getChatRooms(@PathVariable("memberId") Long memberId) {
      List<ChatRoom> chatRooms = chatRoomService.findChatRoomsByMemberId(memberId);
      System.out.println("test" + chatRooms);
      return chatRooms;
  }
  
}
