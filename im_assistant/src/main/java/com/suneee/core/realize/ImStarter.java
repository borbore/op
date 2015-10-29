package com.suneee.core.realize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Presence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.suneee.core.util.AssertUtil;
import com.suneee.core.util.ImGlobals;
import com.suneee.core.util.LogUtil;
import com.suneee.project.model.Account;
import com.suneee.project.model.AccountExample;
import com.suneee.project.service.IAccountService;
public class ImStarter implements Runnable {

	private static Logger log = LoggerFactory.getLogger(ImStarter.class); 

	/**
	 * key为登陆的用户名
	 */
	private static Map<String, Connection> imConnectionMap = new ConcurrentHashMap<String ,Connection>();
	
	
	private static List<Map<String, String>> imList = new ArrayList<Map<String, String>>();
	
	private Map<String, String> imMap;
	

	public  static Map<String, Connection> getImConnectionMap() {
		return imConnectionMap;
	}

	public static List<Map<String, String>> getImList() {
		return imList;
	}

	public static void setImList(List<Map<String, String>> imList) {
		ImStarter.imList = imList;
	}

	public ImStarter(Map<String, String> map) {
		this.imMap = map;
	}

	
	public static String serviceName = "";
	
	
	@Autowired
	private static  IAccountService iAccountService;
	
	public  ImStarter(IAccountService iAccountService){
		ImStarter.setiAccountService(iAccountService);
		ImStarter.start();
	}
	
	
	
	
	public static void start() {	
		if(imConnectionMap.size()>0){
			return;
		}
		List<Account> lsac =getiAccountService().getAllAccount(new  AccountExample());
		List<Map<String, String>>  imAccount = new ArrayList<Map<String,String>>();
		Map<String, String> romap = null;
		String oldJid="";
		for (int i = 0; i < lsac.size(); i++) {
			Account acc = lsac.get(i);
			oldJid=acc.getUsername()+ImStarter.serviceName;
			if(imConnectionMap.containsKey(oldJid)){
				continue;
			};
			
			romap = new HashMap<String, String>();
			romap.put(ImConstants.JID, oldJid);
			romap.put(ImConstants.USER_PASSWORD, acc.getPlainPassword());
			romap.put(ImConstants.SERVER_NAME, ImGlobals.getXMLProperty(ImConstants.SERVER_NAME));
			romap.put(ImConstants.SERVER_PORT, ImGlobals.getXMLProperty(ImConstants.SERVER_PORT));
			romap.put(ImConstants.VCODE, acc.getvCode());
			imAccount.add(romap);
		}
		//List<Map<String, String>>  ImAccount2 =   ImGlobals.getImXMLProperties("ImAccount");
	    int ImStartCount = imAccount.size();
	    if(ImStartCount>0){
	    	ExecutorService   executor = Executors.newFixedThreadPool(ImStartCount);
			for(int i = 0 ; i < ImStartCount ; i++){		
				executor.execute(new ImStarter((Map<String, String>)imAccount.get(i)));
			}
	    }
	}


	public void run() {
		try {
			//Connection.DEBUG_ENABLED = true;
//			ConnectionConfiguration config = new ConnectionConfiguration(servername, serverport);
			ConnectionConfiguration config;
			String jid = AssertUtil.isNotEmpty(ImConstants.JID, (String)imMap.get(ImConstants.JID));
			String password = AssertUtil.isNotEmpty(ImConstants.USER_PASSWORD, (String)imMap.get(ImConstants.USER_PASSWORD));
			String servername = (String)imMap.get(ImConstants.SERVER_NAME);
			String serverport = (String)imMap.get(ImConstants.SERVER_PORT);
			String presenceStatus = (String)imMap.get(ImConstants.PRESENCE_STATUS);
//			AssertUtil.isLegalOfLicence(jid);
			String[] strs = jid.split(ImConstants.AT);
			String username = strs[0];
			String serviceName = strs[1];			
			//如果servername为空，则截取username @后面的字符中做为servername
			if(StringUtils.isEmpty(servername)){
				servername = serviceName;
			}
			if(StringUtils.isNotEmpty(serverport)){
				 config = new ConnectionConfiguration(servername, Integer.parseInt(serverport));
			}else{
		    	 config = new ConnectionConfiguration(servername);
			}
			/**
			 * domain
			 */
			config.setServiceName(serviceName);
			XMPPConnection connection = new XMPPConnection(config);
			connection.connect();
//			String resource = LicenseManager.getInstance().getLicInfo(ImConstants.XMPP_RESOURCE);
			String resource = "IM助手";
			connection.login(username, password, resource);
			imConnectionMap.put(jid, connection);
			
			//如果presenceStatus不为空，则发送presenceStatus
			if(StringUtils.isNotEmpty(presenceStatus)){
				ImGlobals.sendPresenceStatus(connection, presenceStatus);
			}
			
			connection.getRoster().setSubscriptionMode(Roster.SubscriptionMode.manual);
			connection.addPacketListener(new ImPacketListener(connection), new PacketTypeFilter(Presence.class));
			
			
			ChatManager chatManager = connection.getChatManager();
			//ChatListener chatListener
			chatManager.addChatListener(new ChatManagerListener() {
				public void chatCreated(Chat chat, boolean createdLocally) {
					if (!createdLocally)
						chat.addMessageListener(new ImMessageListener());
				}
			});
			//记录VCODE用list
			imList.add(imMap);
			
			log.info("Im " + username + " started");
		} catch (XMPPException e) {
			LogUtil.error(" Exception : " , e, log);
		} catch (Exception e) {
			LogUtil.error(e, log);
		}
	}
	
	public static void main(String[] args){
		ImStarter.start();
	}

	public static IAccountService getiAccountService() {
		return iAccountService;
	}

	public static void setiAccountService(IAccountService iAccountService) {
		ImStarter.iAccountService = iAccountService;
	}
}
