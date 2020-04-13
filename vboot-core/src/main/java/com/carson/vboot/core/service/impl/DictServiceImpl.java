package com.carson.vboot.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carson.vboot.core.base.VbootBaseDao;
import com.carson.vboot.core.bo.PageBo;
import com.carson.vboot.core.dao.mapper.DictDao;
import com.carson.vboot.core.entity.Dict;
import com.carson.vboot.core.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DictServiceImpl implements DictService {
    @Autowired
    private DictDao dictDao;


    @Override
    public VbootBaseDao<Dict> getBaseDao() {
        return dictDao;
    }

    /**
     * 分页查询
     * @param pageBo
     * @param dict
     * @return
     */
    @Override
    public IPage<Dict> getDictByPage(PageBo pageBo, Dict dict) {

        Page<Dict> page = new Page<>(pageBo.getPageIndex(), pageBo.getPageSize());
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();


        if (StrUtil.isNotBlank(dict.getDicName())) {

            dictQueryWrapper.like("dic_name", dict.getDicName());
        }


        // 根据时间倒序
        dictQueryWrapper.orderByDesc(true, "create_time");
        Page<Dict> dictPage = dictDao.selectPage(page, dictQueryWrapper);

        return dictPage;
    }

}
