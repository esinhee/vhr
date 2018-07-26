package org.sang.mapper;

import org.sang.bean.TicketStatus;

import java.util.List;

public interface TicketStatusMapper {
    int deleteByPrimaryKey(Integer zid);

    int insert(TicketStatus record);

    int insertSelective(TicketStatus record);

    TicketStatus selectByPrimaryKey(Integer zid);

    int updateByPrimaryKeySelective(TicketStatus record);

    int updateByPrimaryKey(TicketStatus record);

    List<TicketStatus> selectStatusList();
}