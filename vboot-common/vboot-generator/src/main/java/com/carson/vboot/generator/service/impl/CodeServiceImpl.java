package com.carson.vboot.generator.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carson.vboot.core.base.VbootBaseDao;
import com.carson.vboot.core.bo.PageBo;
import com.carson.vboot.generator.dao.mapper.CodeDao;
import com.carson.vboot.generator.entity.Code;
import com.carson.vboot.generator.service.CodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CodeServiceImpl implements CodeService {
    @Autowired
    private CodeDao codeDao;

    @Override
    public VbootBaseDao<Code> getBaseDao() {
        return codeDao;
    }

    @Override
    public IPage<Code> getCodeByPage(PageBo pageBo, Code code) {

        Page<Code> page = new Page<>(pageBo.getPageIndex(), pageBo.getPageSize());
        QueryWrapper<Code> codeQueryWrapper = new QueryWrapper<>();

        if (StrUtil.isNotBlank(code.getTableName())) {

            codeQueryWrapper.like("table_name", code.getTableName());
        }

        // 根据时间倒序
        codeQueryWrapper.orderByDesc(true, "create_time");
        Page<Code> codePage = codeDao.selectPage(page, codeQueryWrapper);

        return codePage;
    }
}
