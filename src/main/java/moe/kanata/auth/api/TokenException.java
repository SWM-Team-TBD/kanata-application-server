package moe.kanata.auth.api;

import moe.kanata.global.exception.KanataException;
import org.springframework.http.HttpStatus;

public class TokenException extends KanataException {

    public TokenException(final String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
