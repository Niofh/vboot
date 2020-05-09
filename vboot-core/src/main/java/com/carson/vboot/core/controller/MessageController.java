package com.carson.vboot.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.carson.vboot.core.base.VBootController;
import com.carson.vboot.core.base.VbootService;
import com.carson.vboot.core.bo.PageBo;
import com.carson.vboot.core.common.utils.ResultUtil;
import com.carson.vboot.core.entity.Message;
import com.carson.vboot.core.service.MessageService;
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
@Api(tags = "消息表")
@RestController
@Slf4j
@RequestMapping("/message")
public class MessageController extends VBootController<Message> {

    @Autowired
    private MessageService messageService;

    /**
     * 获取service
     *
     * @return
     */
    @Override
    public VbootService<Message> getService() {
        return messageService;
    }

    @RequestMapping(value = "/getMessageByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<Message>> getUserByPage(@Valid PageBo pageBo, Message message) {

        return ResultUtil.data(messageService.getMessageByPage(pageBo, message));
    }

}
