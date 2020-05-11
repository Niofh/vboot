package com.carson.vboot.core.dao.mapper;


import com.carson.vboot.core.base.VbootBaseDao;
import com.carson.vboot.core.entity.MessageSend;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageSendDao extends VbootBaseDao< MessageSend> {

    int insertList(@Param("messageSends") List<MessageSend> messageSends);
}

