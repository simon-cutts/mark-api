package com.sighware.mark.server.error;

public class LockFailedException extends Exception {

    public LockFailedException(String message) {
        super(message);
    }
}
