package com.dtbid.dropthebid.chat.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.dtbid.dropthebid.chat.model.ChatMessage;

@Mapper
public interface ChatMessageRepository {

  long save(ChatMessage message);

  List<ChatMessage> getMessagesByChatRoomId(Long chatRoomId);

}
