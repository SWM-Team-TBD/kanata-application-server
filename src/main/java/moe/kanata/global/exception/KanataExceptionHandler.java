package moe.kanata.global.exception;

import lombok.extern.slf4j.Slf4j;
import moe.kanata.auth.api.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class KanataExceptionHandler {

    @ExceptionHandler(KanataException.class)
    public ResponseEntity<MessageResponse> handle(final KanataException exception) {
        final HttpStatus status = exception.getStatus();
        final String message = exception.getMessage();
        return ResponseEntity
            .status(status)
            .body(new MessageResponse(message));
    }
}
