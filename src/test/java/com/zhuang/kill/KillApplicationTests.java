package com.zhuang.kill;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhuang.kill.config.BloomFilterHelper;
import com.zhuang.kill.config.RedisBloomFilter;
import com.zhuang.kill.entity.KillItem;
import com.zhuang.kill.entity.KillItemDetailVO;
import com.zhuang.kill.entity.UserInfo;
import com.zhuang.kill.mapper.KillItemMapper;
import com.zhuang.kill.service.*;
import com.zhuang.kill.service.impl.KillItemServiceImpl;
import com.zhuang.kill.service.impl.RabbitmqOrderProducer;
import com.zhuang.kill.utils.ObjectMapperUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@SpringBootTest
class KillApplicationTests {

    @Autowired
    private KillItemService killItemService;
    @Autowired
    KillItemMapper killItemMapper;

    @Test
    void addKeyTest() {
        Long killId = 1L;
        Long itemId = 4L;
        BigDecimal killPrice = new BigDecimal(2.98);
        KillItem killItem = new KillItem(killId, itemId, killPrice,8, new Date(), new Date(2024-1900, 9, 1));
        killItemService.insert(killItem);
    }

    @Test
    void checkKeyTest() {
        KillItemDetailVO killItemDetailVO = killItemService.getKillItemDetailVOByKillId(10L);
    }

    @Test
    void testMsg() throws Exception {
        RabbitmqProducer rabbitmqProducer = new RabbitmqOrderProducer();
        rabbitmqProducer.publishMessageAsync("3 1");
    }

}
