package com.dtbid.dropthebid.chat.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dtbid.dropthebid.chat.model.ChatMessage;
import com.dtbid.dropthebid.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
  
  @Autowired
  private final ChatMessageRepository chatMessageRepository;

  public long createChatMessage(ChatMessage chatMessage) {
    return chatMessageRepository.save(chatMessage);
  }

  // auctionId와 memberId에 맞는 메시지 가져오기
  public List<ChatMessage> getMessagesByAuctionIdAndMemberId(Long auctionId, Long memberId) {
      return chatMessageRepository.findMessagesByAuctionIdAndMemberId(auctionId, memberId);
  }

}
