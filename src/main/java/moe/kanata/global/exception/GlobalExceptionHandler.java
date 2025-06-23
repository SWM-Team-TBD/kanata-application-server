package moe.kanata.global.exception;

import lombok.extern.slf4j.Slf4j;
import moe.kanata.auth.api.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(KanataException.class)
    public ResponseEntity<MessageResponse> handle(final Exception exception) {
        final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        final String message = exception.getMessage();
        log.warn("Handling KanataException: status={}, message={}", status, message);
        return ResponseEntity
            .status(status)
            .body(new MessageResponse(message));
    }
}
