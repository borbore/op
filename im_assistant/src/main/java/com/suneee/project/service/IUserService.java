/**  
 * File Name:UserService.java  
 * Package Name:com.suneee.project.service  
 * Description: (That's the purpose of the file)
 * Date:2014年10月9日下午3:34:42  
 * Copyright (c) 2014, forint.lee@qq.com All Rights Reserved.  
 *  
*/  
  
package com.suneee.project.service;  


import java.util.List;

import com.suneee.base.service.CommService;
import com.suneee.project.model.User;
import com.suneee.project.model.UserExample;

/**  
 * ClassName:UserService <br/>  
 * Description:That's the purpose of the class
 * Date:     2014年10月9日 下午3:34:42 <br/>  
 * @author   Forint  
 * @version  V1.0  
 * @see        
 */
public interface IUserService extends CommService<User> {
	public List<User> getAllUser(UserExample example);
}
  
