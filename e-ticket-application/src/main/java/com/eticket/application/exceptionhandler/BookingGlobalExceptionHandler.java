package com.eticket.application.exceptionhandler;

import com.eticket.application.api.dto.BaseResponse;
import com.eticket.domain.exception.BookingBusinessError;
import com.eticket.domain.exception.BookingErrors;
import com.eticket.domain.exception.BookingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseResponse<Void>> handleIllegalArgumentException(IllegalArgumentException exception, HttpServletRequest request) {
        BookingBusinessError errorCode = BookingErrors.INVALID_PARAMETERS;
        BaseResponse<Void> data = BaseResponse.ofFailed(errorCode, exception.getMessage());
        HttpStatus status = errorCode.getHttpStatus();
        return new ResponseEntity<>(data, status);
    }
}
