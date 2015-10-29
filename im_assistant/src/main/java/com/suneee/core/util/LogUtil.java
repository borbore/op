package com.suneee.core.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;


public class LogUtil {

	/**
	 * e.printStackTrace
	 * @param e
	 * @return
	 */
	public static void error(Exception e, Logger log){
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw, true));
		log.error(sw.toString());
//		return sw.toString();
	}
	
	/**
	 * 
	 * @param errorPrefix
	 * @param e
	 * @param log
	 */
	public static void error(String  errorPrefix ,Exception e, Logger log){
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw, true));
		log.error(errorPrefix + sw.toString());
	}
}
