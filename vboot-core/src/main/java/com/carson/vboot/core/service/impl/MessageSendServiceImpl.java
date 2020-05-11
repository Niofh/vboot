package com.carson.vboot.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carson.vboot.core.base.VbootBaseDao;
import com.carson.vboot.core.bo.PageBo;
import com.carson.vboot.core.dao.mapper.MessageSendDao;
import com.carson.vboot.core.entity.MessageSend;
import com.carson.vboot.core.service.MessageSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageSendServiceImpl implements MessageSendService {
    @Autowired
    private MessageSendDao messageSendDao;


    @Override
    public VbootBaseDao<MessageSend> getBaseDao() {
        return messageSendDao;
    }

    /**
     * 分页查询
     *
     * @param pageBo
     * @param messageSend
     * @return
     */
    @Override
    public IPage<MessageSend> getMessageSendByPage(PageBo pageBo, MessageSend messageSend) {

        Page<MessageSend> page = new Page<>(pageBo.getPageIndex(), pageBo.getPageSize());
        QueryWrapper<MessageSend> messageSendQueryWrapper = new QueryWrapper<>();


        // 根据时间倒序
        messageSendQueryWrapper.orderByDesc(true, "create_time");
        Page<MessageSend> messageSendPage = messageSendDao.selectPage(page, messageSendQueryWrapper);

        return messageSendPage;
    }

}
