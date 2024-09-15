package com.dtbid.dropthebid.chat.repository;

import org.apache.ibatis.annotations.Mapper;
import com.dtbid.dropthebid.chat.model.ChatMessage;

@Mapper
public interface ChatMessageRepository {

  void save(ChatMessage message);

  ChatMessage findById(Long chatMessageId);
}
