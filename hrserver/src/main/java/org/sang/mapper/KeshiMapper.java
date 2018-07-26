package org.sang.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.sang.bean.Keshi;

public interface KeshiMapper {
    int deleteByPrimaryKey(String kid);

    int insert(Keshi record);

    int insertSelective(Keshi record);

    Keshi selectByPrimaryKey(String kid);

    int updateByPrimaryKeySelective(Keshi record);

    int updateByPrimaryKey(Keshi record);
    
    List<Keshi> getKeshiList();
    
    List<Keshi> getKeshiByParentId(String pid);
    
    void addKeshi(@Param("ks") Keshi keshi);
}