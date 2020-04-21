package com.carson.vboot.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.carson.vboot.core.base.VBootController;
import com.carson.vboot.core.base.VbootService;
import com.carson.vboot.core.bo.PageBo;
import com.carson.vboot.core.common.utils.ResultUtil;
import com.carson.vboot.core.entity.Dict;
import com.carson.vboot.core.entity.DictDetail;
import com.carson.vboot.core.service.DictService;
import com.carson.vboot.core.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author oufuhua
 */
@Api(tags = "代码生成")
@RestController
@Slf4j
@RequestMapping("/dict")
public class DictController extends VBootController<Dict> {

    @Autowired
    private DictService dictService;

    /**
     * 获取service
     *
     * @return
     */
    @Override
    public VbootService<Dict> getService() {
        return dictService;
    }

    @RequestMapping(value = "/getDictByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<Dict>> getUserByPage(@Valid PageBo pageBo, Dict dict) {

        return ResultUtil.data(dictService.getDictByPage(pageBo, dict));
    }

    @RequestMapping(value = "/getDictDetailByDictKey", method = RequestMethod.GET)
    @ApiOperation(value = "根据dictKey获取字典详情")
    public Result<List<DictDetail>> getDictDetailByDictKey(String dictKey) {
        return ResultUtil.data(dictService.getDictDetailByDictKey(dictKey));
    }

}
