package com.ant.mall.product;

import com.ant.mall.product.entity.BrandEntity;
import com.ant.mall.product.service.BrandService;
import com.ant.mall.product.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Arrays;
import java.util.List;

@Slf4j
@SpringBootTest
class MallProductApplicationTests {

    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void redisson(){
        System.out.println(redissonClient);
    }

    @Test
    public void testFindPath(){
        Long[] catelogPath = categoryService.findCatelogPath(225L);
        log.info("ÍêÕûÂ·¾¶£º{}", Arrays.asList(catelogPath));
    }

    @Test
    void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1L));
        list.forEach((x) -> {
            System.out.println(x);
        });
    }

    @Test
    public void redisTest(){
        ListOperations<String, String> list = redisTemplate.opsForList();
        list.leftPush("demo","hello");
        list.leftPush("demo","world");
        list.leftPush("demo","java");
        System.err.print(list.leftPop("demo"));
    }

    @Test
    void testSema(){
        //redisTemplate.opsForValue().set("sema","100");
        RSemaphore semaphore = redissonClient.getSemaphore("sema1");
        semaphore.trySetPermits(50);
    }
}
