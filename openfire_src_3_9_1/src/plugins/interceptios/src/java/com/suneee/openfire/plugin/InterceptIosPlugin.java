package com.suneee.openfire.plugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.session.ClientSession;
import org.jivesoftware.openfire.session.Session;
import org.jivesoftware.openfire.user.User;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.IQ;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;
import org.xmpp.packet.Presence;

public class InterceptIosPlugin implements Plugin,PacketInterceptor{
	
	private XMPPServer server=XMPPServer.getInstance();
	private static PluginManager pluginManager;  
    private InterceptorManager interceptoerManager;  
      
    private static final Logger Log = LoggerFactory.getLogger(InterceptIosPlugin.class); 
      
    public InterceptIosPlugin() {  
        interceptoerManager = InterceptorManager.getInstance();  
    }  
  
    @Override  
    public void initializePlugin(PluginManager manager, File pluginDirectory) {  
        pluginManager = manager;   
        interceptoerManager.addInterceptor(this);  
        Log.info("加载插件成功！");  
    }  
  
    @Override  
    public void destroyPlugin() {  
        interceptoerManager.removeInterceptor(this);  
        Log.info("销毁插件成功！");  
    }  
  
    @Override  
    public void interceptPacket(Packet packet, Session session,  
        boolean incoming, boolean processed) throws PacketRejectedException {  
    	//System.out.println("接收到的消息内容："+packet.toXML());  
        if (packet instanceof Message) {
        	String iosusername=packet.getTo().getNode()+packet.getTo().getDomain();
        	List<ClientSession> results = new ArrayList<ClientSession>();
        	results.addAll(server.getSessionManager().getSessions(packet.getTo().getNode()));
        	Message message =(Message)packet;
        	if(results.size()==0){
        		try {
        			// 程序执行中；是否为结束或返回状态（是否是当前session用户发送消息）  
 	                if (processed || !incoming) {  
 	                    return;  
 	                }  
        			if(!(message.getBody()==null || message.getBody()=="")){
        				User user =server.getUserManager().getUser(packet.getFrom().getNode());
    					String appKey ="c2c95e81e055b68348538fc4";    
    	        	    String masterSecret = "9e6be0cb6f43f35b6e3e6753";
    	                String alert =message.getBody() ;
    	                String msgContent=message.getBody();
    	                if(user!=null && user.getName()!=null){
    	                	msgContent = user.getName()+":"+message.getBody();
    	                }
    	                Integer badge =1;
    	                //Log.info("msgContent："+msgContent);  
    	                JPushAllUtil.sendPush(appKey,masterSecret,alert,badge,msgContent,iosusername);
        			}
				} catch (UserNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        }
        else if (packet instanceof Presence) {
             
        }
        else if (packet instanceof IQ) {
            
        }
        
        
    }  
   /* 
    Collection<User> users =server.getUserManager().getUsers();
	JPushAllUtil jpush = new JPushAllUtil();
	String appKey ="c2c95e81e055b68348538fc4";    
    String masterSecret = "9e6be0cb6f43f35b6e3e6753";
    			String appKey ="47b61b5828dafeac9a785aec";    
                String masterSecret = "7430718e8ef9685f40562f04";
    String alert = "测试ALERT";
    String msgContent = "测试MSG_CONTENT";
    Log.info("------begin------");
	for(User user :users){
		Presence presence = server.getPresenceManager().getPresence(user);
		if (presence!=null && (presence.getShow() == Presence.Show.chat)) { 
			Log.info(user.getUsername()+","+server.getPresenceManager().isAvailable(user));
			
			Log.info(presence);
		}
		//jpush.sendPush(appKey,masterSecret,alert,msgContent);
	}*/
	
	
	public void pgjdbc() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String jdbcDriver = "org.postgresql.Driver";
		String connectionString = "jdbc:postgresql://10.1.29.17:5432/account_auth";
		//jdbc:postgresql://10.1.29.17:5432/account_auth?user=postgres&password=suneee@psql768
		String sql = "SELECT user_id FROM usr";
		try {
			Class.forName(jdbcDriver).newInstance();
			con = DriverManager.getConnection(connectionString, "postgres","suneee@psql768");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Log.info(rs.getString("user_id"));
			}

		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (rs != null) { // 关闭记录集
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pstmt != null) { // 关闭声明
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) { // 关闭连接对象
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	

	
	
	
	public static void main(String[] args) {
		SimpleDateFormat fat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(fat.format(new Date(Long.valueOf("001444892727995"))));
		
	}

	

}
