package com.zhuang.kill.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RedisExecutor {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 访问频率限流
     *
     * @param key        key值
     * @param limitCount 容量
     * @param seconds    时间间隔
     * @return
     */
    public boolean tryAccess(String key, int limitCount, int seconds) {
        String luaScript = buildLimitLuaScript();
        RedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);

        List<String> keys = new ArrayList<>();
        keys.add(key);
        Long count = (Long) stringRedisTemplate.execute(redisScript, keys, String.valueOf(limitCount), String.valueOf(seconds));
        if (count != 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 接口限流lua脚本
     * @return
     */
    private static final String buildLimitLuaScript() {
        // KEYS[1]: ip+url
        // ARGV[1]: 单位时间限制的访问次数
        // ARGV[2]: 单位时间（毫秒）
        String lua = "local val = redis.call('incr', KEYS[1]) " +
                "if val == 1 then" +
                " redis.call('expire', KEYS[1], tonumber(ARGV[2])) " +
                "end " +
                "if val > tonumber(ARGV[1]) then" +
                " return 0 " +
                "end " +
                "return 1";
        return lua;
    }

    public Boolean decreaseStock(Long killId, Integer amount) {
        String luaScript = buildDecreaseStockLuaScript();
        RedisScript<Boolean> redisScript = new DefaultRedisScript<>(luaScript, Boolean.class);
        List<String> keys = new ArrayList<>();
        keys.add(RedisConfig.KILLITEM_STOCK_PREFIX + killId);
//        System.out.println(RedisConst.KILLITEM_STOCK_PREFIX + killId);
        return (Boolean) stringRedisTemplate.execute(redisScript, keys, String.valueOf(amount));
    }

    /**
     * 删减redis库存lua脚本
     * @return
     */
    private static final String buildDecreaseStockLuaScript() {
        // KEYS[1]: killId
        // ARGV[1]: 购买数量
        String lua = "local key = KEYS[1];" +
                "local amount = tonumber(ARGV[1]);" +
                "local stock = tonumber(redis.call('get',key) or '0');" +
                "if (stock - amount > -1) then" +
                " redis.call('decrby', KEYS[1], amount);" +
                " return true;" +
                "else" +
                " return false;" +
                "end;";
        return lua;
    }
}
