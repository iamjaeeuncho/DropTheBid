package com.dtbid.dropthebid.chat.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dtbid.dropthebid.chat.model.ChatRoom;
import com.dtbid.dropthebid.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
  
  @Autowired
  private final ChatRoomRepository chatRoomRepository;
  
  public ChatRoom findByAuctionIdAndMemberId(Map<String, Object> params) {
    return chatRoomRepository.findByAuctionIdAndMemberId(params);
  }

  public void createChatRoom(ChatRoom newChatRoom) {
    chatRoomRepository.save(newChatRoom);
  }

  //사용자 ID로 채팅방 목록 조회
  public List<ChatRoom> findChatRoomsByMemberId(Long memberId) {
      return chatRoomRepository.findChatRoomsByMemberId(memberId);
  }
  
}
