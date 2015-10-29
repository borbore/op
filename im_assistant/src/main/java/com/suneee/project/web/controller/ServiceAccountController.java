package com.suneee.project.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smackx.packet.VCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suneee.base.service.CommService;
import com.suneee.core.realize.ImConstants;
import com.suneee.core.realize.ImMessageListener;
import com.suneee.core.realize.ImStarter;
import com.suneee.core.task.ImJob;
import com.suneee.core.util.AssertUtil;
import com.suneee.core.util.ImGlobals;
import com.suneee.project.model.Account;
import com.suneee.project.service.IAccountService;

@Controller
@RequestMapping("{serviceNoJID}/api")
public class ServiceAccountController {

	public final ObjectMapper mapper = new ObjectMapper();
	@Autowired
	private IAccountService iAccountService;
	private static Logger log = LoggerFactory.getLogger(ServiceAccountController.class);

	/**
	 * 
	 * @param request
	 * @return 获取订阅用户列表
	 * @throws Exception
	 */
	@RequestMapping(value = "/" + ImConstants.GET_ROSTER, method = RequestMethod.POST)
	@ResponseBody
	public Object getSubscribersList(@PathVariable String serviceNoJID,
			@RequestBody String body) throws Exception {
		// 存取返回的json串
		String resultStr = "";
		try {
			//{"vCode":"1112121","type":"both"}
			AssertUtil.isNotEmpty(ImConstants.BODY, body);
			JsonNode root = mapper.readTree(body);
			AssertUtil.isNotEmpty(ImConstants.ROBOTJID, serviceNoJID);
			serviceNoJID=getClentNameAdd(serviceNoJID);
			ImJob.CheckService();
			serviceNoJID=serviceNoJID+ImStarter.serviceName;
			// 判断vCode是否合法
			AssertUtil.isLegal(root.path(ImConstants.VCODE).asText(),getVcode(serviceNoJID));
			Connection conn = AssertUtil.getConnectionByRobotJID(serviceNoJID);
			Roster roster = conn.getRoster();
			RosterEntry entry;
			List<Map<String, String>> lis = new ArrayList<Map<String, String>>();
			for (RosterEntry en : roster.getEntries()) {
				entry = roster.getEntry(en.getUser());
				if (ImConstants.ROSTERENTRY_TYPE_BOTH.equals(entry.getType().name())) {
					Map<String, String> hashMap = new HashMap<String, String>();
					hashMap.put(ImConstants.ROSTERENTRY_NAME, entry.getName());
					hashMap.put(ImConstants.ROSTERENTRY_USER, entry.getUser());
					lis.add(hashMap);
				}
			}
			resultStr = mapper.writeValueAsString(lis);
		} catch (Exception e) {
			resultStr = AssertUtil.error(e.getMessage());
			e.printStackTrace();
		}
		return resultStr;
	}

	/**
	 * 
	 * @param request
	 * @return 获取用户vcard
	 * @throws Exception
	 */
	@RequestMapping(value = "/" + ImConstants.GET_VCARD, method = RequestMethod.POST)
	@ResponseBody
	public Object getUserVcard(@PathVariable String serviceNoJID,
			@RequestBody String body) throws Exception {
		// 存取返回的json串
		String resultStr = "";
		try {
			// {"vCode":"1112121","targetJID":"test2@ruan-pc"}
			AssertUtil.isNotEmpty(ImConstants.BODY, body);
			JsonNode root = mapper.readTree(body);
			AssertUtil.isNotEmpty(ImConstants.ROBOTJID, serviceNoJID);
			serviceNoJID=getClentNameAdd(serviceNoJID);
			ImJob.CheckService();
			serviceNoJID=serviceNoJID+ImStarter.serviceName;
			// 判断vCode是否合法
			AssertUtil.isLegal(root.path(ImConstants.VCODE).asText(),getVcode(serviceNoJID));
			Connection conn = AssertUtil.getConnectionByRobotJID(serviceNoJID);
			String targetJID = root.path(ImConstants.TARGET_JID).asText();
			AssertUtil.isNotEmpty(ImConstants.TARGET_JID, targetJID);
			AssertUtil.isServiceName(targetJID,serviceNoJID);
			VCard vcard = new VCard();
			vcard.load((XMPPConnection) conn, targetJID);
			resultStr = vcard.toString();

		} catch (Exception e) {
			resultStr = AssertUtil.error(e.getMessage());
			e.printStackTrace();
		}
		return resultStr;
	}

