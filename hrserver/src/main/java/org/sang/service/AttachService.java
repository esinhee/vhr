package org.sang.service;

import org.sang.bean.Attach;
import org.sang.mapper.AttachMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 附近管理
 * @author qhee
 *
 */
@Service
@Transactional
public class AttachService {
	@Autowired
	AttachMapper attachMapper;
	
	public int addAttach(Attach at) {
		return attachMapper.insertSelective(at);
	}

	public int releatedAttach(Integer eid, List<Integer> atList) {
		return attachMapper.releatedAttach(eid, atList);
	}
}
