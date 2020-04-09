package com.carson.vboot.generator.service.impl;

import com.carson.vboot.core.base.VbootBaseDao;
import com.carson.vboot.generator.dao.mapper.CodeDetailDao;
import com.carson.vboot.generator.entity.CodeDetail;
import com.carson.vboot.generator.service.CodeDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     * 修改
     *
     * @param entity
     * @return
     */
    @Override
    public CodeDetail update(CodeDetail entity) {
        int i = codeDetailDao.updateById(entity);
        if (i > 0) {
            return entity;
        } else {
            return null;
        }
    }
}
