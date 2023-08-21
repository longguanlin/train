package com.lgl.train.business.controller;

import com.lgl.train.business.req.ConfirmOrderDoReq;
import com.lgl.train.business.service.ConfirmOrderService;
import com.lgl.train.common.resp.CommonResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/confirm-order")
public class ConfirmOrderController {

    @Resource
    private ConfirmOrderService confirmOrderService;

    // 接口的资源名称不要和接口路径一致，会导致限流后走不到降级方法中
    //@SentinelResource(value = "confirmOrderDo", blockHandler = "doConfirmBlock")
    @PostMapping("/do")
    public CommonResp<Object> doConfirm(@Valid @RequestBody ConfirmOrderDoReq req) {
//        if (!env.equals("dev")) {
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

//        Long id = beforeConfirmOrderService.beforeDoConfirm(req);
//        return new CommonResp<>(String.valueOf(id));

        confirmOrderService.doConfirm(req);
        return new CommonResp<>();
    }
}
