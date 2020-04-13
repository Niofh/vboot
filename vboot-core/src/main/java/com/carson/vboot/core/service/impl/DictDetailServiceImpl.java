package com.carson.vboot.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carson.vboot.core.base.VbootBaseDao;
import com.carson.vboot.core.bo.PageBo;
import com.carson.vboot.core.dao.mapper.DictDetailDao;
import com.carson.vboot.core.entity.DictDetail;
import com.carson.vboot.core.service.DictDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DictDetailServiceImpl implements DictDetailService {
    @Autowired
    private DictDetailDao dictDetailDao;


    @Override
    public VbootBaseDao<DictDetail> getBaseDao() {
        return dictDetailDao;
    }

    /**
     * 分页查询
     *
     * @param pageBo
     * @param dictDetail
     * @return
     */
    @Override
    public IPage<DictDetail> getDictDetailByPage(PageBo pageBo, DictDetail dictDetail) {

        Page<DictDetail> page = new Page<>(pageBo.getPageIndex(), pageBo.getPageSize());
        QueryWrapper<DictDetail> dictDetailQueryWrapper = new QueryWrapper<>();


        if (StrUtil.isNotBlank(dictDetail.getName())) {

            dictDetailQueryWrapper.like("name", dictDetail.getName());
        }


        // 排序
        dictDetailQueryWrapper.orderByDesc("sort");


        // 根据时间倒序
        dictDetailQueryWrapper.orderByDesc(true, "create_time");
        Page<DictDetail> dictDetailPage = dictDetailDao.selectPage(page, dictDetailQueryWrapper);

        return dictDetailPage;
    }

}
