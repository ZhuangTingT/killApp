package com.zhuang.kill.service.impl;

import com.zhuang.kill.config.BloomFilterHelper;
import com.zhuang.kill.config.RedisBloomFilter;
import com.zhuang.kill.config.RedisConfig;
import com.zhuang.kill.entity.KillItem;
import com.zhuang.kill.entity.KillItemDetailVO;
import com.zhuang.kill.entity.KillItemVO;
import com.zhuang.kill.mapper.KillItemMapper;
import com.zhuang.kill.service.KillItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 秒杀商品表 服务实现类
 * </p>
 *
 * @author ztt
 * @since 2023-07-18
 */
@Service
public class KillItemServiceImpl extends ServiceImpl<KillItemMapper, KillItem> implements KillItemService {
    @Autowired
    private BloomFilterHelper bloomFilterHelper;

    @Autowired
    RedisBloomFilter redisBloomFilter;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    private KillItemMapper killItemMapper;

    @Override
    public List<KillItemVO> listKillItemVO() {
        return killItemMapper.listKillItemVO();
    }

    @Override
    public List<KillItemDetailVO> listKillItemDetailVO() {
        return killItemMapper.listKillItemDetailVO();
    }

    @Override
    public KillItemDetailVO getKillItemDetailVOByKillId(Long killId) {
        KillItemDetailVO killItemDetailVO = null;
        // 检查布隆过滤器
        boolean bloomCheck = redisBloomFilter.includeByBloomFilter(bloomFilterHelper, RedisConfig.KILLITEM_BLOOM_NAME, killId.toString());
        if(bloomCheck == true) {
            // 尝试redis缓存获取
            killItemDetailVO = (KillItemDetailVO) redisTemplate.opsForValue().get(RedisConfig.KILLITEMDETAILVO_PREFIX+killId);
            // redis缓存没有，查询数据库
            if(killItemDetailVO == null) {
                System.out.println("查看数据库");
                killItemDetailVO = killItemMapper.getKillItemDetailVOByKillId(killId);
                // 补充到redis缓存
                redisTemplate.opsForValue().set(RedisConfig.KILLITEMDETAILVO_PREFIX + killId, killItemDetailVO);
                stringRedisTemplate.opsForValue().set(RedisConfig.KILLITEM_STOCK_PREFIX + killId, String.valueOf(killItemDetailVO.getStockCount()));
            }
            killItemDetailVO.setStockCount(Integer.valueOf(stringRedisTemplate.opsForValue().get(RedisConfig.KILLITEM_STOCK_PREFIX + killId)));
        }

        return killItemDetailVO;
    }

    @Override
    public KillItem getKillItemByKillId(Long killId) {
        KillItem killItem = null;
        // 检查布隆过滤器
        boolean bloomCheck = redisBloomFilter.includeByBloomFilter(bloomFilterHelper, RedisConfig.KILLITEM_BLOOM_NAME, killId.toString());
        if(bloomCheck == true) {
            // 尝试redis缓存获取
            killItem = (KillItem) redisTemplate.opsForValue().get(RedisConfig.KILLITEM_PREFIX+killId);
            // redis缓存没有，查询数据库
            if(killItem == null) {
                killItem = killItemMapper.getKillItemByKillId(killId);
                // 补充到redis缓存
                redisTemplate.opsForValue().set(RedisConfig.KILLITEM_PREFIX+killId, killItem);
            }
            killItem.setStockCount(Integer.valueOf(stringRedisTemplate.opsForValue().get(RedisConfig.KILLITEM_STOCK_PREFIX + killId)));
        }
        return killItem;
    }

    @Override
    public boolean decreaseStock(Long killId, Integer amount) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("killId", killId);
        map.put("amount", amount);
        if(killItemMapper.decreaseStock(map) > 0)
            return true;
        else
            return false;
    }

    @Override
    public int insert(KillItem killItem) {
        int result = killItemMapper.insert(killItem);
        // 先插入数据库，再用数据库自增主键进行布隆过滤
        redisBloomFilter.addByBloomFilter(bloomFilterHelper, RedisConfig.KILLITEM_BLOOM_NAME, killItem.getKillId().toString());
        // 加入redis缓存
        redisTemplate.opsForValue().set(RedisConfig.KILLITEM_PREFIX+killItem.getKillId(), killItem);
        stringRedisTemplate.opsForValue().set(RedisConfig.KILLITEM_STOCK_PREFIX + killItem.getKillId(), String.valueOf(killItem.getStockCount()));
        return result;
    }
}
