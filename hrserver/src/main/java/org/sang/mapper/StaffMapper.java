package org.sang.mapper;

import java.util.List;

import org.sang.bean.Staff;

public interface StaffMapper {
    int deleteByPrimaryKey(String sid);

    int insert(Staff record);

    int insertSelective(Staff record);

    Staff selectByPrimaryKey(String sid);

    int updateByPrimaryKeySelective(Staff record);

    int updateByPrimaryKey(Staff record);
    
    List<Staff> selectAllStaff();
}