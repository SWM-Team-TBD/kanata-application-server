package moe.kanata.room;

import io.livekit.server.*;
import livekit.LivekitAgentDispatch;
import livekit.LivekitRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.kanata.global.exception.HttpResponseException;
import moe.kanata.member.domain.Member;
import moe.kanata.member.domain.MemberReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {

    @Value("${livekit.api.key}")
    private String apiKey;

    @Value("${livekit.api.secret}")
    private String secret;

    private final MemberReader memberReader;
    private final CharacterRepository characterRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public String createRoom(
        final String email,
        final String characterName
    ) {
        final UUID roomUUID = UUID.randomUUID();
        final Character character = characterRepository.findByName(characterName)
            .orElseThrow(() -> HttpResponseException
                .notFound(String.format("캐릭터 '%s'을(를) 찾을 수 없습니다.", characterName)));
        final LocalDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
            .toLocalDateTime();
        final Member member = memberReader.readByEmail(email);
        final Room room = Room.builder()
            .uuid(roomUUID)
            .character(character)
            .member(member)
            .startTime(now)
            .build();
        roomRepository.save(room);
        log.debug("방 생성 완료: 방 UUID={}, 캐릭터={}, 멤버={}", roomUUID, character.getName(), member.getName());
        return createRoomJoinToken(member, roomUUID.toString());
    }

    private String createRoomJoinToken(
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
            .addAgents(agentDispatch)
            .build();
        token.setRoomConfiguration(roomConfig);

        final String jwt = token.toJwt();

        log.debug("방 입장 토큰 생성 완료: 멤버={}, 방 이름={}, 토큰={}", member.getName(), roomName, jwt);

        return jwt;
    }
}
