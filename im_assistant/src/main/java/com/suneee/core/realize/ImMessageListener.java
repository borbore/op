package com.suneee.core.realize;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.PacketExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.suneee.base.util.StringUtil;
import com.suneee.core.userdefine.RemoteService;
import com.suneee.core.util.AssertUtil;
import com.suneee.core.util.ImGlobals;
import com.suneee.core.util.LogUtil;
public class ImMessageListener implements MessageListener {
	
	private static Map<String, Chat> chatMap = new ConcurrentHashMap<String ,Chat>();


	private static Logger log = LoggerFactory.getLogger(ImMessageListener.class);
	

	public void processMessage(Chat chat, Message message) {
		chatMap.put(AssertUtil.getShortName(message.getFrom()), chat);
		//Iterable<PacketExtension> itPack= message.getExtensions();
		PacketExtension pack = message.getExtension("x", "urn:xmpp:suneee:cs:presence:state");
		PacketExtension filepack = message.getExtension("file", "http://wwww.suneee.com/file-transfer");
		Document doc = null;
		// 命令通道
		RemoteService remoteService;
		String text="";
		System.out.println("client message:"+message.toXML());
		try {
	/*		for(PacketExtension p : itPack){
				System.out.println("p_xml:"+p.toXML());
			}*/
			String form = message.getFrom();
			String formlast = form.substring(form.indexOf("/")+1,form.length());
			
			System.out.println("formlast:"+formlast);
			ObjectMapper mapper = new ObjectMapper();
			if(filepack!=null){
				System.out.println("filepack_xml:"+filepack.toXML());
				doc=DocumentHelper.parseText(filepack.toXML()); 
				// 获取根节点
		        Element rootElt = doc.getRootElement(); 
		        ObjectNode onode = mapper.createObjectNode();  
				onode.put("type", rootElt.elementTextTrim("type"));  
				onode.put("filename", rootElt.elementTextTrim("name"));  
				onode.put("url", rootElt.elementTextTrim("downloadURL")+rootElt.elementTextTrim("key"));
				text=onode.toString();
			}
			if(pack!=null){
				System.out.println("pack_xml:"+pack.toXML());
				doc=DocumentHelper.parseText(pack.toXML()); 
				// 获取根节点
		        Element rootElt = doc.getRootElement(); 
		        text = rootElt.elementTextTrim("text");
			}
			
			if(StringUtil.isBlank(text) && StringUtil.isBlank(message.getBody())){
				return;
			}
			remoteService = (RemoteService) Class.forName(ImGlobals.getRemoteClassName_c()).newInstance();
			remoteService.getReplyInfo(ImGlobals.getRemoteServiceParamsMap_c(), message.getFrom(), message.getTo(),message.getBody(),text);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	// 多个地方需要调用chat.sendMessage,因此对其进行封装
	public static void sendMessage(Chat chat, String message) {
		try {
			chat.sendMessage(message);
		} catch (XMPPException e) {
			LogUtil.error(e, log);
		}
	}
	
	public static Map<String, Chat> getChatMap() {
		return chatMap;
	}

	public static void setChatMap(Map<String, Chat> chatMap) {
		ImMessageListener.chatMap = chatMap;
	}
}
