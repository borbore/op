package com.suneee.openfire.plugin;

import java.io.File;
import java.util.Collection;
import java.util.Map;

import org.dom4j.Element;
import org.jivesoftware.openfire.PresenceManager;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.muc.MUCRoom;
import org.jivesoftware.openfire.muc.MultiUserChatManager;
import org.jivesoftware.openfire.session.Session;
import org.jivesoftware.openfire.user.User;
import org.jivesoftware.openfire.user.UserManager;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.jivesoftware.util.JiveGlobals;
import org.jivesoftware.util.PropertyEventDispatcher;
import org.jivesoftware.util.PropertyEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.component.Component;
import org.xmpp.component.ComponentException;
import org.xmpp.component.ComponentManager;
import org.xmpp.component.ComponentManagerFactory;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;
import org.xmpp.packet.PacketExtension;

/**
 * 多人聊天管理插件：成员、权限等的管理。
 * 
 * <pre>
 * 
 * Create By：hexuan@suneee.com			2014-10-30 上午9:28:45
 * Description：
 * 
 *     详细说明。
 * =========================================================
 * 
 * Modify By：XXX						2014-10-30 上午9:28:45
 * Description：
 * 
 *     详细说明。
 * =========================================================
 * 
 * </pre>
 * 
 * 版权所有：深圳象翌微链电子科技有限公司
 * 
 * @version 1.0
 */
