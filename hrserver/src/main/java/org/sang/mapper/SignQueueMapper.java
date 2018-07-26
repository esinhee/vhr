package org.sang.mapper;

import org.apache.ibatis.annotations.Param;
import org.sang.bean.SignQueue;

public interface SignQueueMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SignQueue record);

    int insertSelective(SignQueue record);

    SignQueue selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SignQueue record);

    int updateByPrimaryKey(SignQueue record);

    int countSign(String rank);

    int deleteWhenSingout(@Param("id") Integer id, @Param("rank") String rank);
}