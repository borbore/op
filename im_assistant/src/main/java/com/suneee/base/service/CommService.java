/**  
 * File Name:CommService.java  
 * Package Name:com.suneee.project.service  
 * Description: (That's the purpose of the file)
 * Date:2014年10月10日下午5:51:16  
 * Copyright (c) 2014, forint.lee@qq.com All Rights Reserved.  
 *  
*/  
  
package com.suneee.base.service;  

import java.util.List;

import com.suneee.base.util.Pager;


/**  
 * ClassName:CommService <br/>  
 * Description:That's the purpose of the class
 * Date:     2014年10月10日 下午5:51:16 <br/>  
 * @author   Forint  
 * @version  V1.0  
 * @see        
 */
public interface CommService<T> {

	public void add(T obj) throws Exception;
	
	public void del(T obj) throws Exception;
	
	public void upd(T obj) throws Exception;
	
	public void del(T obj,Integer id) throws Exception;
	
	public T get(T obj,Integer id);
	
	public T get(T obj);
	
	public List<T> getAll(Pager page,T obj);
}
  
