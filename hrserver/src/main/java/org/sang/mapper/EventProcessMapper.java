package org.sang.mapper;

import org.sang.bean.EventProcess;

import java.util.List;

public interface EventProcessMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EventProcess record);

    int insertSelective(EventProcess record);

    EventProcess selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EventProcess record);

    int updateByPrimaryKeyWithBLOBs(EventProcess record);

    int updateByPrimaryKey(EventProcess record);

    List<EventProcess> selectProcessList(Integer eid);
}