package com.carson.vboot.generator.service;

import com.carson.vboot.core.base.VbootService;
import com.carson.vboot.generator.entity.CodeDetail;

import java.util.List;

public interface CodeDetailService extends VbootService<CodeDetail> {

    /**
     * 根据codeId获取详细属性信息
     * @param codeId
     * @return
     */
    List<CodeDetail> getAllBaseByCodeId(String codeId);
}
