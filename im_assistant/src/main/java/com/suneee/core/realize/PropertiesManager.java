package com.suneee.core.realize;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.suneee.core.util.LogUtil;
/**
 * @author xiepeng
 *
 */
public class PropertiesManager {

	private static Logger log = LoggerFactory.getLogger(PropertiesManager.class); 
	
	private static Properties replyProperties = null;
	
	public static void setReplyProperties(Properties replyProperties) {
		PropertiesManager.replyProperties = replyProperties;
	}

    /**
     * read replyInfo.properties file
     * @return
     * @throws IOException
     */
	public static Properties getReplyPropetiesInfo() {
		if (replyProperties == null) {
			replyProperties = new Properties();
			InputStream in;
			try {
				in = getInputStreamByFile(ImConstants.REPLY_PROPERTIES);
				replyProperties.load(in);
				log.info("加载" + ImConstants.REPLY_PROPERTIES + "文件成功");
			} catch (FileNotFoundException e) {
				LogUtil.error(e, log);
			} catch (IOException e) {
				LogUtil.error(e, log);
			}
			
		}
		return replyProperties;
	}
	
	/**
	 * 
	 */
	public static InputStream getInputStreamByFile(String filename) throws FileNotFoundException{
		return new BufferedInputStream(new FileInputStream(filename));
		
	}
}
