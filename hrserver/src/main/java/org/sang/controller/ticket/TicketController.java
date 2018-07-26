package org.sang.controller.ticket;

import java.io.File;
import java.lang.reflect.Array;
import java.nio.file.FileSystems;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.formula.functions.Even;
import org.sang.bean.*;
import org.sang.common.HrUtils;
import org.sang.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ticket")
public class TicketController {
	@Autowired
	TicketTypeService ttService;
	
	@Autowired
	HrService hrService;
	
	@Autowired
	StaffService staffServcie;
	
	@Autowired
	TicketService ticketServie;
	
	@Autowired
	AttachService attachService;

	@Autowired
	EventProcessService eventProcessService;

	@Autowired
	TicketStatusService ticketStatusService;

	@Autowired
	TicketTypeService ticketTypeService;
	
	@RequestMapping(value = "/ttlist", method = RequestMethod.GET)
	public List<EventType> getEventTypeList() {
		return ttService.getTypeList();
	}

	@RequestMapping(value = "/tttoplist/{pid}", method = RequestMethod.GET)
	public List<EventType> getTopTypeList(@PathVariable Integer pid) {
		return ticketTypeService.selectTypeByParentId(pid);
	}
	
	@RequestMapping(value = "/stafflist", method = RequestMethod.GET)
	public List<Staff> getAllStaff() {
		return staffServcie.getAllStaff();
	}
	
	@RequestMapping(value = "/oplist", method = RequestMethod.GET)
	public List<Hr> getOperatorList() {
		return hrService.getOperatorList();
	}

