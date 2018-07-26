package org.sang.controller;

import org.sang.bean.RespBean;
import org.sang.service.HrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sang on 2017/12/29.
 */
@RestController
@RequestMapping("/personnel")
public class PersonnelController {
    @Autowired
    HrService hrService;

    @RequestMapping("/")
    public String hello() {
        return "hello";
    }
    @RequestMapping("/emp/hello")
    public String helloEmp() {
        return "hello emp";
    }

    /**
     * 头像修改
     * @param file
     * @return
     */
    @RequestMapping(value = "/avator", method = RequestMethod.POST)
    public Map<String, Object> uploadAvator(@RequestParam("file") MultipartFile file) {
        Map<String, Object> map = new HashMap<>();
        if (file == null || file.isEmpty()) {	//判断文件是否存在
            map.put("status", "error");
            map.put("msg", "请选择上传文件!");
            return map;
        }
        String separator = "/";
        try {
            //获取跟目录
            //File path = new File(ResourceUtils.getURL("classpath:").getPath());
            File path = new File("/home/panuser/www");
            if(!path.exists()) path = new File("");
            String curDir = "userface/";
            File directory = new File(path.getAbsolutePath(),"static/" + curDir);
            if(!directory.exists()) directory.mkdirs();

            String fileName = file.getOriginalFilename();
            String sfile = fileName.substring(0, fileName.lastIndexOf("."));
            // 获取文件的后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            String newname = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String tmppath = curDir + sfile + "_" + newname + suffixName;
            File localFile = new File(path.getAbsolutePath(),"static/" + tmppath);
            file.transferTo(localFile);
            int ret = hrService.uploadAvator(tmppath);
            if (ret == 1) {
                map.put("status", "success");
                map.put("msg", tmppath);
                return map;
            }
            map.put("status", "error");
            map.put("msg", "文件上传失败!");
            return map;
        } catch (Exception e) {
            map.put("status", "error");
            map.put("msg", "未知异常发生!");
            return map;
        }
    }

    @RequestMapping(value = "/resetpass", method = RequestMethod.POST)
    public RespBean modifyPassword(String opwd, String npwd) {
        if (opwd == npwd) {
            return new RespBean("error", "新密码不能和原始密码一样！");
        } else if (opwd.isEmpty() || npwd.isEmpty()) {
            return new RespBean("error", "输入错误！");
        }
        int ret = hrService.updatePassword(opwd, npwd);
        if (ret == 1) {
            return new RespBean("success", "密码修改成功！");
        }
        return new RespBean("error", "密码修改失败！");
    }
}
