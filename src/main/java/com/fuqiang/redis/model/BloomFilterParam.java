package com.fuqiang.redis.model;

import lombok.Data;

import java.util.List;

/**
 * <p>Title: BloomFilterParam</p>
 * <p>Description: BloomFilterParam</p>
 * <p>Copyright: Xi An BestTop Technologies, ltd. Copyright(c) 2018/p>
 *
 * @author Fuqiang
 * @version 0.0.0.1
 * <pre>Histroy:
 *       2019/11/19 0019 17:33 Create by Fuqiang
 * </pre>
 */
@Data
public class BloomFilterParam {

    private List<String> list;
}
