package com.suneee.project.service;  


import java.util.List;

import com.suneee.base.service.CommService;
import com.suneee.project.model.Account;
import com.suneee.project.model.AccountExample;

/**  
 * @author   rcj  
 * @version  V1.0  
 * @see        
 */
public interface IAccountService extends CommService<Account> {
	public List<Account> getAllAccount(AccountExample example);
}
  
