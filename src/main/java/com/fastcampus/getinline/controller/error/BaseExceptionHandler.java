package com.fastcampus.getinline.controller.error;

import com.fastcampus.getinline.constant.ErrorCode;
import com.fastcampus.getinline.exception.GeneralException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;

@Slf4j
@ControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler
    public ModelAndView general(GeneralException e) {
        ErrorCode errorCode = e.getErrorCode();

        return new ModelAndView(
                "error",
                Map.of(
                        "statusCode", errorCode.getHttpStatus().value(),
                        "errorCode", errorCode,
                        "message", errorCode.getMessage()
                ),
                errorCode.getHttpStatus()
        );
    }

    @ExceptionHandler(value = {NoResourceFoundException.class, NoHandlerFoundException.class})
    public ModelAndView handle404Error(Exception e) {
        ErrorCode errorCode = ErrorCode.NOT_FOUND;
        HttpStatus status = HttpStatus.NOT_FOUND;

        return new ModelAndView(
                "error",
                Map.of(
                        "statusCode", status.value(),
                        "errorCode", errorCode,
                        "message", errorCode.getMessage()
                ),
                status
        );
    }


    @ExceptionHandler
    public ModelAndView exception(Exception e) {
        ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;

        return new ModelAndView(
                "error",
                Map.of(
                        "statusCode", errorCode.getHttpStatus().value(),
                        "errorCode", errorCode,
                        "message", errorCode.getMessage(e)
                ),
                errorCode.getHttpStatus()
        );
    }

}
