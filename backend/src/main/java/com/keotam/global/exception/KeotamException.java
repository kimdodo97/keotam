package com.keotam.global.exception;

import java.util.HashMap;
import java.util.Map;

public abstract class KeotamException extends RuntimeException{
    public final Map<String, String> validation = new HashMap<>();

    public KeotamException(String message){
        super(message);
    }

    public KeotamException(String message, Throwable cause){
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message){
        validation.put(fieldName, message);
    }
}
