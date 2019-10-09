package com.rokuality.core.exceptions;

public class NoSuchElementException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NoSuchElementException(String inText) {
        super(inText);
    }
  
}
