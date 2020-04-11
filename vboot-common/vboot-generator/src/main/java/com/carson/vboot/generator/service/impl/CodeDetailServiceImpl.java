package com.carson.vboot.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.carson.vboot.core.base.VbootBaseDao;
import com.carson.vboot.generator.dao.mapper.CodeDetailDao;
import com.carson.vboot.generator.entity.CodeDetail;
import com.carson.vboot.generator.service.CodeDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CodeDetailServiceImpl implements CodeDetailService {

    @Autowired
    private CodeDetailDao codeDetailDao;

    @Override
    public VbootBaseDao<CodeDetail> getBaseDao() {
        return codeDetailDao;
    }

    /**
     * 获取所有列表
     *
     * @return
     */
    @Override
    public List<CodeDetail> getAll() {
        QueryWrapper<CodeDetail> codeDetailQueryWrapper = new QueryWrapper<>();
        codeDetailQueryWrapper.orderByDesc("num");
        codeDetailQueryWrapper.orderByAsc("create_time");
        return codeDetailDao.selectList(codeDetailQueryWrapper);
    }

    /**
     * 根据codeId获取详细属性信息
     *
     * @param codeId
     * @return
     */
    @Override
    public List<CodeDetail> getAllBaseByCodeId(String codeId) {
        QueryWrapper<CodeDetail> codeDetailQueryWrapper = new QueryWrapper<>();
        codeDetailQueryWrapper.eq("code_id",codeId);
        codeDetailQueryWrapper.orderByDesc("num");
        codeDetailQueryWrapper.orderByAsc("create_time");
        return codeDetailDao.selectList(codeDetailQueryWrapper);
    }
}
