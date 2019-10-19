package com.rokuality.core.exceptions;

public class NotAuthorizedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NotAuthorizedException(String inText) {
        super(inText);
    }
  
}
