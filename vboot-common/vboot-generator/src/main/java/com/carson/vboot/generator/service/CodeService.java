package com.carson.vboot.generator.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.carson.vboot.core.base.VbootService;
import com.carson.vboot.core.bo.PageBo;
import com.carson.vboot.generator.entity.Code;

public interface CodeService extends VbootService<Code> {
    IPage<Code> getCodeByPage(PageBo pageBo, Code code);
}
