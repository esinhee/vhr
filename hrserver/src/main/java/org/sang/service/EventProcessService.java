package org.sang.service;

import org.sang.bean.Event;
import org.sang.bean.EventProcess;
import org.sang.bean.EventTask;
import org.sang.common.HrUtils;
import org.sang.mapper.EventMapper;
import org.sang.mapper.EventProcessMapper;
import org.sang.mapper.EventTaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class EventProcessService {
    @Autowired
    EventProcessMapper eventProcessMapper;

    @Autowired
    EventMapper eventMapper;

    @Autowired
    EventTaskMapper eventTaskMapper;

    /**
     * 处理工单
     * @param ep
     * @param flg
     * @return
     */
    public int newEventProcess(EventProcess ep, Integer flg) {
        //获取工单信息
        Event et = eventMapper.getTicketInfo(ep.getEid());
        EventTask etask = eventTaskMapper.getEventTaskInfo(ep.getEid());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            ep.setCurdatetime(sdf.parse(sdf.format(new Date())));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ep.setOpid(Integer.parseInt(String.valueOf(HrUtils.getCurrentHr().getId())));
        ep.setOpname(HrUtils.getCurrentHr().getName());
        ep.setTaskid(etask.getId());
        eventProcessMapper.insertSelective(ep);
        if (et.getDealflg() <= flg) {
            eventMapper.updateDealFlg(ep.getEid(), flg);
        }
        return 1;
    }

    public List<EventProcess> selectProcessList(Integer eid) {
        return eventProcessMapper.selectProcessList(eid);
    }
}
