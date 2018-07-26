package org.sang.service;

import org.sang.bean.Sign;
import org.sang.bean.SignQueue;
import org.sang.common.HrUtils;
import org.sang.mapper.SignMapper;
import org.sang.mapper.SignQueueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SignService {

	@Autowired
	SignMapper signMapper;

	@Autowired
	SignQueueMapper signQueueMapper;
	
	public int addSign(Sign sign) {
		return signMapper.insert(sign);
	}

	/**
	 * 检查是否签到过
	 * @return
	 */
	public Sign getSign() {
		return signMapper.selectByUser(Integer.parseInt(String.valueOf(HrUtils.getCurrentHr().getId())));
	}

	/**
	 * 签到处理
	 * @param iDuty
	 * @param bz
	 * @return
	 */
	public Integer doSign(Integer iDuty, Integer bz) {
		Sign sn = new Sign();
		SignQueue sq = new SignQueue();
		String rank = "";
		int userid = Integer.parseInt(String.valueOf(HrUtils.getCurrentHr().getId()));

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sds = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat s = new SimpleDateFormat("HH:mm:ss");

		try {
			sn.setSigntime(sdf.parse(sdf.format(new Date())));
			sn.setSigndate(sd.parse(sd.format(new Date())));
			rank = sds.format(new Date());

			Date dtCur = s.parse(s.format(new Date()));
			Date dtObj = s.parse("12:00:00");
			if (iDuty == 0) {	//非值班签到
				if (dtCur.getTime() > dtObj.getTime()) { //下午班
					sn.setRank(1);
					rank = rank + "02";
				} else {
					sn.setRank(0);
					rank = rank + "01";
				}
			} else {
				sn.setRank(9);
				rank = rank + "00";
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sn.setDuty(iDuty);
		sn.setUserid(userid);
		sn.setRealname(HrUtils.getCurrentHr().getName());
		sn.setBz(bz);
		signMapper.insertSelective(sn);

		if (iDuty == 0) {
			int ret = signQueueMapper.countSign(rank);
			ret++;
			sq.setQueue(ret);
			sq.setRank(rank);
			sq.setUserid(userid);
			signQueueMapper.insertSelective(sq);
		} else {
			sq.setQueue(0);
			sq.setRank("");
			sq.setUserid(userid);
			signQueueMapper.insertSelective(sq);
		}

		return 0;
	}

	/**
	 * 签退
	 * @param id
	 * @return
	 */
	public Integer doSignOut(Integer id) {
		Sign sn = new Sign();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sds = new SimpleDateFormat("yyyyMMdd");
		String strRank = "";
		try {
			sn.setOuttime(sdf.parse(sdf.format(new Date())));
			strRank = sds.format(new Date());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sn.setId(id);
		sn.setBz(1);

		//签退的同时删除所在队列
		signQueueMapper.deleteWhenSingout(Integer.parseInt(String.valueOf(HrUtils.getCurrentHr().getId())), strRank);
		return signMapper.updateByPrimaryKeySelective(sn);
	}

	public Sign getDutyInfo(Integer userid) {
		return signMapper.getDutyInfo(userid);
	}

	/**
	 * 获取当日签到列表
	 * @return
	 */
	public List<Sign> selectOnToday() {
		return signMapper.selectOnToday();
	}
}
