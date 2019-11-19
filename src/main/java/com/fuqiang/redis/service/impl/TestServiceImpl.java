package com.fuqiang.redis.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.besttop.mybatiscommon.service.impl.IBaseServiceImpl;
import com.fuqiang.redis.mapper.TestMapper;
import com.fuqiang.redis.model.Test;
import com.fuqiang.redis.service.TestService;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.awt.CharsetString;

import javax.management.Query;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Title: WechatErrorServiceImpl</p>
 * <p>Description: WechatErrorServiceImpl</p>
 * <p>Copyright: Xi An BestTop Technologies, ltd. Copyright(c) 2018/p>
 *
 * @author Fuqiang
 * @version 0.0.0.1
 * <pre>Histroy:
 *       2019/11/16 0016 9:56 Create by Fuqiang
 * </pre>
 */
@Service
@Slf4j
public class TestServiceImpl extends IBaseServiceImpl<TestMapper, Test> implements TestService {

    // 初始化一个能够容纳10000个元素且容错率为0.01布隆过滤器
    private static final BloomFilter<CharSequence> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.forName("UTF-8")), 10000, 0.01);

    @Autowired
    private TestMapper testMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    @Transactional
    public void inserts(Test test) {
        testMapper.insert(test);
        log.info("数据库插入成功");
        redisTemplate.opsForValue().set(test.getId(), JSON.toJSONString(test));
        log.info("redis插入成功");
    }

    /**
     * @methodName findById
     * @description TODO     缓存添加空对象预防redis穿透
     * @param id
     * @return com.fuqiang.redis.model.Test
     * @author Fu Qiang
     * @date 2019/11/19 0019 16:48
     */
    @Override
    public Test findById(String id) {
        // 整层逻辑进行加锁，多个请求过来之后，只给一个请求执行权限，进行数据缓存，然后下一个请求直接查询redis，降低高并发时redis穿透
        synchronized (Test.class) {
            // 先从redis取值
            Object obj = redisTemplate.opsForValue().get(id);
            // redis没读到数据
            if (obj == null) {
                // MySQL取值
                Test test1 = testMapper.selectById(id);
                log.info("MySQL获取...");
                // 然后更新redis缓存。MySQL也没有取到值，也要更新缓存，存空串进去，避免下次再去查询MySQL -->避免高并发时候redis穿透
                redisTemplate.opsForValue().set(id, test1 == null ? "" : JSON.toJSONString(test1));
                return test1;
            }
            Test test = JSONObject.parseObject(obj.toString(), Test.class);
            log.info("redis获取...");
            return test;
        }
    }

    /**
     * @methodName findByIds
     * @description TODO     使用布隆过滤器预防redis穿透
     *                       1>全查MySQL表，把id放入布隆器。
     *                       2>传进来的id进行布隆器比对，要是布隆器存在，则执行逻辑先查redis，再查MySQL。
     *                         要是布隆器不存在该id，直接舍弃，不用走任何逻辑代码，这样就可以避免无效人肉id攻击MySQL，从而实现预防redis穿透
     * @param ids
     * @return java.util.List<com.fuqiang.redis.model.Test>
     * @author Fu Qiang
     * @date 2019/11/19 0019 18:13
     */
    @Override
    public List<Test> findByIds(List<String> ids) {
        // 全查MySQL，把id放入布隆器
        List<Test> tests = testMapper.selectList(null);
        tests.stream().forEach(test -> {
            bloomFilter.put(test.getId());
        });
        ArrayList<Test> list = new ArrayList<>();
        ids.forEach(id -> {
            if (bloomFilter.mightContain(id)) {
                // 先从redis取值
                Object obj = redisTemplate.opsForValue().get(id);
                // redis没读到数据
                if (obj == null) {
                    // MySQL取值
                    Test test1 = testMapper.selectById(id);
                    log.info("MySQL获取...");
                    // 然后更新redis缓存。MySQL也没有取到值，也要更新缓存，存空串进去，避免下次再去查询MySQL -->避免高并发时候redis穿透
                    redisTemplate.opsForValue().set(id, test1 == null ? "" : JSON.toJSONString(test1));
                    list.add(test1);
                }
                Test test = JSONObject.parseObject(obj.toString(), Test.class);
                log.info("redis获取...");
                list.add(test);
            }
            log.info("id不合法，直接丢弃");
        });
        return list;
    }

}
