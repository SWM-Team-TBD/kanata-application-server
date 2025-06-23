package moe.kanata.global.exception;

import lombok.Getter;

@Getter
public class KanataException extends RuntimeException {

    private final String message;

    public KanataException(final String message) {
        this.message = message;
    }
}
