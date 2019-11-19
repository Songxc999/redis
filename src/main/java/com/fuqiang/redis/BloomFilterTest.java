package com.fuqiang.redis;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

/**
 * <p>Title: BloomFilterTest</p>
 * <p>Description: BloomFilterTest</p>
 * <p>Copyright: Xi An BestTop Technologies, ltd. Copyright(c) 2018/p>
 *
 * @author Fuqiang
 * @version 0.0.0.1
 * <pre>Histroy:
 *       2019/11/19 0019 16:30 Create by Fuqiang
 * </pre>
 */
public class BloomFilterTest {

    // 初始化一个能够容纳10000个元素且容错率为0.01布隆过滤器
    private static final BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), 10000, 0.01);

    /**
     * 初始化布隆过滤器
     */
    private static void init() {
        // 初始化10000个合法Id并加入到过滤器中
        for (int legalId = 0; legalId < 100; legalId++) {
            bloomFilter.put(legalId);
        }
    }

    public static void main(String[] args) {
        // 初始化过滤器
        init();
        // 误判个数
        int errorNum=0;
        // 验证从10000个非法id是否有效
        for (int id = 100; id < 200; id++) {
            // id在集合中返回true
            if (bloomFilter.mightContain(id)){
                // 误判数
                errorNum++;
            }
            System.out.println("当前id为" + id);
        }
        System.out.println("误判数量 : " + errorNum);
    }
}
