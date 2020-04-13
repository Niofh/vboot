package com.carson.vboot.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.carson.vboot.core.base.VbootService;
import com.carson.vboot.core.bo.PageBo;
import com.carson.vboot.core.entity.DictDetail;

public interface DictDetailService extends VbootService<DictDetail> {
    IPage<DictDetail> getDictDetailByPage(PageBo pageBo, DictDetail dictDetail);
}