public class SuneeeMucMgrPlugin implements Plugin, Component,
		PropertyEventListener {

	private static final Logger logger = LoggerFactory
			.getLogger(SuneeeMucMgrPlugin.class);

	private ComponentManager componentManager;
	private PluginManager pluginManager;
	private MultiUserChatManager mucManager;
	private UserManager userManager;
	private PresenceManager presenceManager;
	private IMFillNamePacketInterceptor interceptor = new IMFillNamePacketInterceptor();

	private String serviceName;

	private static final String MUC_MGR_ELEMENTNAME = "mucmgr";
	private static final String MUC_MGR_NAMESPACE = "http://wwww.suneee.com/muc-manager";

	private static final String USER_INFO_ELEMENTNAME = "userinfo";
	private static final String USER_INFO_NAMESPACE = "http://wwww.suneee.com/user-info";

	public void initializePlugin(PluginManager manager, File pluginDirectory) {
		pluginManager = manager;
		XMPPServer server = XMPPServer.getInstance();
		userManager = UserManager.getInstance();
		componentManager = ComponentManagerFactory.getComponentManager();
		mucManager = server.getMultiUserChatManager();
		presenceManager = server.getPresenceManager();
		serviceName = JiveGlobals.getProperty(
				"com.suneee.muc.manager.serviceName", "suneeeMucMgr");
		// Listen to property events
		PropertyEventDispatcher.addListener(this);
		try {
			componentManager.addComponent(serviceName, this);
		} catch (ComponentException e) {
			logger.error(e.getMessage(), e);
		}

		InterceptorManager.getInstance().addInterceptor(interceptor);
	}

	@Override
	public void destroyPlugin() {
		InterceptorManager.getInstance().removeInterceptor(interceptor);
		// Stop listening to property events
		PropertyEventDispatcher.removeListener(this);
		try {
			componentManager.removeComponent(serviceName);
		} catch (ComponentException e) {
			logger.error(e.getMessage(), e);
		}
		pluginManager = null;
		componentManager = null;
		mucManager = null;
		userManager = null;
		presenceManager = null;
	}

	@Override
	public String getDescription() {
		return pluginManager.getDescription(this);
	}

	@Override
	public String getName() {
		return pluginManager.getName(this);
	}

	@Override
	public void initialize(JID arg0, ComponentManager arg1)
			throws ComponentException {

	}

	private class IMFillNamePacketInterceptor implements PacketInterceptor {
		public void interceptPacket(Packet packet, Session session,
				boolean incoming, boolean processed)
				throws PacketRejectedException {
			// 读取未处理的消息包
			if (!processed && incoming && packet instanceof Message) {
				Message msg = (Message) packet;
				User user = null;
				try {
					user=userManager.getUserProvider().loadUser(msg.getFrom().toString());
				} catch (UserNotFoundException e) {
				}
				String name = msg.getFrom().getNode();
				String nickName = name;
				if (user != null) {
					nickName = user.getName();
				}
				// 给消息包填充昵称扩展
				PacketExtension pakExt = msg.getExtension(
						USER_INFO_ELEMENTNAME, USER_INFO_NAMESPACE);
				if (pakExt != null) {
					msg.deleteExtension(USER_INFO_ELEMENTNAME,
							USER_INFO_NAMESPACE);
				}
				pakExt = new PacketExtension(USER_INFO_ELEMENTNAME,
						USER_INFO_NAMESPACE);
				msg.addExtension(pakExt);
				Element userInfo = pakExt.getElement();
				Element nameEle = userInfo.addElement("name");
				nameEle.setText(name != null ? name : msg.getFrom().toBareJID());
				Element nickNameEle = userInfo.addElement("nickName");
				nickNameEle.setText(nickName != null ? nickName : msg.getFrom()
						.toBareJID());
			}
		}
	}

	@Override
	public void processPacket(Packet packet) {
		if (packet instanceof Message) {
			Message msg = (Message) packet;
			PacketExtension pakExt = msg.getExtension(MUC_MGR_ELEMENTNAME,
					MUC_MGR_NAMESPACE);
			if (pakExt != null) {
				String from = msg.getFrom().toString();
				String to = msg.getTo().toString();
				msg.setFrom(to);
				msg.setTo(from);
				final Element mucChatMgr = pakExt.getElement();
				String command = mucChatMgr.elementText("command");
				String userJID = mucChatMgr.elementText("userJID");
				userJID = (userJID == null || userJID.length() == 0) ? from
						: userJID;
				JID uJID = new JID(userJID);
				JID fJID = new JID(from);

				Element status = mucChatMgr.element("status");
				if (status == null) {
					status = mucChatMgr.addElement("status");
				}
				if ("getPresence".equals(command)) { // 获取用户在线状态：非好友时同样可用
					User user = null;
					try {
						user=userManager.getUserProvider().loadUser(userJID);
					} catch (UserNotFoundException e) {
					}
					if (user != null) {
						mucChatMgr.element("status").setText("success");
						mucChatMgr.addElement("presence").setText(
								presenceManager.isAvailable(user) ? "available"
										: "unavailable");
					} else {
						mucChatMgr.element("status").setText("error");
						mucChatMgr.addElement("errorInfo").setText("无效用户。");
					}
				} else if ("removeRoomMember".equals(command)) { // 删除房间成员
					String roomId = mucChatMgr.elementText("roomJID");
					JID roomJID = new JID(roomId);
					String roomName = roomJID.getNode();
					MUCRoom room = mucManager.getMultiUserChatService(roomJID)
							.getChatRoom(roomName);

					// 不是对自己的账号操作，则需要检查权限
					Collection<JID> oc = room.getOwners();
					Collection<JID> ac = room.getAdmins();

					if (!uJID.asBareJID().equals(fJID.asBareJID()) // 不是对自己的操作
							&& (!ac.contains(fJID.asBareJID()) // 不是管理员，且不是创建者
									&& !oc.contains(fJID.asBareJID()) || ac
									.contains(uJID.asBareJID()) // 不是创建者对管理员的操作
									&& !oc.contains(fJID.asBareJID()))) {
						status.setText("error");
						mucChatMgr.addElement("errorInfo").setText("权限不足。");
						msg.setFrom(to);
						msg.setTo(from);
					} else if (oc.contains(uJID.asBareJID())) { // 创建者自己退出房间，则作为删除房间处理
						room.destroyRoom(null, "创建者(" + fJID.asBareJID()
								+ ")解散了房间(" + roomName + ")。");
						status.setText("success");
					} else {
						IQ iq = new IQ(IQ.Type.set);
						Element frag = iq.setChildElement("query",
								"http://jabber.org/protocol/muc#admin");
						Element item = frag.addElement("item");
						item.addAttribute("affiliation", "none");
						item.addAttribute("jid", userJID);
						try {
							room.getIQOwnerHandler().handleIQ(iq,
									room.getRole());
							status.setText("success");
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
							status.setText("error");
							mucChatMgr.addElement("errorInfo").setText(
									e.getMessage());
						}
					}
				}
				try {
					componentManager.sendPacket(this, msg);
				} catch (ComponentException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}

	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void propertySet(String property, Map<String, Object> params) {
		if (property.equals("com.suneee.muc.manager.serviceName")) {
			this.serviceName = (String) params.get("value");
		}
	}

	@Override
	public void propertyDeleted(String property, Map<String, Object> params) {
		if (property.equals("com.suneee.muc.manager.serviceName")) {
			this.serviceName = "suneeeMucMgr";
		}

	}

	@Override
	public void xmlPropertySet(String property, Map<String, Object> params) {
		// TODO Auto-generated method stub

	}

	@Override
	public void xmlPropertyDeleted(String property, Map<String, Object> params) {
		// TODO Auto-generated method stub

	}

}
