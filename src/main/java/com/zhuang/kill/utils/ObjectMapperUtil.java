package com.zhuang.kill.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

public class ObjectMapperUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    // entity 转 json字符串
    public static String toStr(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    //json 字符串转 entity
    public static <T> T fromStr(String string, Class<T> T) {
        return objectMapper.convertValue(string, T);
    }
}
