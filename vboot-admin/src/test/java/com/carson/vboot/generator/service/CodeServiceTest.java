package com.carson.vboot.generator.service;

import com.carson.vboot.generator.dao.mapper.CodeDetailDao;
import com.carson.vboot.generator.entity.CodeDetail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CodeServiceTest {

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
}
