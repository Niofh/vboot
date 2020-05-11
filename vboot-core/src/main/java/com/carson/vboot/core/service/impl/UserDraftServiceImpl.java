package com.carson.vboot.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carson.vboot.core.base.VbootBaseDao;
import com.carson.vboot.core.bo.PageBo;
import com.carson.vboot.core.dao.mapper.UserDraftDao;
import com.carson.vboot.core.entity.UserDraft;
import com.carson.vboot.core.service.UserDraftService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserDraftServiceImpl implements UserDraftService {
    @Autowired
    private UserDraftDao userDraftDao;


    @Override
    public VbootBaseDao<UserDraft> getBaseDao() {
        return userDraftDao;
    }

    /**
     * 分页查询
     *
     * @param pageBo
     * @param userDraft
     * @return
     */
    @Override
    public IPage<UserDraft> getUserDraftByPage(PageBo pageBo, UserDraft userDraft) {

        Page<UserDraft> page = new Page<>(pageBo.getPageIndex(), pageBo.getPageSize());
        QueryWrapper<UserDraft> userDraftQueryWrapper = new QueryWrapper<>();


        // 根据时间倒序
        userDraftQueryWrapper.orderByDesc(true, "create_time");
        Page<UserDraft> userDraftPage = userDraftDao.selectPage(page, userDraftQueryWrapper);

        return userDraftPage;
    }

}
