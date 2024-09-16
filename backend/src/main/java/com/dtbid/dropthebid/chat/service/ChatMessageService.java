package com.dtbid.dropthebid.chat.service;

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

}
