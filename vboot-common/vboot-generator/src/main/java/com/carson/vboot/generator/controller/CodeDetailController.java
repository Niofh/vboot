package com.carson.vboot.generator.controller;

import com.carson.vboot.core.base.VBootController;
import com.carson.vboot.core.base.VbootService;
import com.carson.vboot.core.common.utils.ResultUtil;
import com.carson.vboot.core.vo.Result;
import com.carson.vboot.generator.entity.CodeDetail;
import com.carson.vboot.generator.service.CodeDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author oufuhua
 */
@Api(tags = "代码详情页")
@RestController
@RequestMapping("/codeDetail")
public class CodeDetailController extends VBootController<CodeDetail> {

    @Autowired
    private CodeDetailService codeDetailService;

    @Override
    public VbootService<CodeDetail> getService() {
        return codeDetailService;
    }


    @GetMapping(value = "/getAllBaseByCodeId")
    @ApiOperation(value = "通过代码生成id获取详情列表")
    public Result<List<CodeDetail>> getAllBaseByCodeId(String codeId) {
        return new ResultUtil<List<CodeDetail>>().setData(codeDetailService.getAllBaseByCodeId(codeId));
    }
}
