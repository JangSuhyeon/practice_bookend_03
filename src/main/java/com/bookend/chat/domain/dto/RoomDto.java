package com.bookend.chat.domain.dto;

import com.bookend.book.domain.entity.Book;
import com.bookend.chat.domain.entity.Room;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@ToString
@Getter
@Builder
public class RoomDto {
    private Long roomId;
    private String isbn;
    private Book book;
    private Set<WebSocketSession> sessions;

    public static RoomDto toDto(Room room) {
        return RoomDto.builder()
                .roomId(room.getRoomId())
                .isbn(room.getIsbn())
                .book(room.getBook())
                .sessions(new HashSet<>())
                .build();
    }
}
