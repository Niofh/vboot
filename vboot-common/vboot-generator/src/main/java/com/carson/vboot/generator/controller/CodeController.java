package com.carson.vboot.generator.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.carson.vboot.core.base.VBootController;
import com.carson.vboot.core.base.VbootService;
import com.carson.vboot.core.bo.PageBo;
import com.carson.vboot.core.common.utils.ResultUtil;
import com.carson.vboot.core.vo.Result;
import com.carson.vboot.generator.entity.Code;
import com.carson.vboot.generator.service.CodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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

    @RequestMapping(value = "/getCodeByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<Code>> getUserByPage(@Valid PageBo pageBo, Code code) {

        return ResultUtil.data(codeService.getCodeByPage(pageBo, code));
    }
}
