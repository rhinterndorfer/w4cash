package com.openbravo.pos.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.Session;

public class Log {
	private static Logger logger = Logger.getLogger("at.w4cash");
	
	private static Session m_session;
	private static String m_host;
	
	public static void Init(Session s, String host)
	{
		m_session = s;
		m_host = host;
	}
	
	public static void Exception(Exception e)
	{
		logger.severe(e.getMessage());
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		logger.severe(sw.toString());
	}
	
	public static void Exception(String msg, Exception e)
	{
		logger.severe(msg);
		Exception(e);
	}
	
	public static void Exception(String msg)
	{
		logger.severe(msg);
	}
	
	public static void Info(String msg)
	{
		logger.info(msg);
	}
	
	
	public static void Info2DB(String msg)
	{
		Log2DB("INFO", msg);
	}
	
	private static void Log2DB(String level, String msg)
	{
		if(m_session == null || m_host == null)
		{
			Exception("DB Logging not initialised");
		}
		
		if(msg != null && msg.length() > 1000)
			msg = msg.substring(0, 999);
		
		if(m_host != null && m_host.length() > 200)
			m_host = m_host.substring(0, 199);
		
		try {
			new PreparedSentence(m_session,
					"INSERT INTO LOGS (LOGTIME, LOGHOST, LOGLEVEL, LOGMESSAGE) "
					+ "VALUES (SYSDATE, ?, ?, ?)",
					new SerializerWriteBasic(new Datas[] { Datas.STRING, Datas.STRING, Datas.STRING }))
			.exec(new Object[] { 
					m_host, 
					level, 
					msg 
					});
		} catch (BasicException e) {
			Exception(e);
		}

	}
}
