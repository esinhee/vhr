package org.sang.service;

import java.util.List;

import org.sang.bean.EventType;
import org.sang.mapper.EventTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TicketTypeService {

	@Autowired
	EventTypeMapper eventTypeMapper;
	
	public List<EventType> getTypeList() {
		return eventTypeMapper.selectAllList();
	}
	
	public int insertSelective(EventType et) {
		if (eventTypeMapper.selectByTypeName(et.getTypename()) != null) {
			return -1;
		}
		if (et.getParentid() == 0) {
			et.setSeq(1);
			int ret = eventTypeMapper.insertSelective(et);
			String level = Integer.toString(et.getId());
			et.setLevel(level);
			return eventTypeMapper.updateByPrimaryKeySelective(et);
		} else {
			EventType pet = eventTypeMapper.selectByPrimaryKey(et.getParentid());
			if (pet != null) {
				et.setSeq(pet.getSeq() + 1);
				int ret = eventTypeMapper.insertSelective(et);
				String level = pet.getLevel() + "." + Integer.toString(et.getId());
				et.setLevel(level);
				return eventTypeMapper.updateByPrimaryKeySelective(et);
			} else {
				return 0;
			}
		}
	}

	public List<EventType> selectTypeByParentId(Integer pid) {
		return eventTypeMapper.selectTypeByParentId(pid);
	}
}