	/**
	 * 
	 * @param request
	 * @return 移除订阅关系
	 * @throws Exception
	 */
	@RequestMapping(value = "/" + ImConstants.REMOVE_FRIEND, method = RequestMethod.POST)
	@ResponseBody
	public Object removeSubscribers(@PathVariable String serviceNoJID,
			@RequestBody String body) throws Exception {
		// 存取返回的json串
		String resultStr = "";
		try {
			// {"vCode":"1112121","targetJID":"test2@ruan-pc"}
			AssertUtil.isNotEmpty(ImConstants.BODY, body);
			JsonNode root = mapper.readTree(body);
			AssertUtil.isNotEmpty(ImConstants.ROBOTJID, serviceNoJID);
			serviceNoJID=getClentNameAdd(serviceNoJID);
			ImJob.CheckService();
			serviceNoJID=serviceNoJID+ImStarter.serviceName;
			// 判断vCode是否合法
			AssertUtil.isLegal(root.path(ImConstants.VCODE).asText(),getVcode(serviceNoJID));
			Connection conn = AssertUtil.getConnectionByRobotJID(serviceNoJID);
			String targetJID = root.path(ImConstants.TARGET_JID).asText();
			AssertUtil.isNotEmpty(ImConstants.TARGET_JID, targetJID);
			AssertUtil.isServiceName(targetJID,serviceNoJID);
			AssertUtil.isUnequal("Im JID and targetJID", serviceNoJID,targetJID);
			Roster roster = conn.getRoster();
			for (RosterEntry entry : roster.getEntries()) {
				if (entry.getUser().equals(targetJID)) {
					roster.removeEntry(entry);
					ImGlobals.sendMessage(conn,targetJID);
				}
			}
		} catch (Exception e) {
			resultStr = AssertUtil.error(e.getMessage());
			e.printStackTrace();
		}
		return resultStr;
	}

