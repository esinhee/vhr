package org.sang.mapper;

import org.apache.ibatis.annotations.Param;
import org.sang.bean.Hr;
import org.sang.bean.Role;

import java.util.List;

/**
 * Created by sang on 2017/12/28.
 */
public interface HrMapper {
    Hr loadUserByUsername(String username);

    List<Role> getRolesByHrId(Long id);

    int hrReg(@Param("username") String username, @Param("password") String password);

    List<Hr> getHrsByKeywords(@Param("keywords") String keywords);

    int updateHr(Hr hr);

    int deleteRoleByHrId(Long hrId);

    int addRolesForHr(@Param("hrId") Long hrId, @Param("rids") Long[] rids);
    
    int addRolesForHr2(@Param("hrId") Long hrId, @Param("rids") Long[] rids);

    Hr getHrById(Long hrId);

    int deleteHr(Long hrId);

    List<Hr> getAllHr(@Param("currentId") Long currentId);
    
    int newOperator(Hr hr);
    
    List<Hr> getOperatorList(@Param("rank") String rank);

    int updateUserFace(@Param("userid") Integer userid, @Param("userface") String userface);

    Hr getHrByPassword(@Param("username") String username, @Param("password") String password);

    int updatePassword(@Param("userid") Integer userid, @Param("password") String password);

    List<Hr> getRyList();
}
