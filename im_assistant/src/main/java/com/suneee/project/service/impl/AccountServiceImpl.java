package com.suneee.project.service.impl;  

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suneee.base.exception.ServiceException;
import com.suneee.base.util.Pager;
import com.suneee.project.dao.mybatis.AccountMapper;
import com.suneee.project.model.Account;
import com.suneee.project.model.AccountExample;
import com.suneee.project.service.IAccountService;

/**  
 * @author   rcj  
 * @version  V1.0  
 * @see        
 */
@Service
public class AccountServiceImpl implements IAccountService {

	@Autowired
	private AccountMapper accountMapper;
	
	
	public List<Account> getAllAccount(AccountExample example) {
		example.setPageScale("999999");
		return accountMapper.selectByExample(example);
	}

	public List<Account> getAll(Pager page, Account obj) {
		AccountExample AccountExample=new AccountExample(); //这里可以设置该对象的查询条件
		AccountExample.setPageNo(page.getPageNo());//当前页
		AccountExample.setPageScale(page.getPageScale());//页大小
		
		int count=accountMapper.countByExample(AccountExample);
		page.setTotalRows(count);
		AccountExample.setTotalRows(count);
		return accountMapper.selectByExample(AccountExample);
	}


	@Transactional
	public void add(Account obj) throws ServiceException {
		accountMapper.insertSelective(obj);
		//throw new ServiceException("主动抛出业务异常！");
	}

	@Transactional
	public void del(Account obj) throws Exception {
		accountMapper.deleteByPrimaryKey(obj.getUsername());
	}

	public void upd(Account obj) throws Exception {
		accountMapper.updateByPrimaryKey(obj);
		
	}
	public Account get(Account obj) {
		return accountMapper.selectByPrimaryKey(obj.getUsername()); 
	}

	public void del(Account obj, String id) throws Exception {
		if (obj.getUsername()==null) {
			accountMapper.deleteByPrimaryKey(id);
		}else{
			accountMapper.deleteByPrimaryKey(obj.getUsername());
		}
	}

	public Account get(Account obj, String id) {
		if (obj.getUsername()==null) {
			return accountMapper.selectByPrimaryKey(id); 
		}else{
			return accountMapper.selectByPrimaryKey(obj.getUsername()); 
		}
	}

	public void del(Account obj, Integer id) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public Account get(Account obj, Integer id) {
		// TODO Auto-generated method stub
		return null;
	}


}
  
