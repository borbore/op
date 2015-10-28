package com.suneee.openfire.plugin;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Element;
import org.jivesoftware.openfire.PacketRouter;
import org.jivesoftware.openfire.PresenceManager;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.session.Session;
import org.jivesoftware.openfire.user.User;
import org.jivesoftware.openfire.user.UserManager;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.jivesoftware.util.JiveGlobals;
import org.jivesoftware.util.PropertyEventDispatcher;
import org.jivesoftware.util.PropertyEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;
import org.xmpp.packet.PacketExtension;

/**
 * 多人聊天管理插件：成员权限管理等。
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
public class SuneeeFileTransferPlugin implements Plugin, PropertyEventListener {

	private static final Logger logger = LoggerFactory
			.getLogger(SuneeeFileTransferPlugin.class);

	private UserManager userManager;
	private PresenceManager presenceManager;
	private PacketRouter packetRouter;
	private FileTranserPacketInterceptor interceptor = new FileTranserPacketInterceptor();
	private String uploadURL;
	private String downloadURL;
	private Map<String, String> keys = new HashMap<String, String>();

	private String serverDomain;

	private static final String ELEMENTNAME = "file";
	private static final String NAMESPACE = "http://wwww.suneee.com/file-transfer";

	public void initializePlugin(PluginManager manager, File pluginDirectory) {

		XMPPServer server = XMPPServer.getInstance();
		serverDomain = server.getServerInfo().getXMPPDomain();
		userManager = UserManager.getInstance();
		presenceManager = server.getPresenceManager();
		packetRouter = server.getPacketRouter();
		uploadURL = JiveGlobals.getProperty(
				"com.suneee.filetransfer.uploadURL", "http://" + serverDomain
						+ ":9090/plugins/suneeefiletransfer/upload/");
		downloadURL = JiveGlobals.getProperty(
				"com.suneee.filetransfer.downloadURL", "http://" + serverDomain
						+ ":9090/plugins/suneeefiletransfer/download/");
		InterceptorManager.getInstance().addInterceptor(interceptor);
		// Listen to property events
		PropertyEventDispatcher.addListener(this);
	}

	@Override
	public void destroyPlugin() {
		InterceptorManager.getInstance().removeInterceptor(interceptor);
		// Stop listening to property events
		PropertyEventDispatcher.removeListener(this);
		userManager = null;
		presenceManager = null;
		packetRouter = null;

	}

	public Map<String, String> getKeys() {
		return keys;
	}

	private class FileTranserPacketInterceptor implements PacketInterceptor {
		public void interceptPacket(Packet packet, Session session,
				boolean incoming, boolean processed)
				throws PacketRejectedException {
			// 读取未处理的消息包
			if (!processed && incoming && packet instanceof Message) {
				Message msg = (Message) packet;
				PacketExtension pakExt = msg.getExtension(ELEMENTNAME,
						NAMESPACE);
				if (pakExt != null) {
					String from = msg.getFrom().toString();
					String to = msg.getTo().toString();
					final Element fileInfo = pakExt.getElement();
					String offline = fileInfo.elementText("offline");
					String zipImage = fileInfo.elementText("zipImage");
					String status = fileInfo.elementText("status");
					fileInfo.remove(fileInfo.element("uploadURL"));
					fileInfo.remove(fileInfo.element("downloadURL"));
					if (!"error".equals(status)) {
						fileInfo.remove(fileInfo.element("errorInfo"));
					}
					if ("ask".equals(status)) { // 询问
						if (Message.Type.groupchat.equals(msg.getType())) { // 群聊发送文件
							fileInfo.element("status").setText("accept");
							msg.setFrom(to);
							msg.setTo(from);
							packetRouter.route(msg);
							throw new PacketRejectedException();
						} else { // 点对点发送文件
							User user = null;
							try {
								user = userManager.getUser(to);
							} catch (UserNotFoundException e) {
							}
							if (user == null) { // 不存在的接受者
								fileInfo.element("status").setText("error");
								fileInfo.addElement("errorInfo").setText(
										"无效用户。");
								msg.setFrom(to);
								msg.setTo(from);
								packetRouter.route(msg);
								throw new PacketRejectedException();
							} else {
								if (!"true".equals(offline)) { // 发送者没有指定进行离线传输
									if (!presenceManager.isAvailable(user)) { // 接受者不在线
										fileInfo.element("status").setText(
												"offline");
										msg.setFrom(to);
										msg.setTo(from);
										packetRouter.route(msg);
										throw new PacketRejectedException();
									}
								} else { // 进行离线传输
									fileInfo.element("status")
											.setText("accept");
									msg.setFrom(to);
									msg.setTo(from);
									packetRouter.route(msg);
									throw new PacketRejectedException();
								}
							}
						}
					} else if ("accept".equals(status)
							|| "offline".equals(status)) { // 接受
						fileInfo.addElement("uploadURL").setText(uploadURL);
						fileInfo.addElement("downloadURL").setText(downloadURL);
						String name = fileInfo.elementText("name");
						String ext = "";
						if (name != null && name.length() > 0) {
							int idx = name.lastIndexOf(".");
							if (idx != -1) {
								ext = name.substring(idx);
							}
						}

						Element key = fileInfo.element("key");
						if (key == null) {
							key = fileInfo.addElement("key");
							key.setText(randomID() + ext);
						}
						if ("true".equals(zipImage)) { // 生成缩略图
							Element key2 = fileInfo.element("key2");
							if (key2 == null) {
								key2 = fileInfo.addElement("key2");
								key2.setText(randomID() + ext);
							}
						}
					} else if ("reject".equals(status)) { // 拒绝

					} else if ("download".equals(status)) { // 开始下载
						fileInfo.addElement("downloadURL").setText(downloadURL);
					} else if ("complete".equals(status)) { // 下载完成
						if (Message.Type.groupchat.equals(msg.getType())) { // 群聊发送文件
							// 丢弃，不再通知发送者
							throw new PacketRejectedException();
						}
					}
				}
			}
		}
	}

	/**
	 * base64解码
	 * 
	 * @param str
	 * @return String
	 */
	/*
	 * private String decodeBase64(String str, String charset) {
	 * 
	 * byte[] bt = null; try { sun.misc.BASE64Decoder decoder = new
	 * sun.misc.BASE64Decoder(); bt = decoder.decodeBuffer(str); if (charset !=
	 * null) { return new String(bt, charset); } else { return new String(bt); }
	 * } catch (IOException e) { return null; } }
	 */

	/**
	 * base64编码
	 * 
	 * @param bstr
	 * @return String
	 */
	/*
	 * private static String encodeBase64(String bstr, String charset) {
	 * 
	 * if (charset != null) { try { return new
	 * sun.misc.BASE64Encoder().encode(bstr.getBytes(charset)); } catch
	 * (UnsupportedEncodingException e) { return null; } } else { return new
	 * sun.misc.BASE64Encoder().encode(bstr.getBytes()); } }
	 */

	/**
	 * 生成一个随机id。
	 * 
	 * <pre>
	 * 
	 * Description：
	 * 
	 *     详细说明。
	 * 
	 * </pre>
	 * 
	 * @return 随机id
	 */
	private String randomID() {

		Long now = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date()));
		Long random = (long) (Math.random() * now);
		return new StringBuffer(String.valueOf(now)).append(random)
				.append(Thread.currentThread().getId()).toString();
	}

	@Override
	public void propertySet(String property, Map<String, Object> params) {
		if (property.equals("com.suneee.filetransfer.uploadURL")) {
			this.uploadURL = (String) params.get("value");
		} else if (property.equals("com.suneee.filetransfer.downloadURL")) {
			this.downloadURL = (String) params.get("value");
		}
	}

	@Override
	public void propertyDeleted(String property, Map<String, Object> params) {
		if (property.equals("com.suneee.filetransfer.uploadURL")) {
			this.uploadURL = "http://" + serverDomain
					+ ":9090/plugins/suneeefiletransfer/upload/";
		} else if (property.equals("com.suneee.filetransfer.downloadURL")) {
			this.downloadURL = "http://" + serverDomain
					+ ":9090/plugins/suneeefiletransfer/download/";
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
