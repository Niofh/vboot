package com.carson.vboot.core.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * created by Nicofh on 2020-03-08
 */

// 自定义接口 不会创建接口的实例 必须加此注解
@NoRepositoryBean
public interface VbootBaseDao<T> extends BaseMapper<T> {
}
