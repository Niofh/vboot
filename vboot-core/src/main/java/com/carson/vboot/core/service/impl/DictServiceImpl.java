package com.carson.vboot.core.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carson.vboot.core.base.VbootBaseDao;
import com.carson.vboot.core.bo.PageBo;
import com.carson.vboot.core.common.enums.ExceptionEnums;
import com.carson.vboot.core.dao.mapper.DictDao;
import com.carson.vboot.core.dao.mapper.DictDetailDao;
import com.carson.vboot.core.entity.Dict;
import com.carson.vboot.core.entity.DictDetail;
import com.carson.vboot.core.exception.VbootException;
import com.carson.vboot.core.service.DictDetailService;
import com.carson.vboot.core.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class DictServiceImpl implements DictService {
    @Autowired
    private DictDao dictDao;

    @Autowired
    private DictDetailDao dictDetailDao;

    @Autowired
    private DictDetailService dictDetailService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public VbootBaseDao<Dict> getBaseDao() {
        return dictDao;
    }

    /**
     * 分页查询
     *
     * @param pageBo
     * @param dict
     * @return
     */
    @Override
    public IPage<Dict> getDictByPage(PageBo pageBo, Dict dict) {

        Page<Dict> page = new Page<>(pageBo.getPageIndex(), pageBo.getPageSize());
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();


        if (StrUtil.isNotBlank(dict.getDictName())) {

            dictQueryWrapper.like("dict_name", dict.getDictName());
        }


        // 根据时间倒序
        dictQueryWrapper.orderByDesc(true, "create_time");
        Page<Dict> dictPage = dictDao.selectPage(page, dictQueryWrapper);

        return dictPage;
    }

    /**
     * 根据dictKey获取字典详情
     *
     * @param dictKey
     * @return
     */
    @Cacheable(cacheNames = "vboot::dict", key = "#dictKey", condition = "#dictKey!=null") // 根据字典key缓存数据
    @Override
    public List<DictDetail> getDictDetailByDictKey(String dictKey) {
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("dict_key", dictKey);
        Dict dict = dictDao.selectOne(dictQueryWrapper);
        if (dict == null) {
            throw new VbootException(ExceptionEnums.DICT_KEY_NO_EXIST);
        }

        return dictDetailService.getDictDetailByDictId(dict.getId(), "");
    }

    /**
     * 保存
     *
     * @param entity
     * @return
     */
    @Override
    public Dict save(Dict entity) {
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("dict_key", entity.getDictKey());
        Dict dict = dictDao.selectOne(dictQueryWrapper);
        if (dict != null) {
            throw new VbootException(ExceptionEnums.DICT_KEY_EXIST);
        }
        int insert = dictDao.insert(entity);
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
    @Override
    public Dict update(Dict entity) {
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.ne("id", entity.getId()).eq("dict_key", entity.getDictKey());
        Dict dict = dictDao.selectOne(dictQueryWrapper);
        if (dict != null) {
            throw new VbootException(ExceptionEnums.DICT_KEY_EXIST);
        }
        int i = dictDao.updateById(entity);
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
    @Transactional
    @CacheEvict(cacheNames = "vboot::dict",allEntries = true) // 删除这个key的缓存
    @Override
    public Integer delete(Collection<String> idList) {
        Integer num = 0;
        if (CollUtil.isNotEmpty(idList)) {

            for (String dictId : idList) {

                // 删除缓存
                stringRedisTemplate.delete("vboot::dictDetail::" + dictId);

                // 删除字典详情
                QueryWrapper<DictDetail> dictDetailQueryWrapper = new QueryWrapper<>();
                dictDetailQueryWrapper.eq("dict_id", dictId);
                dictDetailDao.delete(dictDetailQueryWrapper);

            }
            num = dictDao.deleteBatchIds(idList);
        }

        return num;
    }
}
