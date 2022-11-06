package com.eticket.application.exceptionhandler;

import com.eticket.application.api.dto.BaseResponse;
import com.eticket.application.api.dto.FieldViolation;
import com.eticket.domain.exception.BookingBusinessError;
import com.eticket.domain.exception.BookingErrors;
import com.eticket.domain.exception.BookingException;
import com.google.common.base.CaseFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

public class BookingGlobalExceptionHandler {

    @ExceptionHandler(BookingException.class)
    public ResponseEntity<BaseResponse<Void>> handleBusinessException(BookingException exception) {
        BaseResponse<Void> data = BaseResponse.ofFailed(exception);
        return new ResponseEntity<>(data, exception.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Void>> handleException(Exception exception, HttpServletRequest request) {
        BookingBusinessError errorCode = BookingErrors.INTERNAL_SERVER_ERROR;
        BaseResponse<Void> data = BaseResponse.ofFailed(errorCode, exception.getMessage());
        HttpStatus status = errorCode.getHttpStatus();
        return new ResponseEntity<>(data, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Void>> handleRuntimeException(RuntimeException exception, HttpServletRequest request) {
        BookingBusinessError errorCode = BookingErrors.INTERNAL_SERVER_ERROR;
        BaseResponse<Void> data = BaseResponse.ofFailed(errorCode, exception.getMessage());
        HttpStatus status = errorCode.getHttpStatus();
        return new ResponseEntity<>(data, status);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseResponse<Void>> handleIllegalArgumentException(IllegalArgumentException exception, HttpServletRequest request) {
        BookingBusinessError errorCode = BookingErrors.INVALID_PARAMETERS;
        BaseResponse<Void> data = BaseResponse.ofFailed(errorCode, exception.getMessage());
        HttpStatus status = errorCode.getHttpStatus();
        return new ResponseEntity<>(data, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        List<FieldViolation> errors = exception.getBindingResult().getFieldErrors().stream()
                .map(e -> new FieldViolation(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, e.getField()), e.getDefaultMessage()))
                .collect(Collectors.toList());
        BookingBusinessError errorCode = BookingErrors.INVALID_PARAMETERS;
        BaseResponse<Void> data = BaseResponse.ofFailed(errorCode, "Invalid parameters of object: " + exception.getBindingResult().getObjectName(), errors);
        HttpStatus status = errorCode.getHttpStatus();
        return new ResponseEntity<>(data, status);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<BaseResponse<Void>> handleAuthenticationException(AuthenticationException exception, HttpServletRequest request) {
        BookingBusinessError errorCode = BookingErrors.FORBIDDEN_ERROR;
        BaseResponse<Void> data = BaseResponse.ofFailed(errorCode, exception.getMessage());
        HttpStatus status = errorCode.getHttpStatus();
        return new ResponseEntity<>(data, status);
    }
}
