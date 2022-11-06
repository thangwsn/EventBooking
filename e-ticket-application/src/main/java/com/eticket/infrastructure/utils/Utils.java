package com.eticket.infrastructure.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Utils {
    private static ObjectMapper mapper = new ObjectMapper();

    public static <T> String convertObjectToJson(T object) {
        try {
            return (new ObjectMapper()).writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T readObjectFromJsonFormat(String json, Class<T> resultType) {
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        try {
            return (new ObjectMapper()).readValue(json, resultType);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public static String generateBookingCode() {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");
        return sdf.format(new Date()) + RandomStringUtils.randomAlphabetic(16);
    }

    public static Timestamp convertToTimeStamp(long time) {
        return new Timestamp(time);
    }

    public static long timeStampToTime(Timestamp datetime) {
        return datetime.getTime();
    }
}
