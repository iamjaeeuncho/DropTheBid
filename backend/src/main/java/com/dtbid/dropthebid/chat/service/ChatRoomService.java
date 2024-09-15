package com.dtbid.dropthebid.chat.service;

import org.springframework.stereotype.Service;
import com.dtbid.dropthebid.chat.model.ChatRoom;
import com.dtbid.dropthebid.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

  private final ChatRoomRepository chatRoomRepository;

  public ChatRoom createChatRoom(Long auctionId, Long memberId) {
      ChatRoom chatRoom = new ChatRoom();
      chatRoom.setAuctionId(auctionId);
      chatRoom.setMemberId(memberId);
      
      chatRoomRepository.save(chatRoom);
      return chatRoom;
  }
}
