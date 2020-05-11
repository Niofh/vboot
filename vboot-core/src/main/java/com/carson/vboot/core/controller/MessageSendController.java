package com.carson.vboot.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.carson.vboot.core.base.VBootController;
import com.carson.vboot.core.base.VbootService;
import com.carson.vboot.core.bo.PageBo;
import com.carson.vboot.core.common.utils.ResultUtil;
import com.carson.vboot.core.entity.MessageSend;
import com.carson.vboot.core.service.MessageSendService;
import com.carson.vboot.core.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author oufuhua
 */
@Api(tags = "消息接受者")
@RestController
@Slf4j
@RequestMapping("/messageSend")
public class MessageSendController extends VBootController< MessageSend> {

    @Autowired
    private MessageSendService messageSendService;

    /**
     * 获取service
     *
     * @return
     */
    @Override
    public VbootService< MessageSend> getService() {
        return messageSendService;
    }

    @RequestMapping(value = "/getMessageSendByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage< MessageSend>> getUserByPage(@Valid PageBo pageBo, MessageSend messageSend) {

        return ResultUtil.data(messageSendService.getMessageSendByPage(pageBo, messageSend));
    }

}
