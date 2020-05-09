package com.carson.vboot.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.carson.vboot.core.base.VbootService;
import com.carson.vboot.core.bo.PageBo;
import com.carson.vboot.core.entity.Message;

public interface MessageService extends VbootService<Message> {
    IPage<Message> getMessageByPage(PageBo pageBo, Message message);
}
