package org.sang.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sang.bean.Event;
import org.sang.bean.EventTask;
import org.sang.common.HrUtils;
import org.sang.mapper.EventMapper;
import org.sang.mapper.EventTaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 问题处理
 * @author qhee
 *
 */
@Service
@Transactional
public class TicketService {
	@Autowired
	EventMapper eventMapper;
	@Autowired
	EventTaskMapper eventTaskMapper;
	
	@Transactional
	public int addTicket(Event et) {
		int ret = 0;
		et.setRecorderid(Integer.parseInt(String.valueOf(HrUtils.getCurrentHr().getId())));
		et.setRecorder(HrUtils.getCurrentHr().getName());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			et.setRecordetime(sdf.parse(sdf.format(new Date())));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!"".equals(et.getDealer().trim()) && et.getDealerid() != 0) {
			et.setDealflg(2);
		}
		eventMapper.insertSelective(et);
		
		EventTask etask = new EventTask();
		etask.setEid(et.getEid());
		etask.setAssignid(et.getDealerid());
		etask.setAssignname(et.getDealer());
		etask.setAssigntime(et.getRecordetime());
		etask.setRecorderid(et.getRecorderid());
		etask.setRecordername(et.getRecorder());
		etask.setBz(et.getDealflg());
		eventTaskMapper.insertSelective(etask);
		
		return ret;	}
	
	public List<Event> getTodayTicketList() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dt = null;
		try {
			dt = sdf.parse(sdf.format(new Date()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return eventMapper.getTodayTicketList(dt);
	}

	public List<Event> getTodayTicketList(String startDate, String endDate, Integer iPage, Integer size) {
		int startid = (iPage - 1) * size;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dtStartDate = null;
		Date dtEndDate = null;
		try {
			dtStartDate = sdf.parse(startDate);
			dtEndDate = sdf.parse(endDate + 1);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return eventMapper.getTicketListByPage(dtStartDate, dtEndDate, startid, size);
	}

	public int getTicketCount(String startDate, String endDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dtStartDate = null;
		Date dtEndDate = null;
		try {
			dtStartDate = sdf.parse(startDate);
			dtEndDate = sdf.parse(endDate + 24);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return eventMapper.getTicketCount(dtStartDate, dtEndDate);
	}

	public List<Event> fetchUndoEventList(Integer es) {
		return eventMapper.fetchUndoEventList(es, Integer.parseInt(String.valueOf(HrUtils.getCurrentHr().getId())));
	}

	/**
	 * 拒绝工单处理
	 * @param id
	 * @return
	 */
	public int refuseTicket(Integer id) {
		HashMap param = new HashMap();
		param.put("id", id);
		param.put("userId", Integer.parseInt(String.valueOf(HrUtils.getCurrentHr().getId())));
		param.put("result", 0);
		eventMapper.refuseTicket(param);
		int ret = (int)param.get("result");
		return ret;
	}

	/**
	 * 统计工单
	 * @return
	 */
	public Map<String, Object> countTicket() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dt = null;
		try {
			dt = sdf.parse(sdf.format(new Date()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return eventMapper.countTicket(Integer.parseInt(String.valueOf(HrUtils.getCurrentHr().getId())), dt);
	}
	
	/**
	 * 获取工单信息
	 * @param id
	 * @return
	 */
	public Event getTicketInfo(Integer id) {
		return eventMapper.getTicketInfo(id);
	}

	/**
	 * 分配处理
	 * @param eid
	 * @param userid
	 * @param name
	 * @return
	 */
	public Integer Reassign(Integer eid, Integer userid, String name) {
		/**
		 * 1. 修改event
		 * 2. 修改eventtask
		 */
		EventTask etask = new EventTask();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date strDate = null;
		try {
			strDate = sdf.parse(sdf.format(new Date()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		etask.setEid(eid);
		etask.setAssignid(userid);
		etask.setAssignname(name);
		etask.setAssigntime(strDate);
		etask.setRecorderid(Integer.parseInt(String.valueOf(HrUtils.getCurrentHr().getId())));
		etask.setRecordername(HrUtils.getCurrentHr().getName());
		etask.setUpdatetime(strDate);
		etask.setBz(2);
		eventTaskMapper.updateWithReassign(etask);
		etask.setUpdatetime(null);
		eventTaskMapper.insertSelective(etask);

		Event et = new Event();
		et.setEid(eid);
		et.setUpdatetime(strDate);
		et.setDealerid(userid);
		et.setDealer(name);
		return eventMapper.updateByPrimaryKeySelective(et);
	}

	/**
	 * 工单查询
	 * @param page
	 * @param size
	 * @param grade
	 * @param tktype
	 * @param tkstatus
	 * @param dealer
	 * @param beginDate
	 * @return
	 */
	public List<Event> searchTicket(Integer page, Integer size, Integer grade, Integer tktype, Integer tkstatus, Integer dealer, String beginDate) {
		int start = (page - 1) * size;
		Date startBeginDate = null;
		Date endBeginDate = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if (beginDate != null && beginDate.contains(",")) {
			try {
				String[] split = beginDate.split(",");
				startBeginDate = dateFormat.parse(split[0]);
				endBeginDate = dateFormat.parse(split[1]);
			} catch (ParseException e) {
			}
		}
		return eventMapper.selectTicketByKeywords(start, size, grade, tktype, tkstatus, dealer, startBeginDate, endBeginDate);
	}

	public int getCountByKeywords(Integer grade, Integer tktype, Integer tkstatus, Integer dealer, String beginDate) {
		Date startBeginDate = null;
		Date endBeginDate = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if (beginDate != null && beginDate.contains(",")) {
			try {
				String[] split = beginDate.split(",");
				startBeginDate = dateFormat.parse(split[0]);
				endBeginDate = dateFormat.parse(split[1]);
			} catch (ParseException e) {
			}
		}
		return eventMapper.selectTicketCountByKeywords(grade, tktype, tkstatus, dealer, startBeginDate, endBeginDate);
	}
}
