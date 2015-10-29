/**  
 * File Name:LoginController.java  
 * Package Name:com.suneee.project.web.controller  
 * Description: (That's the purpose of the file)
 * Date:2014年10月9日上午9:20:06  
 * Copyright (c) 2014, forint.lee@qq.com All Rights Reserved.  
 *  
*/  
  
package com.suneee.project.web.controller;  

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.suneee.base.service.CommService;
import com.suneee.base.web.controller.BaseController;
import com.suneee.project.model.User;
import com.suneee.project.model.UserExample;
import com.suneee.project.service.IUserService;

/**  
 * ClassName:LoginController <br/>  
 * Description:That's the purpose of the class
 * Date:     2014年10月9日 上午9:20:06 <br/>  
 * @author   Forint  
 * @version  V1.0  
 * @see        
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController<User>{
	private static Logger log = LoggerFactory.getLogger(UserController.class);  
	@Autowired
	private IUserService iUserService;
	
	
	@RequestMapping("/list/show")
	public ModelAndView getAll(){
		log.info("-------get all users----------");
		Map<String,List<User>> m_list=new HashMap<String,List<User>>();
		m_list.put("list",iUserService.getAllUser(new UserExample()));
		ModelAndView mv = new ModelAndView("users/list");
		mv.addAllObjects(m_list);
		return mv;
	}
	
	@RequestMapping(value="/test",method=RequestMethod.POST)
	public @ResponseBody Map<String, Object> testPostJson(
	        @RequestBody  User userForm) {
	         
	    Map<String, Object> map = new HashMap<String, Object>();
/*	    if (bindingResult.hasErrors()) {
	        map.put("errorCode", "40001");
	        map.put("errorMsg", bindingResult.getFieldError().getDefaultMessage());
	    }*/
	     
	    map.put("user", userForm);
	    return map;
	}
	@Override
	protected String getPrefix() {
		return "users";
	}
	@Override
	protected CommService<User> getCommService() {
		return iUserService;
	}
}
  
