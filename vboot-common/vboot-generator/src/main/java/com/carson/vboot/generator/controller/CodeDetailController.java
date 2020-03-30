package com.carson.vboot.generator.controller;

import com.carson.vboot.core.base.VBootController;
import com.carson.vboot.core.base.VbootService;
import com.carson.vboot.generator.entity.CodeDetail;
import com.carson.vboot.generator.service.CodeDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author oufuhua
 */
@RestController
@Slf4j
@RequestMapping("/codeDetail")
public class CodeDetailController extends VBootController<CodeDetail> {

    @Autowired
    private CodeDetailService codeDetailService;

    @Override
    public VbootService<CodeDetail> getService() {
        return codeDetailService;
    }
}
