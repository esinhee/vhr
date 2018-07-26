package org.sang.controller.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sang.bean.EventType;
import org.sang.bean.Keshi;
import org.sang.bean.RespBean;
import org.sang.service.KeshiService;
import org.sang.service.TicketTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/config")
public class SystemConfigController {
	@Autowired
    KeshiService keshiService;
    @Autowired
    TicketTypeService ticketTypeService;
	
	@RequestMapping(value = "/kslist", method = RequestMethod.GET)
    public List<Keshi> getKeshiList() {
    	return keshiService.getKeshiList();
    }
    
    @RequestMapping(value = "/ksplist/{pid}", method = RequestMethod.GET)
    public List<Keshi> getKeshiByParentid(@PathVariable String pid) {
    	return keshiService.getKeshiByParentId(pid);
    }
    
    /**
     * 添加一级科室
     * @return
     */
    @RequestMapping(value = "/addkeshi", method = RequestMethod.POST)
    public Map<String, Object> addTopKeshi(Keshi ks) {
    	Map<String, Object> map = new HashMap<>();
    	if (keshiService.addKeshi(ks) == 1) {
    		map.put("status", "success");
            map.put("msg", ks);
            return map;
    	}
    	map.put("status", "error");
        map.put("msg", "添加失败!");
        return map;
    }

    @RequestMapping(value = "/chang", method = RequestMethod.PUT)
    public RespBean changeKeshiStatus(String kid, Integer enabled) {
        if (keshiService.changeKeshi(kid, enabled) == 1) {
            return new RespBean("success", "修改成功!");
        }
        return new RespBean("error", "修改失败!");
    }

    @RequestMapping(value = "/tkttypelist", method = RequestMethod.GET)
    public List<EventType> getTicketTypeList() {
        return ticketTypeService.getTypeList();
    }

    @RequestMapping(value = "/tttoplist/{pid}", method = RequestMethod.GET)
    public List<EventType> getTopTypeList(@PathVariable Integer pid) {
        return ticketTypeService.selectTypeByParentId(pid);
    }

    @RequestMapping(value = "/tkttype", method = RequestMethod.POST)
    public Map<String, Object> addTypeName(EventType et) {
        Map<String, Object> map = new HashMap<>();
        int ret = ticketTypeService.insertSelective(et);
        if (ret > 0) {
            map.put("status", "success");
            map.put("msg", et);
            return map;
        } else if (ret == -1) {
            map.put("status", "error");
            map.put("msg", "工单类型已经存在，添加失败!");
            return map;
        }
        map.put("status", "error");
        map.put("msg", "添加失败!");
        return map;
    }
}
