package moe.kanata.auth.api;

import moe.kanata.global.exception.HttpResponseException;
import org.springframework.http.HttpStatus;

public class TokenException extends HttpResponseException {

    public TokenException(final String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
