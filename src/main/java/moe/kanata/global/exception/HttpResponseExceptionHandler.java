package moe.kanata.global.exception;

import lombok.extern.slf4j.Slf4j;
import moe.kanata.auth.api.MessageResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class HttpResponseExceptionHandler {

    @ExceptionHandler(HttpResponseException.class)
    public ResponseEntity<MessageResponse> handle(final HttpResponseException exception) {
        final HttpStatus status = exception.getStatus();
        final String message = exception.getMessage();
        log.debug("Handling HttpResponseException: status={}, message={}", status, message);
        return ResponseEntity
            .status(status)
            .body(new MessageResponse(message));
    }
}
