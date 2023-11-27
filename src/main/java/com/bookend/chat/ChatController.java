package com.bookend.chat;

import com.bookend.chat.domain.dto.RoomDto;
import com.bookend.chat.domain.entity.Room;
import com.bookend.login.domain.SessionUser;
import com.bookend.security.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/chat")
@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @GetMapping("")
    public String chatList(Model model) {
        List<RoomDto> roomList = chatService.findAllRoom();
        model.addAttribute("roomList", roomList);
        return "chat/list";
    }

    @PostMapping("/create")
    public String createRoom(Model model,
                             @RequestParam String isbn,
                             @LoginUser SessionUser user,
                             @AuthenticationPrincipal Object principalUser) {

        RoomDto room = chatService.createRoom(isbn, user, principalUser);
        model.addAttribute("room", room);

        return "chat/room";
    }

    @GetMapping("/room")
    public String chatRoom(Model model, @RequestParam Long roomId) {
        RoomDto room = chatService.findRoomById(roomId);
        model.addAttribute("room", room);
        return "chat/room";
    }
}
