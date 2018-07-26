package org.sang.service;

import java.util.List;

import org.sang.bean.Staff;
import org.sang.mapper.StaffMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StaffService {
	@Autowired
	StaffMapper staffMapper;
	
	public List<Staff> getAllStaff() {
		return staffMapper.selectAllStaff();
	}
}
