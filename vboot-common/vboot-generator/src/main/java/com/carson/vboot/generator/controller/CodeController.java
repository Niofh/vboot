package com.carson.vboot.generator.controller;

import cn.hutool.core.util.ZipUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.carson.vboot.core.base.VBootController;
import com.carson.vboot.core.base.VbootService;
import com.carson.vboot.core.bo.PageBo;
import com.carson.vboot.core.common.utils.FileUtil;
import com.carson.vboot.core.common.utils.ResultUtil;
import com.carson.vboot.core.vo.Result;
import com.carson.vboot.generator.entity.Code;
import com.carson.vboot.generator.service.CodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

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


    @GetMapping(value = "/fileDownLoad")
    @ApiOperation(value = "代码文件下载")
    public void fileDownLoad(HttpServletResponse  response,@RequestParam(name="id") String id) {
        //文件下载https://blog.csdn.net/zhangvalue/article/details/89387261
        String path = codeService.fileDownLoad(id);
        ZipUtil.zip(path);
        try {
            FileUtil.downLoad(response, id+".zip", path + ".zip");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


}
