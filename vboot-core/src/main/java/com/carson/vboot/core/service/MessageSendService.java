package com.carson.vboot.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.carson.vboot.core.base.VbootService;
import com.carson.vboot.core.bo.PageBo;
import com.carson.vboot.core.entity.MessageSend;

public interface MessageSendService extends VbootService<MessageSend> {

    IPage<MessageSend> getMessageSendByPage(PageBo pageBo, MessageSend messageSend);

}
