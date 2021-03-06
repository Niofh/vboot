package com.carson.vboot.core.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carson.vboot.core.base.VbootBaseDao;
import com.carson.vboot.core.bo.PageBo;
import com.carson.vboot.core.common.enums.CommonEnums;
import com.carson.vboot.core.common.enums.ExceptionEnums;
import com.carson.vboot.core.config.security.SecurityUtil;
import com.carson.vboot.core.dao.mapper.MessageDao;
import com.carson.vboot.core.dao.mapper.MessageSendDao;
import com.carson.vboot.core.dao.mapper.UserDraftDao;
import com.carson.vboot.core.entity.Message;
import com.carson.vboot.core.entity.MessageSend;
import com.carson.vboot.core.entity.User;
import com.carson.vboot.core.entity.UserDraft;
import com.carson.vboot.core.exception.VbootException;
import com.carson.vboot.core.service.MessageService;
import com.carson.vboot.core.service.UserService;
import com.carson.vboot.core.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageDao messageDao;

    @Autowired
    private UserDraftDao userDraftDao;

    @Autowired
    private MessageSendDao messageSendDao;
    @Autowired
    private UserService userService;

    @Autowired
    private SecurityUtil securityUtil;

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

        if (message.getMsgType() != null) {
            messageQueryWrapper.eq("msg_type", message.getMsgType());
        }

        if (StrUtil.isNotBlank(pageBo.getOrder())) {
            String name = StrUtil.toUnderlineCase(pageBo.getSort());
            if ("asc".equals(pageBo.getOrder())) {
                messageQueryWrapper.orderByAsc(name);
            } else {
                messageQueryWrapper.orderByDesc(name);
            }
        }
        // 根据时间倒序
        messageQueryWrapper.orderByDesc(true, "create_time");
        Page<Message> messagePage = messageDao.selectPage(page, messageQueryWrapper);

        return messagePage;
    }

    @Transactional
    @Override
    public Integer sendMsg(String msgId) {

        Message message = messageDao.selectById(msgId);
        // todo 推送消息 rabbitmq+websocket

        if (null == message) {
            throw new VbootException(ExceptionEnums.DATA_NO_EXIST);
        }
        int num = 0;
        if (message.getSendAll() == 1) {
            // 所有人
            UserVO currUser = securityUtil.getCurrUser();
            List<MessageSend> messageSendList = new ArrayList<>();
            List<User> userList = userService.getAll();
            for (User user : userList) {
                Date date = new Date();
                // 创建草稿关联人
                MessageSend messageSend = new MessageSend();
                messageSend.setMessageId(message.getId());
                messageSend.setUserId(user.getId());
                messageSend.setUpdateTime(date);
                messageSend.setCreateTime(date);
                messageSend.setUpdateBy(currUser.getUsername());
                messageSend.setCreateBy(currUser.getUsername());
                messageSend.setStatus(CommonEnums.MESSAGE_STATUS_UNREAD.getId());
                messageSendList.add(messageSend);
            }
            num = messageSendDao.insertList(messageSendList);
        } else {
            List<String> userIdList = message.getUserIdList();
            for (String userId : userIdList) {
                MessageSend messageSend = new MessageSend();
                messageSend.setMessageId(msgId);
                messageSend.setUserId(userId);
                num = messageSendDao.insert(messageSend);
            }
        }
        return num;
    }

    /**
     * 根据ID获取实体类数据
     *
     * @param id
     * @return
     */
    @Override
    public Message getId(String id) {
        Message message = messageDao.selectById(id);

        // 获取草稿的关联用户id
        ArrayList<String> userIdList = new ArrayList<>();
        QueryWrapper<UserDraft> userDraftQueryWrapper = new QueryWrapper<>();
        userDraftQueryWrapper.eq("message_id", message.getId());
        List<UserDraft> userDrafts = userDraftDao.selectList(userDraftQueryWrapper);
        for (UserDraft userDraft : userDrafts) {
            userIdList.add(userDraft.getUserId());
        }
        message.setUserIdList(userIdList);
        return message;
    }

    /**
     * 保存
     *
     * @param message
     * @return
     */
    @Transactional
    @Override
    public Message save(Message message) {

        // 插入消息列表
        int insertMsg = messageDao.insert(message);

        ArrayList<UserDraft> userDraftList = new ArrayList<>();
        List<String> userIdList = message.getUserIdList();

        if (message.getSendAll() == 0) {
            UserVO currUser = securityUtil.getCurrUser();
            // 如果指定人员
            for (String userId : userIdList) {
                UserDraft userDraft = new UserDraft();
                userDraft.setMessageId(message.getId());
                userDraft.setUserId(userId);
                userDraft.setCreateTime(new Date());
                userDraft.setCreateBy(currUser.getUsername());
                userDraftList.add(userDraft);
            }
            // 保存到草稿关联人
            userDraftDao.insertList(userDraftList);
        }

        return message;
    }

    /**
     * 修改
     *
     * @param message
     * @return
     */
    @Transactional
    @Override
    public Message update(Message message) {

        // 更新消息
        messageDao.updateById(message);

        // 删除草稿关联人
        QueryWrapper<UserDraft> userDraftQueryWrapper = new QueryWrapper<>();
        userDraftQueryWrapper.eq("message_id", message.getId());
        userDraftDao.delete(userDraftQueryWrapper);

        // 更新草稿和发送人
        ArrayList<UserDraft> userDraftList = new ArrayList<>();
        List<String> userIdList = message.getUserIdList();

        if (message.getSendAll() == 0) {
            if (CollUtil.isNotEmpty(userIdList)) {
                UserVO currUser = securityUtil.getCurrUser();

                // 如果指定人员
                for (String userId : userIdList) {
                    Date date = new Date();
                    // 创建草稿关联人
                    UserDraft userDraft = new UserDraft();
                    userDraft.setMessageId(message.getId());
                    userDraft.setUserId(userId);
                    userDraft.setUpdateTime(date);
                    userDraft.setCreateTime(date);
                    userDraft.setUpdateBy(currUser.getUsername());
                    userDraft.setCreateBy(currUser.getUsername());
                    userDraftList.add(userDraft);
                }
                // 保存到草稿关联人
                userDraftDao.insertList(userDraftList);
            }
        }
        return message;
    }

    /**
     * 批量id删除
     *
     * @param idList
     */
    @Transactional
    @Override
    public Integer delete(Collection<String> idList) {
        if (CollUtil.isNotEmpty(idList)) {
            for (String id : idList) {
                // 删除草稿
                messageDao.deleteById(id);

                // 删除草稿关联人
                QueryWrapper<UserDraft> userDraftQueryWrapper = new QueryWrapper<>();
                userDraftQueryWrapper.eq("message_id", id);
                userDraftDao.delete(userDraftQueryWrapper);
            }
            return idList.size();
        }
        return null;
    }
}
