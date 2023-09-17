package com.zhuang.kill.utils;

import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

public class JSONUtil {
    private static GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();

    // entity 转 json字符串
    public static byte[] toJson(Object object) {
        return jsonRedisSerializer.serialize(object);
    }

    //json 字符串转 entity
    public static <T> T fromJson(byte[] bytes, Class<T> T) {
        return jsonRedisSerializer.deserialize(bytes, T);
    }
}
