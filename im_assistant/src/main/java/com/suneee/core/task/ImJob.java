package com.suneee.core.task;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.suneee.base.service.CommService;
import com.suneee.core.realize.ImConstants;
import com.suneee.core.realize.ImStarter;
import com.suneee.project.model.Account;
import com.suneee.project.service.IAccountService;

public class ImJob {
	
	
	private static final String LOAD_PROPERTIES = "SELECT propValue FROM ofProperty where name='xmpp.domain'";
	
	
	@Autowired
	private IAccountService iAccountService;
	protected CommService<Account> getCommService() {
		return iAccountService;
	}
	
	private static JdbcTemplate jdbcTemplate;   
	
	private int index=0;
	
	/**
	 * Function:定时检查机器助手启动情况
	 * 
	 * @author RCJ
	 */
	public void checkIm() {
		//System.out.println("定时器启动次数："+index+"--时间:" +System.currentTimeMillis() +"----------登录数："+ImStarter.getImConnectionMap().size());
		CheckService();
		new ImStarter(iAccountService);
	}
	
	
	public static void CheckService(){
		if("".equals(ImStarter.serviceName)){
			List<Map<String,Object>> lis=jdbcTemplate.queryForList(LOAD_PROPERTIES);
			Map<String,Object> map =lis.get(0);
			ImStarter.serviceName=ImConstants.AT+map.get("propValue");
		}
	}
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}
