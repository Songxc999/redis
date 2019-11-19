package com.fuqiang.redis.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.besttop.mybatiscommon.service.IBaseService;
import com.fuqiang.redis.model.Test;

import java.util.List;

/**
 * <p>Title: WechatErrorService</p>
 * <p>Description: WechatErrorService</p>
 * <p>Copyright: Xi An BestTop Technologies, ltd. Copyright(c) 2018/p>
 *
 * @author Fuqiang
 * @version 0.0.0.1
 * <pre>Histroy:
 *       2019/11/16 0016 9:54 Create by Fuqiang
 * </pre>
 */
public interface TestService extends IBaseService<Test> {

    void inserts(Test wechatError);

    Test findById(String id);

    List<Test> findByIds(List<String> ids);
}
