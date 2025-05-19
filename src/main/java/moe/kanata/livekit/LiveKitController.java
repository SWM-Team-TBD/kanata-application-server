package moe.kanata.livekit;

import lombok.RequiredArgsConstructor;
import moe.kanata.member.domain.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/livekit")
public class LiveKitController {

    private final LiveKitService liveKitService;

    @GetMapping("/token")
    public ResponseEntity<LiveKitAccessTokenResponse> getAccessToken(
        final Member member,
        @RequestParam final String roomName
    ) {
        final String token = liveKitService.getAccessToken(member, roomName);
        final LiveKitAccessTokenResponse response = new LiveKitAccessTokenResponse(token);
        return ResponseEntity.ok(response);
    }
}
