/**  
 * File Name:UserServiceImpl.java  
 * Package Name:com.suneee.project.service.impl  
 * Description: (That's the purpose of the file)
 * Date:2014年10月9日下午3:40:49  
 * Copyright (c) 2014, forint.lee@qq.com All Rights Reserved.  
 *  
*/  
  
package com.suneee.project.service.impl;  

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suneee.base.exception.ServiceException;
import com.suneee.base.util.Pager;
import com.suneee.project.dao.mybatis.UserMapper;
import com.suneee.project.model.User;
import com.suneee.project.model.UserExample;
import com.suneee.project.service.IUserService;

/**  
 * ClassName:UserServiceImpl <br/>  
 * Description:That's the purpose of the class
 * Date:     2014年10月9日 下午3:40:49 <br/>  
 * @author   Forint  
 * @version  V1.0  
 * @see        
 */
@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private UserMapper userMapper;
	
	public List<User> getAllUser(UserExample example) {
		example.setPageScale("999999");
		return userMapper.selectByExample(example);
	}

	public List<User> getAll(Pager page, User obj) {
		UserExample userExample=new UserExample(); //这里可以设置该对象的查询条件
		userExample.setPageNo(page.getPageNo());//当前页
		userExample.setPageScale(page.getPageScale());//页大小
		
		int count=userMapper.countByExample(userExample);
		page.setTotalRows(count);
		userExample.setTotalRows(count);
		return userMapper.selectByExample(userExample);
	}


	@Transactional
	public void add(User obj) throws ServiceException {
		userMapper.insert(obj);
		throw new ServiceException("主动抛出业务异常！");
	}

	@Transactional
	public void del(User obj) throws Exception {
		userMapper.deleteByPrimaryKey(obj.getUserid());
	}

	public void upd(User obj) throws Exception {
		userMapper.updateByPrimaryKey(obj);
		
	}
	public User get(User obj) {
		return userMapper.selectByPrimaryKey(obj.getUserid()); 
	}

	public void del(User obj, Integer id) throws Exception {
		if (obj.getUserid()==null) {
			userMapper.deleteByPrimaryKey(id);
		}else{
			userMapper.deleteByPrimaryKey(obj.getUserid());
		}
	}

	public User get(User obj, Integer id) {
		if (obj.getUserid()==null) {
			return userMapper.selectByPrimaryKey(id); 
		}else{
			return userMapper.selectByPrimaryKey(obj.getUserid()); 
		}
	}

}
  
