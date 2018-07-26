package org.sang.mapper;

import org.sang.bean.EventTask;

public interface EventTaskMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EventTask record);

    int insertSelective(EventTask record);

    EventTask selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EventTask record);

    int updateByPrimaryKey(EventTask record);

    int updateWithReassign(EventTask record);

    EventTask getEventTaskInfo(Integer eid);
}