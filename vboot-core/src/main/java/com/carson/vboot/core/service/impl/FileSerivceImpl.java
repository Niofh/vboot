package com.carson.vboot.core.service.impl;

import com.carson.vboot.core.base.VbootBaseDao;
import com.carson.vboot.core.dao.mapper.FileDao;
import com.carson.vboot.core.entity.File;
import com.carson.vboot.core.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileSerivceImpl implements FileService {

    @Autowired
    private FileDao fileDao;

    @Override
    public VbootBaseDao<File> getBaseDao() {
        return fileDao;
    }
}
