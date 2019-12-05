package com.sighware.mark.server.error;

public class RegistrationNumberNotFoundException extends Exception {
    public RegistrationNumberNotFoundException() {
    }

    public RegistrationNumberNotFoundException(String message) {
        super(message);
    }

    public RegistrationNumberNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegistrationNumberNotFoundException(Throwable cause) {
        super(cause);
    }

    public RegistrationNumberNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
