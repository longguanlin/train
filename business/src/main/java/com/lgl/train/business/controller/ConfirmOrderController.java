package com.lgl.train.business.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.lgl.train.business.req.ConfirmOrderDoReq;
import com.lgl.train.business.service.BeforeConfirmOrderService;
import com.lgl.train.business.service.ConfirmOrderService;
import com.lgl.train.common.exception.BusinessException;
import com.lgl.train.common.exception.BusinessExceptionEnum;
import com.lgl.train.common.resp.CommonResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/confirm-order")
public class ConfirmOrderController {

    private static final Logger LOG = LoggerFactory.getLogger(ConfirmOrderController.class);

    @Resource
    private BeforeConfirmOrderService beforeConfirmOrderService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${spring.profiles.active}")
    private String env;

    @Resource
    private ConfirmOrderService confirmOrderService;

    // 接口的资源名称不要和接口路径一致，会导致限流后走不到降级方法中
    @SentinelResource(value = "confirmOrderDo", blockHandler = "doConfirmBlock")
    @PostMapping("/do")
    public CommonResp<Object> doConfirm(@Valid @RequestBody ConfirmOrderDoReq req) {
//        if (!env.equals("dev")) {//使用jmeter做压测时需要注释掉
//            // 图形验证码校验
//            String imageCodeToken = req.getImageCodeToken();
//            String imageCode = req.getImageCode();
//            String imageCodeRedis = redisTemplate.opsForValue().get(imageCodeToken);
//            LOG.info("从redis中获取到的验证码：{}", imageCodeRedis);
//            if (ObjectUtils.isEmpty(imageCodeRedis)) {
//                return new CommonResp<>(false, "验证码已过期", null);
//            }
//            // 验证码校验，大小写忽略，提升体验，比如Oo Vv Ww容易混
//            if (!imageCodeRedis.equalsIgnoreCase(imageCode)) {
//                return new CommonResp<>(false, "验证码不正确", null);
//            } else {
//                // 验证通过后，移除验证码
//                redisTemplate.delete(imageCodeToken);
//            }
//        }

        Long id = beforeConfirmOrderService.beforeDoConfirm(req);
        return new CommonResp<>(String.valueOf(id));

    }

    @GetMapping("/query-line-count/{id}")
    public CommonResp<Integer> queryLineCount(@PathVariable Long id) {
        Integer count = confirmOrderService.queryLineCount(id);
        return new CommonResp<>(count);
    }

    @GetMapping("/cancel/{id}")
    public CommonResp<Integer> cancel(@PathVariable Long id) {
        Integer count = confirmOrderService.cancel(id);
        return new CommonResp<>(count);
    }

    /**
     * 降级方法，需包含限流方法的所有参数和BlockException参数
     * @param req
     * @param e
     */
    public CommonResp<Object> doConfirmBlock(ConfirmOrderDoReq req, BlockException e) {
        LOG.info("购票请求被限流：{}", req);
        throw new BusinessException(BusinessExceptionEnum.CONFIRM_ORDER_FLOW_EXCEPTION);
    }
}
