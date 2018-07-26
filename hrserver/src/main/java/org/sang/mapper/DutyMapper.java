package org.sang.mapper;

import org.sang.bean.Duty;

public interface DutyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Duty record);

    int insertSelective(Duty record);

    Duty selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Duty record);

    int updateByPrimaryKey(Duty record);
}