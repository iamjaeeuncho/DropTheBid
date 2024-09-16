package com.dtbid.dropthebid.chat.repository;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import com.dtbid.dropthebid.chat.model.ChatRoom;

@Mapper
public interface ChatRoomRepository {

    void save(ChatRoom chatRoom);

    ChatRoom findByAuctionIdAndMemberId(Map<String, Object> params);
    
    List<ChatRoom> findChatRoomsByMemberId(Long memberId);

}