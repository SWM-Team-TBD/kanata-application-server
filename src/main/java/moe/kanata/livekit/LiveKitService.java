package moe.kanata.livekit;

import io.livekit.server.*;
import livekit.LivekitAgentDispatch;
import livekit.LivekitRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.kanata.member.domain.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LiveKitService {

    @Value("${livekit.api.key}")
    private String apiKey;

    @Value("${livekit.api.secret}")
    private String secret;

    public String getAccessToken(
        final Member member,
        final String roomName
    ) {
        final AccessToken token = new AccessToken(apiKey, secret);

        token.setName(member.getName());
        token.setIdentity(member.getName());
        token.addGrants(
            new RoomCreate(false),
            new RoomJoin(true),
            new RoomName(roomName),
            new CanPublish(true),
            new CanSubscribe(true)
        );

        final LivekitAgentDispatch.RoomAgentDispatch agentDispatch = LivekitAgentDispatch.RoomAgentDispatch.newBuilder()
            .setAgentName(member.getName())
            .build();
        final LivekitRoom.RoomConfiguration roomConfig = LivekitRoom.RoomConfiguration.newBuilder()
            .setAgents(0, agentDispatch)
            .build();
        token.setRoomConfiguration(roomConfig);

        log.debug("🎤 [{}]에게 [{}]에 대한 Access Token 생성 완료", member.getName(), roomName);

        return token.toJwt();
    }
}
