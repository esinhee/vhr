package org.sang.mapper;

import org.sang.bean.EventType;

import java.util.List;

public interface EventTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EventType record);

    int insertSelective(EventType record);

    EventType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EventType record);

    int updateByPrimaryKey(EventType record);

    List<EventType> selectAllList();

    EventType selectByTypeName(String typename);

    List<EventType> selectTypeByParentId(Integer pid);
}