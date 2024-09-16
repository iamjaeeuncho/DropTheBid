package com.dtbid.dropthebid.chat.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.dtbid.dropthebid.chat.model.ChatMessage;
import com.dtbid.dropthebid.chat.model.ChatRoom;

@Mapper
public interface ChatMessageRepository {

  long save(ChatMessage message);

  ChatMessage findById(Long chatMessageId);
  
  List<ChatRoom> findChatRoomsByMemberId(Long memberId);

  @Select("SELECT * FROM CHAT_MESSAGE WHERE AUCTION_ID = #{auctionId} AND MEMBER_ID = #{memberId}")
  List<ChatMessage> findMessagesByAuctionIdAndMemberId(@Param("auctionId") Long auctionId, @Param("memberId") Long memberId);
}
