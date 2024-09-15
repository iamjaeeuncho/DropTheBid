package com.dtbid.dropthebid.chat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {
    
    private Long chatRoomId;
    private Long auctionId;       // 메세지 받는 사람
    private Long memberId;        // 메세지 보내는 사람
    
}
