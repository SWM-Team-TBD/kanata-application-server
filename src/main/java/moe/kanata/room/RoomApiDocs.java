package moe.kanata.room;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

@Tag(name = "방")
public interface RoomApiDocs {

    @Operation(summary = "방 생성", description = "방을 생성하고 참여할 수 있는 토큰을 반환합니다.")
    @RequestBody(
        description = "방 생성 요청",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = CreateRoomRequest.class)
        )
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "방 생성 성공한 경우, 입장 토큰을 응답",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RoomJoinTokenResponse.class)
            )),
    })
    ResponseEntity<RoomJoinTokenResponse> createRoom(UserDetails principal, CreateRoomRequest request);
}
