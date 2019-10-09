package com.rokuality.core.exceptions;

public class SessionNotStartedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SessionNotStartedException(String inText) {
        super(inText);
    }
  
}
