package org.sang.mapper;

import org.sang.bean.Attach;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AttachMapper {
    int deleteByPrimaryKey(Integer atid);

    int insert(Attach record);

    int insertSelective(Attach record);

    Attach selectByPrimaryKey(Integer atid);

    int updateByPrimaryKeySelective(Attach record);

    int updateByPrimaryKey(Attach record);

    int releatedAttach(@Param("eid") Integer eid, @Param("list") List<Integer> list);
}