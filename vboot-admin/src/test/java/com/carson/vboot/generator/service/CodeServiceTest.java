package com.carson.vboot.generator.service;

import com.carson.vboot.generator.dao.mapper.CodeDetailDao;
import com.carson.vboot.generator.entity.CodeDetail;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ClassUtils;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CodeServiceTest {

    private String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private CodeService codeService;

    @Autowired
    private CodeDetailDao codeDetailDao;

    @Test
    public void renderFile() {
        String s = codeService.fileDownLoad("257299003744456704");
        log.info(s);
    }

    @Test
    public void addCodeDetail() {
        CodeDetail codeDetail = new CodeDetail();
        codeDetail.setCodeId("257299003744456704");
        codeDetail.setName("username");
        codeDetail.setChinaName("用户名");
        codeDetail.setNameType("varchar");
        codeDetail.setDescription("用户名");
        codeDetail.setRequired(1);
        codeDetail.setTableSite(1);
        codeDetail.setFormType(1);
        codeDetail.setSearch(1);

        codeDetailDao.insert(codeDetail);
    }

    @Test
    public void test() {
//        redisTemplate.delete("vboot::dictDetail::1252150469406507010");

        redisTemplate.expire("vboot::dictDetail::1252150469406507010",0, TimeUnit.MILLISECONDS);

    }
}
