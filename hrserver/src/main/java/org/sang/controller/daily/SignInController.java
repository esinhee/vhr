package org.sang.controller.daily;

import org.sang.bean.RespBean;
import org.sang.bean.Sign;
import org.sang.mapper.HrMapper;
import org.sang.service.HrService;
import org.sang.service.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*****
 * 签到
 * @author qhee
 *
 */
@RestController
@RequestMapping("/daily/punch")
public class SignInController {
	@Autowired
	SignService signService;
	
	@Autowired
	HrService hrService;
	
	@RequestMapping(value = "/getsign", method = RequestMethod.GET)
	public Map<String, Object> checkSign() {
		//检查是否签到过
		Map<String, Object> map = new HashMap<>();
		Sign s = signService.getSign();
		if (s != null) {
			map.put("status", "success");
			map.put("msg", s);
		} else {
			map.put("status", "error");
			map.put("msg", "请签到！");
		}
		return map;
	}

	/**
	 * 签退
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/signout", method = RequestMethod.PUT)
	public RespBean updateSign(Integer id) {
		if (signService.doSignOut(id) == 1) {
			return new RespBean("success", "更新成功!");
		}
		return new RespBean("error", "更新失败!");
	}

	@RequestMapping(value = "/daka", method = RequestMethod.POST)
	public Integer doSign(Integer iDuty, Integer bz) {
		return signService.doSign(iDuty, bz);
	}

	/**
	 * 检查是否值班
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/checkduty/{id}", method = RequestMethod.GET)
	public Integer checkDuty(@PathVariable Integer id) {
		if (signService.getDutyInfo(id) != null) {
			return 1;
		} else {
			return 0;
		}
	}

	@RequestMapping(value = "/todaylist", method = RequestMethod.GET)
	public List<Sign> getTodaySignList() {
		return signService.selectOnToday();
	}
}
