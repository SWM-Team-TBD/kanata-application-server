package moe.kanata.room;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController implements RoomApiDocs {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomJoinTokenResponse> createRoom(
        @AuthenticationPrincipal final UserDetails principal,
        @RequestBody final CreateRoomRequest request
    ) {
        final String email = principal.getUsername();
        final String character = request.character();
        final String token = roomService.createRoom(email, character);
        final RoomJoinTokenResponse response = new RoomJoinTokenResponse(token);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
