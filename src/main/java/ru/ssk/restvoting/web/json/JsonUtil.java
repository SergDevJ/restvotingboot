package ru.ssk.restvoting.web.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectReader;

import java.io.IOException;
import java.util.List;

public class JsonUtil {

    public static <T> T readValue(String jsonData, Class<T> clazz) {
        try {
            return JacksonObjectMapper.getMapper().readValue(jsonData, clazz);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid read from JSON:\n'" + jsonData + "'", e);
        }
    }

    public static <T> List<T> readValues(String jsonData, Class<T> clazz) {
        ObjectReader reader = JacksonObjectMapper.getMapper().readerFor(clazz);
        try {
            return reader.<T>readValues(jsonData).readAll();
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid read array from JSON:\n'" + jsonData + "'", e);
        }
    }

    public static <T> String writeValue(T value) {
        try {
            return JacksonObjectMapper.getMapper().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Invalid write to JSON:\n'" + value + "'", e);
        }
    }
}
