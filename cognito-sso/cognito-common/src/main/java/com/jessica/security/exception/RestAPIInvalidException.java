package com.jessica.security.exception;

import org.springframework.security.core.AuthenticationException;

@SuppressWarnings("serial")
public class RestAPIInvalidException extends AuthenticationException {
    public RestAPIInvalidException(String msg, Throwable t) {
        super(msg, t);
    }

    public RestAPIInvalidException(String msg) {
        super(msg);
    }
}
