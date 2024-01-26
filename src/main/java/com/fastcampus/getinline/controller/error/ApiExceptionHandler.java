package com.fastcampus.getinline.controller.error;

import com.fastcampus.getinline.constant.ErrorCode;
import com.fastcampus.getinline.dto.APIErrorResponse;
import com.fastcampus.getinline.exception.GeneralException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice(annotations = {RestController.class, RepositoryRestController.class})
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> validation(ConstraintViolationException e, WebRequest request) {
        return handleExceptionInternal(e, ErrorCode.VALIDATION_ERROR, HttpHeaders.EMPTY, request);
    }

    @ExceptionHandler
    public ResponseEntity<Object> general(GeneralException e, WebRequest request) {
        return handleExceptionInternal(e, e.getErrorCode(), HttpHeaders.EMPTY, request);
    }

    @ExceptionHandler
    public ResponseEntity<Object> exception(Exception e, WebRequest request) {
        return handleExceptionInternal(e, ErrorCode.INTERNAL_ERROR, HttpHeaders.EMPTY, request);
    }


    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        ErrorCode errorCode = statusCode.is4xxClientError() ?
                ErrorCode.SPRING_BAD_REQUEST :
                ErrorCode.SPRING_INTERNAL_ERROR;
        return handleExceptionInternal(ex, errorCode, headers, request);
    }

    private ResponseEntity<Object> handleExceptionInternal(Exception e, ErrorCode errorCode, HttpHeaders headers, WebRequest request) {
        return super.handleExceptionInternal(
                e,
                APIErrorResponse.of(false, errorCode.getCode(), errorCode.getMessage(e)),
                headers,
                errorCode.getHttpStatus(),
                request
        );
    }

}


