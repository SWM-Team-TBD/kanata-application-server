package moe.kanata.room;

import io.swagger.v3.oas.annotations.media.Schema;

public record RoomJoinTokenResponse(
    @Schema(description = "방 입장 토큰") String token
) {
}
