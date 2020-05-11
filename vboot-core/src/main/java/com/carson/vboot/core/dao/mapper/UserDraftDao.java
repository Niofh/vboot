package com.carson.vboot.core.dao.mapper;


import com.carson.vboot.core.base.VbootBaseDao;
import com.carson.vboot.core.entity.UserDraft;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDraftDao extends VbootBaseDao<UserDraft> {

    int insertList(@Param("userDraftList") List<UserDraft> userDraftList);
}