	@RequestMapping(value = "/rylist", method = RequestMethod.GET)
	public List<Hr> getRyList() {
		return hrService.getRyList();
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Map<String, Object> addEvent(Event et) {
		Map<String, Object> map = new HashMap<>();
		if (et != null) {
			ticketServie.addTicket(et);
			map.put("status", "success");
			map.put("msg", et.getEid());
			return map;
		}
		map.put("status", "error");
		map.put("msg", "添加失败!");
		return map;
	}
	
	@RequestMapping(value = "/tlto", method = RequestMethod.GET)
	public List<Event> getTodayTicketList() {
		return ticketServie.getTodayTicketList();
	}

	@RequestMapping(value = "/tlundo", method = RequestMethod.GET)
	public List<Event> fetchUndoTicketList() {
		return ticketServie.fetchUndoEventList(7);
	}

	@RequestMapping(value = "/tkrefuse", method = RequestMethod.POST)
	public RespBean refuseTicket() {
		return new RespBean("error", "更新失败!");
	}
	
	/**
	 * 文件上传
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/fileUpload", method = RequestMethod.POST/*, produces = { MediaType.TEXT_PLAIN_VALUE }*/)
	public Map<String, Object> upload(@RequestParam("file") MultipartFile file, @RequestParam("cid") int cid) {
		Map<String, Object> map = new HashMap<>();
		if (file == null || file.isEmpty()) {	//判断文件是否存在
			map.put("status", "error");
			map.put("msg", "请选择上传文件!");
			return map;
		}
		String separator = "/";//FileSystems.getDefault().getSeparator();

		try {
			//获取跟目录
			//File path = new File(ResourceUtils.getURL("classpath:").getPath());
			File path = new File("/home/panuser/www");
			if(!path.exists()) path = new File("");
			String strCurDir = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
			String curDir = "uploadfiles/" + strCurDir;
			String subDir = "static/";
			File directory = new File(path.getAbsolutePath(),subDir + curDir);
			if(!directory.exists()) directory.mkdirs();

			String fileName = file.getOriginalFilename();
			String sfile = fileName.substring(0, fileName.lastIndexOf("."));
			// 获取文件的后缀名
	        String suffixName = fileName.substring(fileName.lastIndexOf("."));
	        String tmppath = curDir + separator + fileName;
	        File localFile = new File(path.getAbsolutePath(),subDir + tmppath);
	        //判断文件是否存在，存在则重命名
			for (int i=1; localFile.exists() && i < Integer.MAX_VALUE; i++) {
				tmppath= curDir + separator + sfile + '(' + i + ')' + suffixName;
				 localFile = new File(path.getAbsolutePath(), subDir + tmppath);
			}
	        /*while (!localFile.exists()) {
	        	fNum++;
	        	sfile = sfile + "_" + fNum + suffixName;
	        	tmppath = directory + separator + sfile;
	        	localFile = new File(tmppath);
	        }*/
	        file.transferTo(localFile);
	        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        //重新生成UUID，用于标识文件，以便修改和删除操作
            UUID uuid = UUID.nameUUIDFromBytes((df.format(new Date()) + "-"+ System.currentTimeMillis()).getBytes());
            Long s = localFile.length();
            Attach at = new Attach();
            at.setAtname(sfile);
            at.setAtpath(tmppath);
            at.setAtsize(s.toString());
            at.setAttype(suffixName);
            at.setAtuuid(uuid.toString());
            at.setUploaddate(sdf.parse(sdf.format(new Date())));
            at.setUploadid(Integer.parseInt(String.valueOf(HrUtils.getCurrentHr().getId())));
            at.setUploadby(HrUtils.getCurrentHr().getName());
            at.setCid(cid);
            int ret = attachService.addAttach(at);
            if (ret == 1) {
				map.put("status", "success");
				map.put("msg", at);
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

	/**
	 * 附件关联主体对象
	 * @param eid
	 * @param atList
	 * @return
	 */
	@RequestMapping(value = "/releated", method = RequestMethod.POST)
	@ResponseBody
	public int releateAttach(Integer eid, Integer[] atList ) {
		return attachService.releatedAttach(eid, Arrays.asList(atList));
	}

	@RequestMapping(value = "/tkcount", method = RequestMethod.GET)
	public Map<String, Object> getTicketCount() {
		return ticketServie.countTicket();
	}
	
	@RequestMapping(value = "/tkinfo/{eid}", method = RequestMethod.GET)
	public Map<String, Object> getTicketInfo(@PathVariable Integer eid) {
		Map<String, Object> map = new HashMap<>();
		Event et = ticketServie.getTicketInfo(eid);
		if (et != null) {
			map.put("status", "success");
			map.put("msg", et);
			return map;
		}
		map.put("status", "error");
		map.put("msg", "无法获取工单信息！");
		return map;
	}

	/**
	 * 发布处理结果，未完成
	 * @param ep
	 * @return
	 */
	@RequestMapping(value = "/newep", method = RequestMethod.POST)
	public RespBean newEventProcess(EventProcess ep) {
		int ret = eventProcessService.newEventProcess(ep, 3);
		if (ret != 0) {
			return new RespBean("suceess","发布成功！");
		}
		return new RespBean("error","操作失败！");
	}

	/**
	 * 发布处理结果，完成
	 * @param ep
	 * @return
	 */
	@RequestMapping(value = "/newepc", method = RequestMethod.POST)
	public RespBean complateProcess(EventProcess ep) {
		int ret = eventProcessService.newEventProcess(ep, 7);
		if (ret != 0) {
			return new RespBean("suceess","发布成功！");
		}
		return new RespBean("error","操作失败！");
	}

	@RequestMapping(value = "/tp/{id}", method = RequestMethod.GET)
	public List<EventProcess> getProcessList(@PathVariable Integer id) {
		return eventProcessService.selectProcessList(id);
	}

	@RequestMapping(value = "/tl", method = RequestMethod.GET)
	public Map<String, Object> getTodayTicketList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
		Map<String, Object> map = new HashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dt = null;
		String startDate = "";
		String endDate = "";
		try {
			startDate = sdf.format(new Date());
			endDate = sdf.format(new Date());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Event> tl = ticketServie.getTodayTicketList(startDate, endDate, page, size);
		int count = ticketServie.getTicketCount(startDate, endDate);
		map.put("tlist", tl);
		map.put("count", count);
		return map;
	}

	@RequestMapping(value = "/reassign", method = RequestMethod.PUT)
	public RespBean reassign(Integer eid, Integer userid, String name) {
		if (ticketServie.Reassign(eid, userid, name) != 0) {
			return new RespBean("suceess","分配成功！");
		}
		return new RespBean("error","分配失败！");
	}

	@RequestMapping(value = "/getstatuss", method = RequestMethod.GET)
	public List<TicketStatus> getStatusList() {
		return ticketStatusService.getStatusList();
	}

	@RequestMapping(value = "/searchtk", method = RequestMethod.GET)
	public Map<String, Object> searchTicket(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, Integer grade, Integer tktype, Integer tkstatus, Integer dealer, String beginDate) {
		Map<String, Object> map = new HashMap<>();
		List<Event> tlist = ticketServie.searchTicket(page, size, grade, tktype, tkstatus, dealer, beginDate);
		int count = ticketServie.getCountByKeywords(grade, tktype, tkstatus, dealer, beginDate);
		map.put("tlist", tlist);
		map.put("count", count);
		return map;
	}
}
