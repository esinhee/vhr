package org.sang.mapper;

import org.sang.bean.Sign;

import java.util.List;

public interface SignMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Sign record);

    int insertSelective(Sign record);

    Sign selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Sign record);

    int updateByPrimaryKey(Sign record);

    Sign selectByUser(Integer userid);

    Sign getDutyInfo(Integer userid);

    List<Sign> selectOnToday();
}