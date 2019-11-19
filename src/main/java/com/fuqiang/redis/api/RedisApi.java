package com.fuqiang.redis.api;

import com.fuqiang.redis.common.BaseController;
import com.fuqiang.redis.common.ResultResponse;
import com.fuqiang.redis.model.BloomFilterParam;
import com.fuqiang.redis.model.Test;
import com.fuqiang.redis.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


/**
 * <p>Title: WechatErrorApi</p>
 * <p>Description: WechatErrorApi</p>
 * <p>Copyright: Xi An BestTop Technologies, ltd. Copyright(c) 2018/p>
 *
 * @author Fuqiang
 * @version 0.0.0.1
 * <pre>Histroy:
 *       2019/11/16 0016 9:57 Create by Fuqiang
 * </pre>
 */
@RestController
@RequestMapping("/test")
public class RedisApi extends BaseController {

    @Autowired
    private TestService testService;

    /**
     * @methodName inserts
     * @description TODO     数据插入
     * @param test
     * @return com.fuqiang.redis.common.ResultResponse
     * @author Fu Qiang
     * @date 2019/11/19 0019 18:18
     */
    @PostMapping("/insert")
    public ResultResponse inserts(@RequestBody Test test) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        test.setId(uuid);
        testService.inserts(test);
        return okResponse();
    }

    /**
     * @methodName find
     * @description TODO     添加空对象，预防redis穿透
     * @param test
     * @return com.fuqiang.redis.common.ResultResponse
     * @author Fu Qiang
     * @date 2019/11/19 0019 18:18
     */
    @PostMapping("/find")
    public ResultResponse find(@RequestBody Test test) {
        return okResponse(testService.findById(test.getId()));
    }

    /**
     * @methodName finds
     * @description TODO     使用布隆过滤器，预防redis穿透
     * @param param
     * @return com.fuqiang.redis.common.ResultResponse
     * @author Fu Qiang
     * @date 2019/11/19 0019 18:19
     */
    @PostMapping("/finds")
    public ResultResponse finds(@RequestBody BloomFilterParam param) {
        return okResponse(testService.findByIds(param.getList()));
    }
}
