package com.carson.vboot.core.base;

import com.carson.vboot.core.common.utils.ResultUtil;
import com.carson.vboot.core.vo.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * created by Nicofh on 2020-03-08
 */
public abstract class VBootController<T> {
    /**
     * 获取service
     *
     * @return
     */
    @Autowired
    public abstract VbootService<T> getService();


    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "通过id获取")
    public Result<Object> get(@PathVariable String id) {
        T entity = getService().getId(id);
        return new ResultUtil<Object>().setData(entity);
    }


    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取全部数据")
    public Result<Object> getAll() {

        List<T> list = getService().getAll();
        return new ResultUtil<Object>().setData(list);
    }


    @PostMapping(value = "/save")
    @ResponseBody
    @ApiOperation(value = "保存数据")
    public Result<Object> save(@Valid T entity) {
        T e = getService().save(entity);
        if (e != null) {
            return ResultUtil.success("添加成功");
        } else {
            return ResultUtil.error("添加失败");
        }
    }


    @PostMapping(value = "/update")
    @ResponseBody
    @ApiOperation(value = "更新数据")
    public Result<Object> update(@Valid T entity) {
        T e = getService().update(entity);
        if (e != null) {
            return ResultUtil.success("更新成功");
        } else {
            return ResultUtil.error("更新失败");
        }
    }


    @PostMapping(value = "/del")
    @ResponseBody
    @ApiOperation(value = "根据id删除数据")
    public Result<Object> del(String id) {
        Integer count = getService().delete(id);
        if (count > 0) {
            return ResultUtil.success("删除成功");
        } else {
            return ResultUtil.error("删除失败");
        }
    }

    @PostMapping(value = "/delByIds")
    @ResponseBody
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(String[] ids) {
        Integer count = getService().delete(Arrays.asList(ids));
        if (count > 0) {
            return ResultUtil.success("删除成功");
        } else {
            return ResultUtil.error("删除失败");
        }
    }


}
