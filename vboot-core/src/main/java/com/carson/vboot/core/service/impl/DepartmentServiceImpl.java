package com.carson.vboot.core.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.carson.vboot.core.base.VbootBaseDao;
import com.carson.vboot.core.common.enums.CommonEnums;
import com.carson.vboot.core.common.enums.ExceptionEnums;
import com.carson.vboot.core.dao.mapper.DepartmentDao;
import com.carson.vboot.core.dao.mapper.DepartmentHeaderDao;
import com.carson.vboot.core.entity.Department;
import com.carson.vboot.core.entity.DepartmentHeader;
import com.carson.vboot.core.exception.VbootException;
import com.carson.vboot.core.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * created by Nicofh on 2020-03-09
 */
@Slf4j
@Service
public class DepartmentServiceImpl implements DepartmentService {


    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private DepartmentHeaderDao departmentHeaderDao;

    @Override
    public VbootBaseDao<Department> getBaseDao() {
        return departmentDao;
    }

    /**
     * 根据ID获取实体类数据
     *
     * @param id
     * @return
     */
    @Override
    public Department getId(String id) {
        Department department = departmentDao.selectById(id);
        this.getDepartmentHeader(department);
        return department;
    }


    /**
     * 获取全部数据
     *
     * @return
     */
    @Cacheable(cacheNames = "vboot::dep",key = "'getall'")
    @Override
    public List<Department> getAll() {
        List<Department> departments = departmentDao.selectList(null);
        for (Department department : departments) {
            this.getDepartmentHeader(department);
        }
        return departments;
    }

    /**
     * 根据部门获取部门负责人
     *
     * @param department
     */
    private void getDepartmentHeader(Department department) {
        if (null == department) {
            return;
        }
        String departmentId = department.getId();

        // 根据部门id获取部门负责人的数据
        QueryWrapper<DepartmentHeader> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("department_id", departmentId);
        List<DepartmentHeader> departmentHeaderList = departmentHeaderDao.selectList(queryWrapper);


        ArrayList<String> mainHeaders = new ArrayList<>();
        ArrayList<String> viceHeaders = new ArrayList<>();
        for (DepartmentHeader departmentHeader : departmentHeaderList) {
            // 获取部门负责人的id插入到新的数组
            if (departmentHeader.getType().equals(CommonEnums.HEADER_TYPE_VICE.getId())) {
                mainHeaders.add(departmentHeader.getUserId());
            } else {
                viceHeaders.add(departmentHeader.getUserId());
            }
        }
        department.setMainHeader(mainHeaders);
        department.setViceHeader(viceHeaders);
    }

    /**
     * 添加部门
     *
     * @param department
     * @return
     */
    @CacheEvict(cacheNames = "vboot::dep",key = "'getall'")
    @Transactional
    @Override
    public Department save(Department department) {
        try {
            department.setId(null);
            // 添加新部门
            departmentDao.insert(department);
            // 获取部门id
            String depId = department.getId();
            log.info("【获取部门id】 {}", depId);

            // 主负责人
            List<String> mainHeader = department.getMainHeader();
            // 次负责人
            List<String> viceHeader = department.getViceHeader();

            // 判断是否有主管，有的就插入部门主管表
            if (CollUtil.isNotEmpty(mainHeader)) {
                for (String userId : mainHeader) {
                    DepartmentHeader departmentHeader = new DepartmentHeader();
                    departmentHeader.setDepartmentId(depId);
                    departmentHeader.setType(CommonEnums.HEADER_TYPE_MAIN.getId());
                    departmentHeader.setUserId(userId);
                    departmentHeaderDao.insert(departmentHeader);
                }

            }

            // 次负责人
            if (CollUtil.isNotEmpty(viceHeader)) {
                for (String userId : viceHeader) {
                    DepartmentHeader departmentHeader = new DepartmentHeader();
                    departmentHeader.setDepartmentId(depId);
                    departmentHeader.setType(CommonEnums.HEADER_TYPE_VICE.getId());
                    departmentHeader.setUserId(userId);
                    departmentHeaderDao.insert(departmentHeader);
                }
            }
            return department;
        } catch (Exception e) {
            throw new VbootException(ExceptionEnums.ADD_ERROR);
        }

    }

    /**
     * 更新部门
     *
     * @param department
     * @return
     */
    @CacheEvict(cacheNames = "vboot::dep",key = "'getall'")
    @Transactional
    @Override
    public Department update(Department department) {

        try {

            // 部门id
            String depId = department.getId();
            departmentDao.updateById(department);

            // 主负责人
            List<String> mainHeader = department.getMainHeader();
            // 次负责人
            List<String> viceHeader = department.getViceHeader();

            // 删除原来的部门主管
            QueryWrapper<DepartmentHeader> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("department_id", depId);
            departmentHeaderDao.delete(queryWrapper);

            // 判断是否有主管，有的就插入部门主管表
            if (CollUtil.isNotEmpty(mainHeader)) {
                for (String userId : mainHeader) {
                    DepartmentHeader departmentHeader = new DepartmentHeader();
                    departmentHeader.setDepartmentId(depId);
                    departmentHeader.setType(CommonEnums.HEADER_TYPE_MAIN.getId());
                    departmentHeader.setUserId(userId);
                    departmentHeaderDao.insert(departmentHeader);
                }

            }

            // 次负责人
            if (CollUtil.isNotEmpty(viceHeader)) {
                for (String userId : viceHeader) {
                    DepartmentHeader departmentHeader = new DepartmentHeader();
                    departmentHeader.setDepartmentId(depId);
                    departmentHeader.setType(CommonEnums.HEADER_TYPE_VICE.getId());
                    departmentHeader.setUserId(userId);
                    departmentHeaderDao.insert(departmentHeader);
                }
            }

            return department;

        } catch (Exception e) {
            throw new VbootException(ExceptionEnums.UPDATE_ERROR);
        }
    }


    /**
     * 批量id删除
     *
     * @param idList
     */
    @CacheEvict(cacheNames = {"vboot::dep","role::dep"})
    @Transactional
    @Override
    public Integer delete(Collection<String> idList) {
        try {
            if (CollUtil.isNotEmpty(idList)) {
                // 删除部门
                departmentDao.deleteBatchIds(idList);

                for (String depId : idList) {
                    // 删除原来的部门主管
                    QueryWrapper<DepartmentHeader> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("department_id", depId);
                    departmentHeaderDao.delete(queryWrapper);

                    // 删除parent_id存在的数据
                    QueryWrapper<Department> departmentQueryWrapper = new QueryWrapper<>();
                    departmentQueryWrapper.eq("parent_id", depId);
                    departmentDao.delete(departmentQueryWrapper);
                }
            }
            return 1;
        } catch (Exception e) {
            throw new VbootException(ExceptionEnums.DEL_ERROR);
        }
    }

    /**
     * 通过父部门id获取子部门id
     * @param
     * @return
     */
    @Override
    public List<String> findChildByParentId(String depId) {

        ArrayList<String> depIds = new ArrayList<>();
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", depId);

        List<Department> departments = departmentDao.selectList(queryWrapper);
        if (CollUtil.isNotEmpty(departments)) {
            for (Department department : departments) {

                depIds.add(department.getId());
            }
        }
        return depIds;
    }
}
