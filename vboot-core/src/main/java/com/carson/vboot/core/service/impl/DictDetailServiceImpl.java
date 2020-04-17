package com.carson.vboot.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carson.vboot.core.base.VbootBaseDao;
import com.carson.vboot.core.bo.PageBo;
import com.carson.vboot.core.common.enums.ExceptionEnums;
import com.carson.vboot.core.dao.mapper.DictDetailDao;
import com.carson.vboot.core.entity.DictDetail;
import com.carson.vboot.core.exception.VbootException;
import com.carson.vboot.core.service.DictDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class DictDetailServiceImpl implements DictDetailService {
    @Autowired
    private DictDetailDao dictDetailDao;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public VbootBaseDao<DictDetail> getBaseDao() {
        return dictDetailDao;
    }

    /**
     * 分页查询
     *
     * @param pageBo
     * @param dictDetail
     * @return
     */
    @Override
    public IPage<DictDetail> getDictDetailByPage(PageBo pageBo, DictDetail dictDetail) {

        Page<DictDetail> page = new Page<>(pageBo.getPageIndex(), pageBo.getPageSize());
        QueryWrapper<DictDetail> dictDetailQueryWrapper = new QueryWrapper<>();


        if (StrUtil.isNotBlank(dictDetail.getName())) {

            dictDetailQueryWrapper.like("name", dictDetail.getName());
        }


        // 排序
        dictDetailQueryWrapper.orderByDesc("sort");


        // 根据时间倒序
        dictDetailQueryWrapper.orderByDesc(true, "create_time");
        Page<DictDetail> dictDetailPage = dictDetailDao.selectPage(page, dictDetailQueryWrapper);

        return dictDetailPage;
    }

    /**
     * 根据dictID获取字典详情
     *
     * @param dictId 字典id
     * @return
     */
    @Override
    @Cacheable(cacheNames = "vboot::dictDetail", key = "#dictId", condition = "#name==''") // 根据dictId缓存字典详情
    public List<DictDetail> getDictDetailByDictId(String dictId, String name) {

        QueryWrapper<DictDetail> dictDetailQueryWrapper = new QueryWrapper<>();
        dictDetailQueryWrapper.eq("dict_id", dictId);

        if (StrUtil.isNotBlank(name)) {
            dictDetailQueryWrapper.like("name", name);
        }
        // 排序
        dictDetailQueryWrapper.orderByDesc("sort");

        // 创建时间排序
        dictDetailQueryWrapper.orderByDesc("create_time");
        List<DictDetail> dictDetails = dictDetailDao.selectList(dictDetailQueryWrapper);

        return dictDetails;
    }

    /**
     * 保存
     *
     * @param entity
     * @return
     */
    @Caching(
        evict = {
            @CacheEvict(cacheNames = "vboot::dictDetail", key = "#result.dictId", condition = "#result!=null") // 删除根据dictID的缓存
        }
    )
    @Override
    public DictDetail save(DictDetail entity) {
        QueryWrapper<DictDetail> dictDetailQueryWrapper = new QueryWrapper<>();
        dictDetailQueryWrapper.eq("code", entity.getCode()).eq("dict_id", entity.getDictId());
        DictDetail dictDetail = dictDetailDao.selectOne(dictDetailQueryWrapper);
        if (dictDetail != null) {
            throw new VbootException(ExceptionEnums.DICT_CODE_EXIST);
        }
        int insert = dictDetailDao.insert(entity);
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
    @Caching(
        evict = {
                @CacheEvict(cacheNames = "vboot::dictDetail", key = "#result.dictId", condition = "#result!=null") // 删除根据dictID的缓存
        }
    )
    @Override
    public DictDetail update(DictDetail entity) {
        QueryWrapper<DictDetail> dictDetailQueryWrapper = new QueryWrapper<>();
        dictDetailQueryWrapper.ne("id", entity.getDictId()).ne("dict_id", entity.getDictId()).eq("code", entity.getCode());
        DictDetail dictDetail = dictDetailDao.selectOne(dictDetailQueryWrapper);
        if (dictDetail != null) {
            throw new VbootException(ExceptionEnums.DICT_CODE_EXIST);
        }
        int i = dictDetailDao.updateById(entity);
        if (i > 0) {
            return entity;
        } else {
            return null;
        }
    }

    /**
     * 批量id删除
     *
     * @param idList
     */
    @Caching(
        evict = {
                @CacheEvict(cacheNames = "vboot::dictDetail") // 删除缓存
        }
    )
    @Override
    public Integer delete(Collection<String> idList) {

        return dictDetailDao.deleteBatchIds(idList);
    }
}