	/**
	 * 
	 * @param request
	 * @return 订阅消息号信息推送
	 * @throws Exception
	 */
	@RequestMapping(value = "/" + ImConstants.SENDMESSAGE, method = RequestMethod.POST)
	@ResponseBody
	public Object m_sendMessage(@PathVariable String serviceNoJID,
								@RequestBody String body) throws Exception {
		// 存取返回的json串
		String resultStr = "";
		Connection conn;
		try {
			// {"vCode":"1112121","targetJIDs":["210@suneeedev"],"type":" 空  or chat ","scope":"all or subscribed","subject":"消息主题","content":"消息内容","htmlcontent":"消息内容Html"}
			System.out.println("serviceNoJID:"+serviceNoJID+"body:"+body);
			AssertUtil.isNotEmpty(serviceNoJID, serviceNoJID);
			serviceNoJID=getClentNameAdd(serviceNoJID);
			AssertUtil.isNotEmpty(ImConstants.BODY, body);
			JsonNode root = mapper.readTree(body);
			AssertUtil.isNotEmpty(ImConstants.ROBOTJID, serviceNoJID);
			ImJob.CheckService();
			serviceNoJID=serviceNoJID+ImStarter.serviceName;
			// 判断vCode是否合法
			AssertUtil.isLegal(root.path(ImConstants.VCODE).asText(),getVcode(serviceNoJID));
			conn = AssertUtil.getConnectionByRobotJID(serviceNoJID);
			String messageSubject = root.path(ImConstants.MESSAGE_SUBJECT).asText();
			String messageBody = root.path("content").asText();
			JsonNode htmlBody = root.path("htmlcontent");
			
			String typeReq=root.path(ImConstants.ROSTERENTRY_TYPE).asText();
			JsonNode targetJIDs=null;
			//一、消息可以为通知和聊天两种，默认为通知
			if(StringUtils.isEmpty(typeReq)){
				//当type为空时，表示通知，targetJIDs和scope二选一     targetJIDs优先
				Roster roster = conn.getRoster();
				roster.reload();
				targetJIDs = root.path(ImConstants.TARGET_JIDS);
				if (targetJIDs.size() <= 0  || (targetJIDs.size()==1 && StringUtils.isEmpty(targetJIDs.get(0).asText()))) {
					//scope分两种，all表示数据库中所有用户subscribed表示订阅用户 
					String scope =root.path(ImConstants.SCOPE).asText();
					if(ImConstants.SUBSCRIBED.equals(scope)){
						for (RosterEntry entry : roster.getEntries()) {
							if (ImConstants.ROSTERENTRY_TYPE_BOTH.equals(entry.getType().name())) {
								ImGlobals.sendMessage(conn, messageSubject, messageBody,entry.getUser());
							}
						}
					}else if(ImConstants.ALL.equals(scope)){
						for (RosterEntry entry : roster.getEntries()) {
							ImGlobals.sendMessage(conn, messageSubject, messageBody,entry.getUser());
						}
					}else{
						resultStr = AssertUtil.error(" scope is all or subscribed !");
					}
				}else{
					for (int i = 0; i < targetJIDs.size(); i++) {
						AssertUtil.isServiceName(targetJIDs.get(i).asText(),serviceNoJID);
						ImGlobals.sendMessage(conn, messageSubject, messageBody,targetJIDs.get(i).asText());
					}
				}
			}else if(ImConstants.CHAT.equals(typeReq)){
				// 当type为chat时，表示聊天，只能用targetJIDs，且只能有一个JID
				targetJIDs = root.path(ImConstants.TARGET_JIDS);
				if (targetJIDs.size() ==1) {
					AssertUtil.isServiceName(targetJIDs.get(0).asText(),serviceNoJID);
					//AssertUtil.getChatJID(targetJIDs.get(0).asText());
					Map<String, Chat> chatMaps = ImMessageListener.getChatMap();
					Chat chat = chatMaps.get(AssertUtil.getShortName(targetJIDs.get(0).asText()));
					if(chat==null){
						chat = conn.getChatManager().createChat(targetJIDs.get(0).asText(), new ImMessageListener());  
						chatMaps.put(targetJIDs.get(0).asText(), chat);
					}
					
					Message mes = new  Message(); 
					String url = htmlBody.path("url").asText();
                    Integer lastUrlIndex=url.lastIndexOf("/");
					if(htmlBody.size()>0 && lastUrlIndex>0){
						
						final Element x = DocumentHelper.createElement(QName.get("x", "urn:xmpp:suneee:cs:presence:state"));
	                    Element text = x.addElement("text");
	                    text.setText(htmlBody.toString());
	                    
	                    final Element file = DocumentHelper.createElement(QName.get("file", "http://wwww.suneee.com/file-transfer"));
	                    file.addElement("messageId").setText("10000");
	                    file.addElement("isPersistent").setText("true");
	                    file.addElement("name").setText(htmlBody.path("filename").asText());
	                    file.addElement("type").setText(htmlBody.path("type").asText());
	                    file.addElement("size").setText("");
	                    file.addElement("path").setText("");
	                    file.addElement("desc").setText("");
	                    file.addElement("offline").setText("");
	                    file.addElement("zipImage").setText("");
	                    String key=url.substring(lastUrlIndex+1, url.length());
	                    file.addElement("key").setText(key);
	                    file.addElement("key2").setText(key);
	                    file.addElement("status").setText("download");
	                    if(lastUrlIndex==-1){
	                    	lastUrlIndex=0;
	                    }
	                    file.addElement("downloadURL").setText(url.substring(0,lastUrlIndex));
	                    System.out.println(file.asXML());
						mes.addExtension(new PacketExtension() {
							public String toXML() {
								// TODO Auto-generated method stub
								return x.asXML()+file.asXML();
							}
							public String getNamespace() {
								// TODO Auto-generated method stub
								return null;
							}
							public String getElementName() {
								// TODO Auto-generated method stub
								return null;
							}
						});
					}
					
                    /*
                    <messageId>15</messageId>
                    <isPersistent>true</isPersistent>
                    <name>mis_20151009153717546.jpg</name>
                    <type>image/jpeg</type>
                    <size>290463</size>
                    <path>/storage/emulated/0/Weilian2/Cache/Image/mis_20151009153717546.jpg</path>
                    <desc>918,1632</desc>
                    <offline>true</offline>
                    <zipImage>true</zipImage>
                    <key>201510091537196109221882260162.jpg</key>
                    <key2>201510091537194530630630162.jpg</key2>
                    <status>download</status>
                    <downloadURL>http://yi.weilian.cn/plugins/suneeefiletransfer/download/</downloadURL>
                    */
                    
					mes.setTo(targetJIDs.get(0).asText());
					mes.setBody(messageBody);
					chat.sendMessage(mes);
					log.debug("crm message:"+mes.toXML());
				}else{
					resultStr = AssertUtil.error(" Type is a chat, There can be only one JID ! ");
				}
			}else{
				resultStr = AssertUtil.error(" type is null or chat !");
			}
			
			if(StringUtils.isEmpty(resultStr)){
				resultStr=AssertUtil.resultSet(ImConstants.SUCCESS);
			}
		} catch (Exception e) {
			resultStr = AssertUtil.error(e.getMessage());
			e.printStackTrace();
		}
		finally{
		}
		return resultStr;
	}
	
	
	public String getClentNameAdd(String name){
		return "c_"+name.toLowerCase();
	}


	public String getVcode(String JID) {
		String vCode = "";
		List<Map<String, String>> ls = ImStarter.getImList();
		for (int i = 0; i < ls.size(); i++) {
			Map<String, String> map = ls.get(i);
			if (JID.equals(map.get(ImConstants.JID))) {
				vCode = map.get(ImConstants.VCODE);
				break;
			}
		}
		if ("".equals(vCode)) {
			String[] strs = JID.split(ImConstants.AT);
			String username = strs[0];
			Account account = new Account();
			account.setUsername(username);
			account=iAccountService.get(account);
			vCode = account.getvCode();
		}
		return vCode;
	}
	
	
	protected CommService<Account> getCommService() {
		return iAccountService;
	}

}
