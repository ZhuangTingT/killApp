package com.zhuang.kill.config;

import com.zhuang.kill.entity.KillItem;
import com.zhuang.kill.entity.KillItemDetailVO;
import com.zhuang.kill.service.KillItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
public class CachePreloader {
    @Autowired
    private BloomFilterHelper bloomFilterHelper;
    @Autowired
    RedisBloomFilter redisBloomFilter;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    private KillItemService killItemService;

    @Scheduled(fixedRate = 300000) // 每5分钟执行一次,
    public void preloadCache() {
        // 获取满足缓存条件的商品信息
        List<KillItemDetailVO> list = killItemService.listKillItemDetailVO();
        Random random = new Random();
        for(KillItemDetailVO vo : list) {
            redisBloomFilter.addByBloomFilter(bloomFilterHelper, RedisConfig.KILLITEM_BLOOM_NAME, vo.getKillId().toString());
            long delTime = (vo.getEndDate().getTime()-new Date().getTime())/1000 + random.nextInt(30);
            // 加入redis缓存
            redisTemplate.opsForValue().set(RedisConfig.KILLITEMDETAILVO_PREFIX+vo.getKillId(), vo, delTime, TimeUnit.SECONDS);
            stringRedisTemplate.opsForValue().set(RedisConfig.KILLITEM_STOCK_PREFIX + vo.getKillId(), String.valueOf(vo.getStockCount()), delTime, TimeUnit.SECONDS);
        }
    }
}
