/**  
 * File Name:ServiceException.java  
 * Package Name:com.suneee.base.exception  
 * Description: (That's the purpose of the file)
 * Date:2014年10月16日上午9:53:10  
 * Copyright (c) 2014, forint.lee@qq.com All Rights Reserved.  
 *  
*/  
  
package com.suneee.base.exception;  
/**  
 * ClassName:ServiceException <br/>  
 * Description:业务处理异常
 * Date:     2014年10月16日 上午9:53:10 <br/>  
 * @author   Forint  
 * @version  V1.0  
 * @see        
 */
public class ServiceException extends RuntimeException {
	/**  
	 * serialVersionUID:TODO(That's the purpose of the variable).  
	 */
	private static final long serialVersionUID = 546659255074821720L;

	public ServiceException(String message) {
		super(message);
	}
	
	public ServiceException(Throwable cause) {
		super(cause);
	}
	
	public ServiceException(String message,Throwable cause){
		super(message,cause);
	}
}
  
