package org.jivesoftware.openfire.plugin;

import java.io.File;

import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;

/**
 * rcj
 */
public class HttpFriendPlugin  implements Plugin{

	@Override
	public void initializePlugin(PluginManager manager, File pluginDirectory) {
		// TODO Auto-generated method stub
		// 绕过登录直接访问plugin的servlet  rcj
		//AuthCheckFilter.addExclude("httpfriends");
	}

	@Override
	public void destroyPlugin() {
		// TODO Auto-generated method stub
		//AuthCheckFilter.removeExclude("httpfriends");
	}

}
