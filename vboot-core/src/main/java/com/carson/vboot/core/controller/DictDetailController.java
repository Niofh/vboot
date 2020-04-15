package com.carson.vboot.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.carson.vboot.core.base.VBootController;
import com.carson.vboot.core.base.VbootService;
import com.carson.vboot.core.bo.PageBo;
import com.carson.vboot.core.common.utils.ResultUtil;
import com.carson.vboot.core.entity.DictDetail;
import com.carson.vboot.core.service.DictDetailService;
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
@RequestMapping("/dictDetail")
public class DictDetailController extends VBootController<DictDetail> {

    @Autowired
    private DictDetailService dictDetailService;

    /**
     * 获取service
     *
     * @return
     */
    @Override
    public VbootService<DictDetail> getService() {
        return dictDetailService;
    }

    @RequestMapping(value = "/getDictDetailByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<DictDetail>> getUserByPage(@Valid PageBo pageBo, DictDetail dictDetail) {

        return ResultUtil.data(dictDetailService.getDictDetailByPage(pageBo, dictDetail));
    }

    @RequestMapping(value = "/getDictDetailByDictId", method = RequestMethod.GET)
    @ApiOperation(value = "根据dictID获取字典详情")
    public Result<List<DictDetail>> getDictDetailByDictId(DictDetail dictDetail) {
        return ResultUtil.data(dictDetailService.getDictDetailByDictId(dictDetail.getDictId(), dictDetail.getName()));
    }

}
