package com.carson.vboot.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.carson.vboot.core.base.VbootService;
import com.carson.vboot.core.bo.PageBo;
import com.carson.vboot.core.entity.DictDetail;

import java.util.List;

public interface DictDetailService extends VbootService<DictDetail> {
    IPage<DictDetail> getDictDetailByPage(PageBo pageBo, DictDetail dictDetail);

    /**
     * 根据dictID获取字典详情
     * @param dictId 字典id
     * @param name 搜索字典详情名称
     * @return
     */
    List<DictDetail> getDictDetailByDictId(String dictId,String name);

}
