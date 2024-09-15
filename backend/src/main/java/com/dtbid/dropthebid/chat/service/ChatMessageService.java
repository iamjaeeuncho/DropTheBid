package com.dtbid.dropthebid.chat.service;

import org.springframework.stereotype.Service;
import com.dtbid.dropthebid.chat.model.ChatMessage;
import com.dtbid.dropthebid.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

  private final ChatMessageRepository chatMessageRepository;

  public void createChatMessage(ChatMessage chatMessage) {
    chatMessageRepository.save(chatMessage);
  }
}
