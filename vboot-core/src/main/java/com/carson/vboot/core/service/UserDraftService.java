package com.carson.vboot.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.carson.vboot.core.base.VbootService;
import com.carson.vboot.core.bo.PageBo;
import com.carson.vboot.core.entity.UserDraft;

public interface UserDraftService extends VbootService<UserDraft> {
    IPage<UserDraft> getUserDraftByPage(PageBo pageBo, UserDraft userDraft);
}
