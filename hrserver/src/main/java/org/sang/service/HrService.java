package org.sang.service;

import org.sang.bean.Hr;
import org.sang.common.HrUtils;
import org.sang.mapper.HrMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by sang on 2017/12/28.
 */
@Service
@Transactional
public class HrService implements UserDetailsService {

    @Autowired
    HrMapper hrMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Hr hr = hrMapper.loadUserByUsername(s);
        if (hr == null) {
            throw new UsernameNotFoundException("用户名不对");
        }
        return hr;
    }

    public int hrReg(String username, String password) {
        //如果用户名存在，返回错误
        if (hrMapper.loadUserByUsername(username) != null) {
            return -1;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode(password);
        return hrMapper.hrReg(username, encode);
    }

    public List<Hr> getHrsByKeywords(String keywords) {
        return hrMapper.getHrsByKeywords(keywords);
    }

    public int updateHr(Hr hr) {
        return hrMapper.updateHr(hr);
    }

    public int updateHrRoles(Long hrId, Long[] rids) {
        int i = hrMapper.deleteRoleByHrId(hrId);
        return hrMapper.addRolesForHr(hrId, rids);
    }
    
    public int updateHrRoles2(Long hrId, Long[] rids) {
        return hrMapper.addRolesForHr2(hrId, rids);
    }

    public Hr getHrById(Long hrId) {
        return hrMapper.getHrById(hrId);
    }

    public int deleteHr(Long hrId) {
        return hrMapper.deleteHr(hrId);
    }

    public List<Hr> getAllHrExceptAdmin() {
        return hrMapper.getAllHr(HrUtils.getCurrentHr().getId());
    }
    public List<Hr> getAllHr() {
        return hrMapper.getAllHr(null);
    }
    
    public int newOperator(Hr hr) {
    	//如果用户名存在，返回错误
        if (hrMapper.loadUserByUsername(hr.getUsername()) != null) {
            return -1;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode(hr.getPassword());
        hr.setPassword(encode);
        hrMapper.newOperator(hr);
        return Integer.parseInt(String.valueOf(hr.getId()));
    }

    public List<Hr> getOperatorList() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat s = new SimpleDateFormat("HH:mm:ss");
        String rank = "";

        try {
            rank = sdf.format(new Date());
            Date dtCur = s.parse(s.format(new Date()));
            Date dtObj = s.parse("12:30:00");
            if (dtCur.getTime() > dtObj.getTime()) { //下午班
                rank = rank + "02";
            } else {
                rank = rank + "01";
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    	return hrMapper.getOperatorList(rank);
    }

    public int uploadAvator(String userface) {
        return hrMapper.updateUserFace(Integer.parseInt(String.valueOf(HrUtils.getCurrentHr().getId())),userface);
    }

    /**
     * 修改密码
     * @param oldpass
     * @param newpass
     * @return
     */
    public int updatePassword(String oldpass, String newpass) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        /*String oldpassword = encoder.encode(oldpass);
        if (hrMapper.getHrByPassword(HrUtils.getCurrentHr().getUsername(), oldpassword) == null) {
            return -1;
        }*/

        String newpassword = encoder.encode(newpass);
        return hrMapper.updatePassword(Integer.parseInt(String.valueOf(HrUtils.getCurrentHr().getId())), newpassword);
    }

    public List<Hr> getRyList() {
        return hrMapper.getRyList();
    }
}
