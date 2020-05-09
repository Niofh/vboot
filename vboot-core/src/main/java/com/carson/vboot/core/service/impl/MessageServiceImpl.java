package com.carson.vboot.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carson.vboot.core.base.VbootBaseDao;
import com.carson.vboot.core.bo.PageBo;
import com.carson.vboot.core.dao.mapper.MessageDao;
import com.carson.vboot.core.entity.Message;
import com.carson.vboot.core.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageDao messageDao;


    @Override
    public VbootBaseDao<Message> getBaseDao() {
        return messageDao;
    }

    /**
     * 分页查询
     *
     * @param pageBo
     * @param message
     * @return
     */
    @Override
    public IPage<Message> getMessageByPage(PageBo pageBo, Message message) {

        Page<Message> page = new Page<>(pageBo.getPageIndex(), pageBo.getPageSize());
        QueryWrapper<Message> messageQueryWrapper = new QueryWrapper<>();


        if (StrUtil.isNotBlank(message.getTitle())) {

            messageQueryWrapper.like("title", message.getTitle());
        }


        if (message.getMsgType()!=null) {
            messageQueryWrapper.eq("msg_type", message.getMsgType());
        }


        // 根据时间倒序
        messageQueryWrapper.orderByDesc(true, "create_time");
        Page<Message> messagePage = messageDao.selectPage(page, messageQueryWrapper);

        return messagePage;
    }

}
