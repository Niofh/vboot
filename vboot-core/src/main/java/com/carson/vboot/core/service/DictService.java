package com.carson.vboot.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.carson.vboot.core.base.VbootService;
import com.carson.vboot.core.bo.PageBo;
import com.carson.vboot.core.entity.Dict;

public interface DictService extends VbootService< Dict> {
    IPage<Dict> getDictByPage(PageBo pageBo, Dict dict);
}
