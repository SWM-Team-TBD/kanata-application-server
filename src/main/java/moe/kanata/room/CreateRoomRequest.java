package moe.kanata.room;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateRoomRequest(
    @Schema(description = "대화할 캐릭터 이름") String character
) {
}
