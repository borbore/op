package com.suneee.project.web.controller;  

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
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
import com.suneee.core.realize.ImConstants;
import com.suneee.core.util.AssertUtil;
import com.suneee.project.model.Account;
import com.suneee.project.model.AccountExample;
import com.suneee.project.service.IAccountService;

/**  
 * @author   rcj  
 * @version  V1.0  
 * @see        
 */
@Controller
@RequestMapping("/account")
public class AccountController extends BaseController<Account>{
	private static Logger log = LoggerFactory.getLogger(AccountController.class);  
	@Autowired
	private IAccountService iAccountService;
	
	public final ObjectMapper mapper = new ObjectMapper();
	
	private String PATH_ACCOUNTS="coaxial/views/";
	
	@RequestMapping("/main")
	public ModelAndView main(){
		ModelAndView mv = new ModelAndView(PATH_ACCOUNTS+"main");
		return mv;
	}
	
	@RequestMapping("/add-account")
	public ModelAndView addAccount(){
		ModelAndView mv = new ModelAndView(PATH_ACCOUNTS+"add-accounts");
		return mv;
	}
	
	
	@RequestMapping("/add")
	public ModelAndView add(){
		Map<String,List<Account>> m_list=new HashMap<String,List<Account>>();
		m_list.put("list",iAccountService.getAllAccount(new AccountExample()));
		ModelAndView mv = new ModelAndView(PATH_ACCOUNTS+"accounts");
		mv.addAllObjects(m_list);
		return mv;
	}
	
	@RequestMapping("/adds")
	public ModelAndView add(Account  acc) throws Exception{
		iAccountService.add(acc);
		ModelAndView mv = new ModelAndView(PATH_ACCOUNTS+"accounts");
		return mv;
	}
	
	
	
	@RequestMapping(value = "/addAcc", method = RequestMethod.POST)
	@ResponseBody
	public Object addAcc(@RequestBody String accBody) throws Exception{
		log.info(accBody);
		String result=AssertUtil.resultSet(ImConstants.SUCCESS);
		Account acc=null;
		try {
			AssertUtil.isNotEmpty("body", accBody);
			JsonNode root = mapper.readTree(accBody);
			acc = new Account();
			AssertUtil.isNotEmpty("username",root.path("username").asText());
			acc.setUsername("c_"+root.path("username").asText().toLowerCase());
			acc.setNickname(root.path("nickname").asText());
			acc.setIntro(root.path("intro").asText());
			acc.setType(root.path("type").asText());
			acc.setBrand(root.path("brand").asText());
			acc.setHotline(root.path("hotline").asText());
			acc.setBusiness(root.path("business").asText());
			///AssertUtil.isNotEmpty("plainPassword",root.path("plainPassword").asText());
			acc.setPlainPassword("123456");
			acc.setName(root.path("name").asText());
			acc.setEmail(root.path("email").asText());
			acc.setEncryptedPassword(root.path("encryptedPassword").asText());
			acc.setCreationDate(root.path("creationDate").asText());
			acc.setModificationDate(root.path("modificationDate").asText());
			AssertUtil.isNotEmpty("vCode",root.path("vCode").asText());
			acc.setvCode(root.path("vCode").asText());
			iAccountService.add(acc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			result= AssertUtil.error(e.getMessage());
		}
		return result;
	}
	
	
	@RequestMapping("/index")
	public ModelAndView index(){
		Map<String,List<Account>> m_list=new HashMap<String,List<Account>>();
		m_list.put("list",iAccountService.getAllAccount(new AccountExample()));
		ModelAndView mv = new ModelAndView(PATH_ACCOUNTS+"index");
		mv.addAllObjects(m_list);
		return mv;
	}
	
	
	@RequestMapping(value="/test",method=RequestMethod.POST)
	public @ResponseBody Map<String, Object> testPostJson(
	        @RequestBody  Account accountForm) {
	         
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("account", accountForm);
	    return map;
	}
	@Override
	protected String getPrefix() {
		return "accounts";
	}
	@Override
	protected CommService<Account> getCommService() {
		return iAccountService;
	}
}
  
