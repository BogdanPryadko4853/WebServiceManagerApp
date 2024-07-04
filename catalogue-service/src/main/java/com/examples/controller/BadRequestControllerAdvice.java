package com.examples.controller;


import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.validation.ObjectError;

import java.util.Locale;

@ControllerAdvice
@RequiredArgsConstructor
public class BadRequestControllerAdvice {


    private final MessageSource message;

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ProblemDetail> handleBadRequestException(BindException exception,
                                                                   Locale locale) {

        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatus.BAD_REQUEST,
                        this.message.getMessage("errors.404.title"
                        ,new Object[0],"errors.404.title",
                                locale));

        problemDetail.setProperty("errors",
                exception.getAllErrors().stream()
                        .map(ObjectError::getDefaultMessage)
                        .toList());


    return ResponseEntity.badRequest()
            .body(problemDetail);

    }

}
