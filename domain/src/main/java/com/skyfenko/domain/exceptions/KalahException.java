package com.skyfenko.domain.exceptions;

/**
 * Root of application exception hierarchy
 *
 * @author Stanislav Kyfenko
 */
public class KalahException extends RuntimeException {

    public KalahException(String message) {
        super(message);
    }
}
