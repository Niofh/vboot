package com.carson.vboot.generator.controller;

import com.carson.vboot.core.base.VBootController;
import com.carson.vboot.core.base.VbootService;
import com.carson.vboot.generator.entity.Code;
import com.carson.vboot.generator.service.CodeService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author oufuhua
 */
@Api(tags = "代码生成")
@RestController
@Slf4j
@RequestMapping("/code")
public class CodeController extends VBootController<Code> {

    @Autowired
    private CodeService codeService;

    /**
     * 获取service
     *
     * @return
     */
    @Override
    public VbootService<Code> getService() {
        return codeService;
    }
}
