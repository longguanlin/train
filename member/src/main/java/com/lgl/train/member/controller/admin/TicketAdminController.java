package com.lgl.train.member.controller.admin;

import com.lgl.train.common.resp.CommonResp;
import com.lgl.train.common.resp.PageResp;
import com.lgl.train.member.req.TicketQueryReq;
import com.lgl.train.member.resp.TicketQueryResp;
import com.lgl.train.member.service.TicketService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/ticket")
public class TicketAdminController {

    @Resource
    private TicketService ticketService;

    @GetMapping("/query-list")
    public CommonResp<PageResp<TicketQueryResp>> queryList(@Valid TicketQueryReq req) {
        PageResp<TicketQueryResp> list = ticketService.queryList(req);
        return new CommonResp<>(list);
    }

}
