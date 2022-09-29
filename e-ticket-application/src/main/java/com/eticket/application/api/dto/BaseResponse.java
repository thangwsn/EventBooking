package com.eticket.application.api.dto;

import com.eticket.domain.exception.BookingBusinessError;
import com.eticket.domain.exception.BookingException;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Slf4j
public class BaseResponse<T> {
    public static final Integer OK_CODE = 200;
    public static final Integer INVALID_CODE = 100;

    private T data;
    private Metadata meta = new Metadata();

    public static <T> BaseResponse<T> ofSucceeded(T data) {
        BaseResponse<T> response = new BaseResponse<>();
        response.data = data;
        response.meta.code = OK_CODE;
        response.meta.message = "OK";
        return response;
    }

    public static <T> BaseResponse<List<T>> ofSucceeded(Page<T> data) {
        BaseResponse<List<T>> response = new BaseResponse<>();
        response.data = data.getContent();
        response.meta.code = OK_CODE;
        response.meta.page = data.getPageable().getPageNumber();
        response.meta.size = data.getPageable().getPageSize();
        response.meta.total = data.getTotalElements();
        return response;
    }

    public static <T> BaseResponse<T> ofSucceeded() {
        BaseResponse<T> response = new BaseResponse<>();
        response.meta.code = OK_CODE;
        return response;
    }

    public static <T> BaseResponse<T> ofInvalid(T data, String message) {
        BaseResponse<T> response =  new BaseResponse<T>();
        response.data = data;
        response.meta.code = OK_CODE;
        response.meta.message = message;
        return response;
    }

    public static BaseResponse<Void> ofFailed(BookingBusinessError errorCode) {
        return ofFailed(errorCode, null);
    }


    public static BaseResponse<Void> ofFailed(BookingBusinessError errorCode, String message) {
        BaseResponse<Void> response = new BaseResponse<>();
        response.meta.code = errorCode.getCode();
        response.meta.message = (message != null) ? message : errorCode.getMessage();
        return response;
    }

    public static BaseResponse<Void> ofFailed(BookingException exception) {
        return ofFailed(exception.getErrorCode(), exception.getMessage());
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Metadata {
        private Integer code;
        private Integer page;
        private Integer size;
        private Long total;
        private String message;
        private String requestId;
    }
}
