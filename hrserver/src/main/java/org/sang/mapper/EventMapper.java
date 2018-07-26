package org.sang.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.sang.bean.Event;

public interface EventMapper {
    int deleteByPrimaryKey(Integer eid);

    int insert(Event record);

    int insertSelective(Event record);

    Event selectByPrimaryKey(Integer eid);

    int updateByPrimaryKeySelective(Event record);

    int updateByPrimaryKeyWithBLOBs(Event record);

    int updateByPrimaryKey(Event record);
    
    List<Event> getTodayTicketList(@Param("dt") Date dt);

    int refuseTicket(Map param);

    Map<String, Object> countTicket(@Param("userid") Integer userid, @Param("dt") Date dt);
    
    Event getTicketInfo(Integer id);

    int updateDealFlg(@Param("eid") Integer eid,@Param("flg") Integer flg);

    List<Event> getTicketListByPage(@Param("startDate") Date startDate,@Param("endDate") Date endDate,@Param("startid") Integer startid, @Param("size") Integer size);

    int getTicketCount(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    List<Event> selectTicketByKeywords(@Param("startid") Integer startid, @Param("size") Integer size, @Param("grade") Integer grade, @Param("tktype") Integer tktype, @Param("tkstatus") Integer tkstatus, @Param("dealer") Integer dealer, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    int selectTicketCountByKeywords(@Param("grade") Integer grade, @Param("tktype") Integer tktype, @Param("tkstatus") Integer tkstatus, @Param("dealer") Integer dealer, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    List<Event> fetchUndoEventList(@Param("es") Integer es, @Param("userid") Integer userid);
}