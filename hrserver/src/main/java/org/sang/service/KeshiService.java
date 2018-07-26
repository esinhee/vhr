package org.sang.service;

import java.util.List;

import org.sang.bean.Keshi;
import org.sang.mapper.KeshiMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class KeshiService {

	@Autowired
	KeshiMapper keshiMapper;
	
	public List<Keshi> getKeshiList() {
		return keshiMapper.getKeshiList();
	}
	
	public List<Keshi> getKeshiByParentId(String pid) {
		return keshiMapper.getKeshiByParentId(pid);
	}
	
	public int addKeshi(Keshi keshi) {
		keshi.setEnabled(1);
		keshiMapper.addKeshi(keshi);
		return keshi.getResult();
	}

	public int changeKeshi(String kid, Integer enabled) {
		Keshi ks = new Keshi();
		ks.setKid(kid);
		ks.setEnabled(enabled);
		return keshiMapper.updateByPrimaryKeySelective(ks);
	}
}
