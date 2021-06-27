package com.dunzo.coffeemachine.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Slf4j
public class TransformUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T fromJson(String jsonString, Class<T> classType){
        if(StringUtils.isNotBlank(jsonString)) {
            try {
                return objectMapper.readValue(jsonString, classType);
            } catch (JsonProcessingException e) {
                log.error("Error in fromJson() while processing json string {} to class {}. Exception class {} message {}", jsonString, classType, e.getClass(), e.getMessage());
            }
        }
        return null;
    }

    public String toJson(Object object){
        if(Objects.nonNull(object)) {
            try {
                return objectMapper.writeValueAsString(object);
            } catch (JsonProcessingException e) {
                log.error("Error in toJson() while processing object {} to string. Exception class {} message {}", object, e.getClass(), e.getMessage());
            }
        }
        return null;
    }
}
