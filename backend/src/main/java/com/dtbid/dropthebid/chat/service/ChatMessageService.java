package com.dtbid.dropthebid.chat.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.dtbid.dropthebid.chat.model.ChatMessage;
import com.dtbid.dropthebid.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

  private final ChatMessageRepository chatMessageRepository;

  public long createChatMessage(ChatMessage chatMessage) {
    return chatMessageRepository.save(chatMessage);
  }

  public List<ChatMessage> findMessagesByChatRoomId(Long chatRoomId) {
    return chatMessageRepository.findByChatRoomId(chatRoomId);
  }
}
