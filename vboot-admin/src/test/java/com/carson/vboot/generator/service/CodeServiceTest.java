package com.carson.vboot.generator.service;

import com.carson.vboot.generator.common.Constant;
import com.carson.vboot.generator.dao.mapper.CodeDetailDao;
import com.carson.vboot.generator.entity.CodeDetail;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ClassUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CodeServiceTest {

    private String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();

    @Autowired
    private CodeService codeService;

    @Autowired
    private CodeDetailDao codeDetailDao;

    @Test
    public void renderFile() {
        codeService.fileDownLoad("257299003744456704");
    }
    @Test
    public void addCodeDetail(){
        CodeDetail codeDetail = new CodeDetail();
        codeDetail.setCodeId("257299003744456704");
        codeDetail.setName("username");
        codeDetail.setChinaName("用户名");
        codeDetail.setNameType("varchar");
        codeDetail.setDescription("用户名");
        codeDetail.setRequired(1);
        codeDetail.setTableSite(1);
        codeDetail.setFormType(1);
        codeDetail.setSearch("=");

        codeDetailDao.insert(codeDetail);
    }

    @Test
    public void del(){
        log.info(path+Constant.TARGET_PATH);
        // FileUtil.del(path+Constant.TARGET_PATH);
    }
}
