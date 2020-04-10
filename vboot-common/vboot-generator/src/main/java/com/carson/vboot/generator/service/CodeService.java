package com.carson.vboot.generator.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.carson.vboot.core.base.VbootService;
import com.carson.vboot.core.bo.PageBo;
import com.carson.vboot.generator.entity.Code;

import java.util.HashMap;

public interface CodeService extends VbootService<Code> {
    IPage<Code> getCodeByPage(PageBo pageBo, Code code);

    /**
     * 根据id生成文件，并且压缩，返回压缩文件路径
     * @param id
     */
    String  fileDownLoad(String id);

    /**
     * 生成代码返回前端
     * @param id
     * @return
     */
    HashMap<String,Object> showCode(String id);
}
