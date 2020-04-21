package com.carson.vboot.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.carson.vboot.core.base.VbootService;
import com.carson.vboot.core.bo.PageBo;
import com.carson.vboot.core.entity.Dict;
import com.carson.vboot.core.entity.DictDetail;

import java.util.List;

public interface DictService extends VbootService< Dict> {
    IPage<Dict> getDictByPage(PageBo pageBo, Dict dict);

    /**
     * 根据dickKey获取字典详情
     * @param dictKey
     * @return
     */
    List<DictDetail> getDictDetailByDictKey(String dictKey);
}
