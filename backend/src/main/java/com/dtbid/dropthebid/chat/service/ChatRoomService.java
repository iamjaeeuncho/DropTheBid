package com.dtbid.dropthebid.chat.service;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.dtbid.dropthebid.chat.model.ChatRoom;
import com.dtbid.dropthebid.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

  private final ChatRoomRepository chatRoomRepository;

  public ChatRoom findByAuctionIdAndMemberId(Map<String, Object> params) {
    return chatRoomRepository.findByAuctionIdAndMemberId(params);
  }

  public void createChatRoom(ChatRoom newChatRoom) {
    chatRoomRepository.save(newChatRoom);
  }

  public ChatRoom findById(long chatRoomId) {
    return chatRoomRepository.findById(chatRoomId);
  }

  public List<ChatRoom> findByMemberId(Long memberId) {
    return chatRoomRepository.findByMemberId(memberId);
  }
}
