package com.bookend.chat;

import com.bookend.book.domain.entity.Book;
import com.bookend.book.repository.BookRepository;
import com.bookend.chat.domain.dto.RoomDto;
import com.bookend.chat.domain.entity.Room;
import com.bookend.login.domain.SessionUser;
import com.bookend.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ChatService {

    private Map<Long, RoomDto> rooms;

    private final BookRepository bookRepository;
    private final ChatRepository chatRepository;

    @PostConstruct
    private void init() {
        rooms = new LinkedHashMap<>();
    }

    public List<RoomDto> findAllRoom() {
        return new ArrayList<>(rooms.values());
    }

    public RoomDto findRoomById(Long roomId) {
        return rooms.get(roomId);
    }

    public RoomDto createRoom(String isbn,
                               SessionUser guestUser,
                               Object principalUser) {

        Long id;
        if (principalUser instanceof OAuth2User) { // 구글 계정
            id = guestUser.getId(); // Todo 왜 googleUser에는 id가 없을까?
        } else if (principalUser instanceof UserDetails) { // 게스트 계정
            id = ((PrincipalDetails) principalUser).getUser().getId();
        } else {
            id = null; // Todo 에러 예외 처리
        }

        // isbn으로 book 조회
        Book book = bookRepository.findByIsbn(isbn);

        Room room = Room.builder()
                .isbn(isbn)
                .book(book)
                .build();

        // Room 엔티티를 db에 저장
        Room savedRoom = chatRepository.save(room);
        RoomDto savedRoomDto = RoomDto.toDto(room);
        rooms.put(savedRoom.getRoomId(), savedRoomDto);

        return savedRoomDto;
    }

}
