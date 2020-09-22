package com.rakuten.buildcharacterbackend.api.v1.exception;

import lombok.Getter;

@Getter
public class TTlOutOfRangeException extends RuntimeException{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public TTlOutOfRangeException(String message ) {
        super(message);
    }
    
}