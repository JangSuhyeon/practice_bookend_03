package com.bookend.chat.domain.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;

@Getter
@Setter
@ToString
public class ChatMessage {
    public enum MessageType {
        ENTER, TALK, QUIT
    }
    private MessageType type; // 메세지 타입
    private Long roomId;    // 방 번호
    private String sender;    // 발신자
    private String message;   // 메세지 내용
}
