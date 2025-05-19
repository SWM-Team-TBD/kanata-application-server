package moe.kanata.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class KanataException extends RuntimeException {

    private final HttpStatus status;
    private final String message;

    public KanataException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }
}
