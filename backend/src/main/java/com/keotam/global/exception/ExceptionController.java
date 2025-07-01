package com.keotam.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Configuration
public class ExceptionController {
    @ExceptionHandler(KeotamException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> bidGoodException(KeotamException ex) {
        int statusCode = ex.getStatusCode();
        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(ex.getMessage())
                .validation(ex.validation)
                .build();

        return ResponseEntity.status(statusCode)
                .body(body);
    }
}
