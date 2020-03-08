package com.carson.vboot.core.base;

import cn.hutool.core.util.PageUtil;
import com.carson.vboot.core.common.utils.ResultUtil;
import com.carson.vboot.core.vo.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

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
    public Result<T> get(@PathVariable String id) {
        T entity = getService().getId(id);
        return new ResultUtil<T>().setData(entity);
    }


    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取全部数据")
    public Result<List<T>> getAll(){

        List<T> list = getService().getAll();
        return new ResultUtil<List<T>>().setData(list);
    }



    @PostMapping(value = "/save")
    @ResponseBody
    @ApiOperation(value = "保存数据")
    public Result<Object> save(T entity) {
        getService().save(entity);
        return ResultUtil.success("添加成功");
    }



    @PostMapping(value = "/del")
    @ResponseBody
    @ApiOperation(value = "根据id删除数据")
    public void del(String id) {
        getService().delete(id);
    }
}
