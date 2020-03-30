package com.carson.vboot.generator.service.impl;

import com.carson.vboot.core.base.VbootBaseDao;
import com.carson.vboot.generator.dao.mapper.CodeDao;
import com.carson.vboot.generator.entity.Code;
import com.carson.vboot.generator.service.CodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public  class CodeServiceImpl implements CodeService {
    @Autowired
    private CodeDao codeDao;

    @Override
    public VbootBaseDao<Code> getBaseDao() {
        return codeDao;
    }
}
