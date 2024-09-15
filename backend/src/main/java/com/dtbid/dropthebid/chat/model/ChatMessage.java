package com.dtbid.dropthebid.chat.model;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    
    private Long chatMessageId;
    private Long chatRoomId;
    private Long memberId;        // 메세지 보내는 사람
    private String message;
    private Timestamp createdAt;
    
}
