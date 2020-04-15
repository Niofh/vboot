package com.carson.vboot.core.base;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.Collection;
import java.util.List;

/**
 * created by Nicofh on 2020-03-08
 * 初始化一些增删改查等基础方法
 */

@FunctionalInterface
public interface VbootService<T> {

    // 实例化dao
    public VbootBaseDao<T> getBaseDao();


    /**
     * 根据ID获取实体类数据
     *
     * @param id
     * @return
     */
    public default T getId(String id) {
        return getBaseDao().selectById(id);
    }


    /**
     * 获取所有列表
     *
     * @return
     */
    public default List<T> getAll() {
        return getBaseDao().selectList(null);
    }


    /**
     * 保存
     *
     * @param entity
     * @return
     */
    public default T save(T entity) {
        int insert = getBaseDao().insert(entity);
        if (insert > 0) {
            return entity;
        } else {
            return null;
        }
    }


    /**
     * 修改
     *
     * @param entity
     * @return
     */
    public default T update(T entity) {
        int i = getBaseDao().updateById(entity);
        if (i > 0) {
            return entity;
        } else {
            return null;
        }
    }


    /**
     * 根据Id删除
     *
     * @param id
     */
//    public default Integer delete(String id) {
//        return getBaseDao().deleteById(id);
//    }


    /**
     * 批量id删除
     *
     * @param idList
     */
    public default Integer delete(Collection<String> idList) {
        return getBaseDao().deleteBatchIds(idList);
    }


    /**
     * 根据查询条件分页获取
     *
     * @param page
     * @param queryWrapper
     * @return
     */
    public default IPage<T> selectPage(IPage<T> page, Wrapper<T> queryWrapper) {
        return getBaseDao().selectPage(page, queryWrapper);
    }


}
