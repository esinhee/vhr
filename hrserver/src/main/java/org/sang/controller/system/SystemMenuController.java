package org.sang.controller.system;

import java.util.List;

import org.sang.bean.Menu;
import org.sang.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/***
 * 后台导航菜单管理
 * @author qhee
 */
@RestController
@RequestMapping("/system/menu")
public class SystemMenuController {
	@Autowired
	MenuService menuService;

	@RequestMapping("/all")
	public List<Menu> allMenus() {
		return menuService.getAllMenu();
	}
}
