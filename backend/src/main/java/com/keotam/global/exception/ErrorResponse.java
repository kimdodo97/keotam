package com.keotam.global.exception;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ErrorResponse {
    private final String code;
    private final String message;
    private final Map<String,String> validation;

    @Builder
    public ErrorResponse(String code, String message, Map<String, String> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation;
    }

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
        this.validation = new HashMap<>();
    }

    public void addValidation(String fieldName, String message){
        this.validation.put(fieldName, message);
    }
}
