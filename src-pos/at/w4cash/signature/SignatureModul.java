package at.w4cash.signature;


import java.awt.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


import org.apache.axis.types.PositiveInteger;
import org.apache.commons.codec.binary.Base64;

import com.openbravo.basic.BasicException;
import com.openbravo.basic.SignatureUnitException;
import com.openbravo.data.gui.JConfirmDialog;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SerializerReadBytes;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.data.loader.SerializerReadInteger;
import com.openbravo.data.loader.SerializerReadString;
import com.openbravo.data.loader.SerializerReadTimestamp;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.Session;
import com.openbravo.data.loader.SessionDBOracle;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.JPrincipalApp;
import com.openbravo.pos.payment.PaymentInfo;
import com.openbravo.pos.payment.PaymentInfoCash;
import com.openbravo.pos.sales.JPanelTicket;
import com.openbravo.pos.sales.TaxesLogic;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.util.Log;

import at.gv.bmf.finanzonline.fon.ws.session.LoginRequest;
import at.gv.bmf.finanzonline.fon.ws.session.LoginResponse;
import at.gv.bmf.finanzonline.fon.ws.session.SessionService;
import at.gv.bmf.finanzonline.fon.ws.session.SessionServiceLocator;
import at.gv.bmf.finanzonline.fon.ws.session.SessionServicePortProxy;
import at.gv.bmf.finanzonline.rkdb.Art_se;
import at.gv.bmf.finanzonline.rkdb.Art_uebermittlung;
import at.gv.bmf.finanzonline.rkdb.Ausfall;
import at.gv.bmf.finanzonline.rkdb.Ausfall_se;
import at.gv.bmf.finanzonline.rkdb.Belegpruefung;
import at.gv.bmf.finanzonline.rkdb.Registrierung_kasse;
import at.gv.bmf.finanzonline.rkdb.Registrierung_se;
import at.gv.bmf.finanzonline.rkdb.Result;
import at.gv.bmf.finanzonline.rkdb.Rkdb;
import at.gv.bmf.finanzonline.rkdb.RkdbMessage;
import at.gv.bmf.finanzonline.rkdb.RkdbRequest;
import at.gv.bmf.finanzonline.rkdb.RkdbResponse;
import at.gv.bmf.finanzonline.rkdb.RkdbServicePortProxy;
import at.gv.bmf.finanzonline.rkdb.Status_ggs;
import at.gv.bmf.finanzonline.rkdb.Status_kasse;
import at.gv.bmf.finanzonline.rkdb.Status_see;
import at.gv.bmf.finanzonline.rkdb.VerificationResult;
import at.gv.bmf.finanzonline.rkdb.Wiederinbetriebnahme_se;
import at.gv.bmf.finanzonline.rkdb.Zertifikatsseriennummer;

public class SignatureModul {
	private static SignatureModul m_instance;
	private Session m_session;
	private String m_posid;
	private String m_zdaId;
	private String m_dbzdaId;
	private SecretKey m_aeskey;
	private X509Certificate m_sigCert;
	private String m_sigserialnumber;
	private String m_dbsigserialnumber;
	private String m_dbactivesignatureid;
	private X509Certificate m_issuerCert;
	private Boolean m_isOutOfOrder = false;
	private Boolean m_isActive = false;
	private Boolean m_isCriticalError;
	private Boolean m_isOutOfOrderInPast = false;
	private Boolean m_isOldSignatureUnit = false;
	private Boolean m_isFirstTicket = false;
	private AppView m_app;
	private Cipher m_cipher;
	private TaxesLogic m_taxeslogic;
	private DataLogicSales m_dlSales;
	private JPanelTicket m_panelticket;
	
	private String m_wssessionid;
	private String m_wstid;
	private String m_wsbenid;
	private String m_wspin;
	
	
	private HashMap<String, String> m_zdamap = new HashMap<String, String>();
	private HashMap<String, String> m_signatureserialhexmap = new HashMap<String, String>();
	private HashMap<String, String> m_signaturedermap = new HashMap<String, String>();
	private HashMap<String, String> m_signatureproviderdermap = new HashMap<String, String>();
	
	public String GetZDAId()
	{
		return m_zdaId;
	}
	
	public String GetZDAId(String signatureid) throws BasicException
	{
		if(m_zdamap.containsKey(signatureid))
			return m_zdamap.get(signatureid);
		
		Object ozdaid = new StaticSentence(m_session, "SELECT CERTPROVIDERID FROM SIGNATURES "
				+ "WHERE ID = ?",
				new SerializerWriteBasic(new Datas[] { Datas.STRING }),
				SerializerReadString.INSTANCE).find(new Object[] { signatureid });
		
		m_zdamap.put(signatureid, ozdaid.toString());
		return ozdaid.toString();
	}
	
	public String GetSignatureId()
	{
		return m_dbactivesignatureid;
	}
	
	public String GetSignatureSerialNumber()
	{
		return m_sigserialnumber;
	}
	
	private String ToHex(String serialNumber)
	{
		BigInteger i = new BigInteger(serialNumber);
		String hex = String.format("%01X", i);
		return hex;
	}
	
	public String GetSignatureSerialNumberHex()
	{
		return ToHex(m_sigserialnumber);
	}
	
	public String GetSignatureSerialNumberHex(String signatureid) throws BasicException
	{
		if(m_signatureserialhexmap.containsKey(signatureid))
			return m_signatureserialhexmap.get(signatureid);
		
		Object ocertserial = new StaticSentence(m_session, "SELECT CERTSERIAL FROM SIGNATURES "
				+ "WHERE ID = ?",
				new SerializerWriteBasic(new Datas[] { Datas.STRING }),
				SerializerReadBytes.INSTANCE).find(new Object[] { signatureid });
		
		String sigserialnumberhex = ToHex(new String((byte[])ocertserial, StandardCharsets.UTF_8));
		m_signatureserialhexmap.put(signatureid, sigserialnumberhex);
		return sigserialnumberhex;
	}
	
	public String GetSignatureDERBase64(String signatureid) throws BasicException
	{
		if(m_signaturedermap.containsKey(signatureid))
			return m_signaturedermap.get(signatureid);
		
		Object ocertder = new StaticSentence(m_session, "SELECT CERTDERBASE64 FROM SIGNATURES "
				+ "WHERE ID = ?",
				new SerializerWriteBasic(new Datas[] { Datas.STRING }),
				SerializerReadBytes.INSTANCE).find(new Object[] { signatureid });
		
		String sigderbase64 = new String((byte[])ocertder, StandardCharsets.UTF_8);
		m_signaturedermap.put(signatureid, sigderbase64);
		return sigderbase64;
	}
	
	public String GetSignatureProviderDERBase64(String signatureid) throws BasicException
	{
		if(m_signatureproviderdermap.containsKey(signatureid))
			return m_signatureproviderdermap.get(signatureid);
		
		Object ocertproviderder = new StaticSentence(m_session, "SELECT CERTDERBASE64 FROM CERTIFICATECHAIN "
				+ "WHERE CHAINNO=1 AND SIGNATUREID = ?",
				new SerializerWriteBasic(new Datas[] { Datas.STRING }),
				SerializerReadBytes.INSTANCE).find(new Object[] { signatureid });
		
		String sigproviderderbase64 = new String((byte[])ocertproviderder, StandardCharsets.UTF_8);
		m_signatureproviderdermap.put(signatureid, sigproviderderbase64);
		return sigproviderderbase64;
	}
	
	public Boolean GetIsOutOfOrder()
	{
		return m_isOutOfOrder || m_isOutOfOrderInPast;
	}
	
	public Boolean GetIsActive()
	{
		return m_isActive;
	}
	
	public Boolean GetIsCriticalError()
	{
		return m_isCriticalError;
	}
	
	public Boolean GetIsFirstTicket()
	{
		return m_isFirstTicket;
	}
	
	public Boolean GetIsDevelopment()
	{
		return  "wSg9954936361".equals(GetPOSID()) 
				||  "1636394599".equals(GetSignatureSerialNumber());
	}
	
	public static SignatureModul getInstance()
	{
		return m_instance;
	}
	
	public static void InitInstance(AppView app)
	{
		m_instance = new SignatureModul();
		m_instance.Init(app);
	}
	
	public void InitDataLogic(JPanelTicket panelTicket, DataLogicSales dlSales, TaxesLogic taxesLogic)
	{
		m_taxeslogic = taxesLogic;
		m_dlSales = dlSales;
		m_panelticket = panelTicket;
	}
	
	public void Init(AppView app)
	{
		m_isOutOfOrder = false;
		m_isCriticalError = false;
		m_isOutOfOrderInPast = false;
		m_isFirstTicket = false;
		
		m_app = app;
		Session s = m_app.getSession(); 
		SetSession(s);
		SetSignatureUnitWSAddress();
		
		InitSignatureWS();
		InitPOSID();
		InitAES();
	}
	
	private void InitSignatureWS()
	{
		// check if signature unit exists
		X509Certificate sigCert = JWSModul.GetSigCert();
		if(sigCert != null)
		{
			m_sigCert = sigCert;
			m_issuerCert = JWSModul.GetIssuerCert();
			m_sigserialnumber = JWSModul.GetSigCertSerialNumber();
			m_zdaId = JWSModul.GetZDAId();
		}
	}
	
	private void InitPOSID()
	{
		// check POSID and store if not exists
		Object posid = null;
		
		try {
			posid = new StaticSentence(m_session, "SELECT POSID FROM APPLICATIONS WHERE ID = 'w4cashdb' AND DBSERVERNAME = SYS_CONTEXT('USERENV', 'SERVER_HOST')", null,
					SerializerReadString.INSTANCE).find();
		} catch (BasicException e) {
			m_isCriticalError = true;
			Log.Exception(e);
		}
		if(posid != null)
		{
			m_posid = posid.toString();
		}
	}
	
	private void InitAES()
	{
		Object aesbase64 = null;
		
		try {
			aesbase64 = new StaticSentence(m_session, "SELECT AESBASE64 FROM APPLICATIONS WHERE ID = 'w4cashdb' AND DBSERVERNAME = SYS_CONTEXT('USERENV', 'SERVER_HOST')", null,
					SerializerReadString.INSTANCE).find();
		} catch (BasicException e) {
			m_isCriticalError = true;
			Log.Exception(e);
		}
		
		// check AES and generate if not exists		
		if(aesbase64 != null)
		{
			//base64 encoding of AES key (DO NOT USE BASE64-URL encoding)
			byte[] base64decode = base64Decode(aesbase64.toString(), false);
			m_aeskey = new SecretKeySpec(base64decode, "AES");
		}
		else if(!m_isCriticalError)
		{
			// create first AES key
			SecretKey aesKey = createAESKey();
			// store AES key
			try {
				//base64 encoding of AES key (DO NOT USE BASE64-URL encoding)
				String aesKeyBase64 = base64Encode(aesKey.getEncoded(), false);
				new PreparedSentence(m_session,
						"UPDATE APPLICATIONS SET AESBASE64 = ? "
						+ "WHERE ID = 'w4cashdb' AND DBSERVERNAME = SYS_CONTEXT('USERENV', 'SERVER_HOST')",
						new SerializerWriteBasic(new Datas[] { Datas.STRING })).exec(new Object[] { aesKeyBase64 });
				
				m_aeskey = aesKey;
			} catch (BasicException e) {
				m_isCriticalError = true;
				Log.Exception(e);
			}
		}
	}
	
	public void CheckSignatureUnitState(Component caller, Boolean isStartup)
	{
		// check if signature unit exists
		m_sigserialnumber = JWSModul.GetSigCertSerialNumber();
		m_zdaId = JWSModul.GetZDAId();
		
		// check active signature certificates and store if not exists
		int dbsigCount = 0;
		try {
			Object oDbSigCount = new StaticSentence(m_session, "SELECT COUNT(*) FROM SIGNATURES "
					+ "WHERE DBSERVERNAME = SYS_CONTEXT('USERENV', 'SERVER_HOST') ", null,
					SerializerReadInteger.INSTANCE).find();
			
			m_isOldSignatureUnit = false;
			if(m_sigserialnumber != null && m_zdaId != null)
			{
				Object oDbSigCountOld = new StaticSentence(m_session, "SELECT COUNT(*) FROM SIGNATURES "
					+ "WHERE DBSERVERNAME = SYS_CONTEXT('USERENV', 'SERVER_HOST') "
					+ "AND CERTPROVIDERID = ? "
					+ "AND utl_raw.cast_to_varchar2(CERTSERIAL) = ? "
					+ "AND ACTIVE = 0 ", 
					new SerializerWriteBasic(new Datas[] { Datas.STRING, Datas.STRING }),
					SerializerReadInteger.INSTANCE).find(new Object[] { m_zdaId, m_sigserialnumber });
				m_isOldSignatureUnit = ((int)oDbSigCountOld) > 0;
			}
			
			dbsigCount = (int)oDbSigCount;
			if(dbsigCount > 0)
			{
				Object odbactivesignatureid = new StaticSentence(m_session, "SELECT ID FROM SIGNATURES "
						+ "WHERE DBSERVERNAME = SYS_CONTEXT('USERENV', 'SERVER_HOST') AND ACTIVE > 0", null,
						SerializerReadString.INSTANCE).find();
				
				Object odbcertserial = new StaticSentence(m_session, "SELECT CERTSERIAL FROM SIGNATURES "
						+ "WHERE DBSERVERNAME = SYS_CONTEXT('USERENV', 'SERVER_HOST') AND ACTIVE > 0", null,
						SerializerReadBytes.INSTANCE).find();
				
				Object odbzdaid = new StaticSentence(m_session, "SELECT CERTPROVIDERID FROM SIGNATURES "
						+ "WHERE DBSERVERNAME = SYS_CONTEXT('USERENV', 'SERVER_HOST') AND ACTIVE > 0", null,
						SerializerReadString.INSTANCE).find();
				
				if(odbactivesignatureid != null)
				{
					m_dbactivesignatureid = odbactivesignatureid.toString();
				}
				
				if(odbcertserial != null)
				{
					m_dbsigserialnumber = new String((byte[])odbcertserial, StandardCharsets.UTF_8);
					if(odbactivesignatureid != null)
					{
						m_signatureserialhexmap.put(m_dbactivesignatureid, ToHex(m_dbsigserialnumber));
					}
				}
				
				if(odbzdaid != null)
				{
					m_dbzdaId = odbzdaid.toString();
					if(odbactivesignatureid != null)
					{
						m_zdamap.put(m_dbactivesignatureid, m_dbzdaId);
					}
				}
			}
			else
			{
				m_dbsigserialnumber = null;
				m_dbzdaId = null;
			}
			
			Object oDbSignatureOutOfOrderLastCashTicket = new StaticSentence(m_session, 
					"SELECT NVL(SIGNATUREOUTOFORDER,1) as SIGNATUREOUTOFORDER FROM TICKETS "
					+ "WHERE CASHTICKETID = (SELECT NVL(MAX(CASHTICKETID),-1) FROM TICKETS WHERE CASHTICKETID IS NOT NULL) ",
					null,
					SerializerReadInteger.INSTANCE).find();
			m_isOutOfOrderInPast = oDbSignatureOutOfOrderLastCashTicket != null && ((int)oDbSignatureOutOfOrderLastCashTicket == 1);
			
			
			m_isFirstTicket = oDbSignatureOutOfOrderLastCashTicket == null;
		} catch (BasicException e) {
			m_isCriticalError = true;
			Log.Exception(e);
		}

		if(m_isCriticalError)
		{
			// critical error
			// retry to resolve error 
			// or cancel and stay in critical error state
			int res = JConfirmDialog.showConfirm(m_app, caller,
					null,
					AppLocal.getIntString("signature.criticalfailureretry"));
			if (res == JOptionPane.YES_OPTION) {
				m_isCriticalError = false;
				InitPOSID();
				InitAES();
				CheckSignatureUnitState(caller, isStartup);
				return;
			}
			else
				return;
		}
		
		
		if(m_isOldSignatureUnit)
		{
			int res = JConfirmDialog.showConfirm(m_app, caller, 
					null,
					AppLocal.getIntString("signature.signatureunitold.failure"));
		
			if (res == JOptionPane.YES_OPTION) {
				m_isActive = true;
				m_isOutOfOrder = true;
				Log.Info2DB("Signature: use out of order");
			}
			else
			{
				m_isCriticalError = true;
				m_isActive = true;
				m_isOutOfOrder = true;
			}
		}
		
		else if((m_sigserialnumber == null || m_zdaId == null) 
				&& m_dbsigserialnumber != null)
		{
			// signature unit error
			// check unit
			// continue => finance office out of order notification
			// cancel => critical
			
			int res = JConfirmDialog.showConfirm(m_app, caller,
					null,
					AppLocal.getIntString("signature.signatureunit.failure"));
		
			if (res == JOptionPane.YES_OPTION) {
				m_isActive = true;
				m_isOutOfOrder = true;
				Log.Info2DB("Signature: use out of order");
			}
			else
			{
				m_isCriticalError = true;
				m_isActive = true;
				m_isOutOfOrder = true;
			}
			
		}
		else if(m_sigserialnumber != null 
				&& m_zdaId != null
				&& m_dbsigserialnumber != null 
				&& (!m_sigserialnumber.equals(m_dbsigserialnumber)
						|| !m_zdaId.equals(m_dbzdaId))
		)
		{
			// different certificate
			// cancel (quit) or continue with new certificate
			// old certificate cannot be used afterwards
			// register new signature unit at finance office
			int res = JConfirmDialog.showConfirm(m_app, caller, 
					null,
					AppLocal.getIntString("signature.registernextsignatureunit"));
		
			if (res == JOptionPane.YES_OPTION) {
				
				// add signature unit
				if(!AddNewSignatureDevice(caller, false, false))
				{
					JConfirmDialog.showError(m_app, 
							caller, 
							null, 
							AppLocal.getIntString("signature.registernextsignatureunit.failure"));
					m_isActive = true;
					m_isOutOfOrder = true;
					m_isCriticalError = true;
				}
				else
				{
					m_isActive = true;
					m_isOutOfOrder = false;
					Log.Info2DB("Signature: signature unit added (with online registration)");
				}
			}
			else
			{
				int resOffline = JConfirmDialog.showConfirm(m_app, caller, 
						null,
						AppLocal.getIntString("signature.registernextsignatureunit.offline"));
				
				if (resOffline == JOptionPane.YES_OPTION) {
					// add signature unit
					if(!AddNewSignatureDevice(caller, false, true))
					{
						JConfirmDialog.showError(m_app, 
								caller, 
								null, 
								AppLocal.getIntString("signature.registernextsignatureunit.failure"));
						m_isActive = true;
						m_isOutOfOrder = true;
						m_isCriticalError = true;
					}
					else
					{
						m_isActive = true;
						m_isOutOfOrder = false;
						Log.Info2DB("Signature: signature unit added (with offline registration)");
					}
				}
				else
				{
					m_isActive = true;
					m_isOutOfOrder = true;
					m_isCriticalError = true;
				}
			}
		}
		else if(m_sigserialnumber != null 
				&& m_dbsigserialnumber == null 
				&& dbsigCount == 0)
		{
			// first certificate
			// register new signature unit at finance office first
			
			int res = JConfirmDialog.showConfirm(m_app, caller, 
					null,
					AppLocal.getIntString("signature.registerfirstsignatureunit"));
		
			if (res == JOptionPane.YES_OPTION) {
				
				// add signature unit
				if(!AddNewSignatureDevice(caller, true, false))
				{
					JConfirmDialog.showError(m_app, 
							caller, 
							null, 
							AppLocal.getIntString("signature.registerfirstsignatureunit.failure"));
					m_isActive = false;
					m_isOutOfOrder = true;
				}
				else
				{
					m_isActive = true;
					m_isOutOfOrder = false;
					Log.Info2DB("Signature: first signature unit added (with online registration)");
				}
			}
			else
			{
				int resOffline = JConfirmDialog.showConfirm(m_app, caller, 
						null,
						AppLocal.getIntString("signature.registerfirstsignatureunit.offline"));
				if (resOffline == JOptionPane.YES_OPTION) {
					if(!AddNewSignatureDevice(caller, true, true))
					{
						JConfirmDialog.showError(m_app, 
								caller, 
								null, 
								AppLocal.getIntString("signature.registerfirstsignatureunit.failure"));
						m_isActive = false;
						m_isOutOfOrder = true;
					}
					else
					{
						m_isActive = true;
						m_isOutOfOrder = false;
						Log.Info2DB("Signature: first signature unit added (with offline registration)");
					}
				}
				else
				{
					m_isActive = false;
					m_isOutOfOrder = true;
				}
			}
		}
		else
		{
			m_isActive = dbsigCount > 0;
			m_isOutOfOrder = false;
		}
		
		
		// check outage
		CheckOutageInterval48h(caller);
		
		if(m_isActive 
				&& !isStartup
				&& m_isFirstTicket)
		{
			JConfirmDialog.showError(m_app, 
					caller, 
					null,
					AppLocal.getIntString("signature.startticket.failure")
					);
			m_isOutOfOrder = true;
			m_isCriticalError = true;
		}
		
		// check month ticket
		if(m_isActive 
				&& !isStartup)
		{
			CheckMonthTicket(caller);
		}
	}
	
	public void CheckOpenTicketValidations(Component caller)
	{
		// fix error with wrong year ticket
		try {
			new StaticSentence(m_session, 
					"UPDATE TICKETS SET VALIDATION=NULL WHERE MONTH=201801 AND (SELECT COUNT(*) FROM TICKETS WHERE MONTH BETWEEN 201701 AND 201712 AND VALIDATION is not null) > 0 AND VALIDATION IS NOT NULL "
				)
			.exec();
		} catch(Exception e) {
			// do nothing
		}
		
		List oDbticketIds = null;
		try {
			oDbticketIds = new StaticSentence(m_session, 
					"SELECT T.CASHTICKETID " +
					"	FROM TICKETS T " +
					"	INNER JOIN " +
					"	RECEIPTS R " +
					"	on T.ID = R.ID " +
					"	WHERE  " +
					"	 VALIDATION IS NOT NULL " + 
					"	 AND VALIDATION = 0 " +
					" ORDER BY T.CASHTICKETID ",
					null,
					SerializerReadInteger.INSTANCE
					).list();
			
			if(oDbticketIds != null)
			{
				for(Integer cashTicketid : (List<Integer>)oDbticketIds)
				{
					TicketInfo ticket = m_dlSales.loadTicket(true, cashTicketid, m_taxeslogic);
					String rksvnotes = ticket.getProperty("rksvnotes", "Barbelegnr.:" + cashTicketid.toString());
					int res = JConfirmDialog.showConfirm(m_app, 
							caller, 
							null,
							String.format(AppLocal.getIntString("signature.ticket.post.validation"), rksvnotes)
							);
					if(res == JOptionPane.YES_OPTION)
					{
						CheckTicketOnline(caller, ticket);
					}
					else
					{
						Log.Info2DB(String.format("Signature: check ticket %1$s canceled", rksvnotes));
					}
				}
			}
			
		} catch (BasicException e) {
			m_isCriticalError = true;
		}
	}
	
	public void SetTicketValidation(TicketInfo ticket, Boolean isOfflineValidation) throws BasicException
	{
		Integer validationState = isOfflineValidation == null || isOfflineValidation ? 1 : 2;
		
		new StaticSentence(m_session, 
				"UPDATE TICKETS SET VALIDATION=? WHERE CASHTICKETID = ? ",
				new SerializerWriteBasic(new Datas[] {Datas.INT, Datas.INT})
			)
		.exec(new Object[] { validationState, ticket.getCashTicketId() }); 
	}
	
	
	public void CheckMonthTicket(Component caller)
	{
		// read last month ticket information
		try {
			Object oDblastMonth = new StaticSentence(m_session, 
					"SELECT MONTH FROM ( "
					+ "SELECT CASE WHEN T.MONTH = 0 THEN EXTRACT(year FROM ADD_MONTHS(r.DATENEW,-1))*100 + EXTRACT(month FROM ADD_MONTHS(r.DATENEW,-1)) ELSE T.MONTH END AS MONTH  "
					+ "	FROM TICKETS T "
					+ "   INNER JOIN RECEIPTS R ON T.ID=R.ID "
					+ "	WHERE  "
					+ "	T.MONTH IS NOT NULL " 
					+ "ORDER BY T.MONTH DESC)"
					+ "WHERE ROWNUM <= 1 ",
					null,
					SerializerReadInteger.INSTANCE
					).find();
			Integer storedLastMonth = null;
			if(oDblastMonth != null)
			{
				storedLastMonth = (Integer)oDblastMonth;
			}
			String cashID = m_app.getActiveCashIndex(false, true); // used to reload cache
			Date activeCashDateStart = m_app.getActiveCashDateStart();
			if(activeCashDateStart == null)
				activeCashDateStart = new Date();
			
			// build last month integer
			Calendar c = Calendar.getInstance();
			c.setTime(activeCashDateStart);
			// int currentMonth = c.get(Calendar.MONTH) + 1; // + 1 because January = 0 

			// current month
			int currentYear = c.get(Calendar.YEAR);
			int currentMonth = c.get(Calendar.MONTH) + 1; // + 1 because January = 0 
			int currentYearMonth = (currentYear * 100 + currentMonth);
			
			
			// last month
			c.add(Calendar.MONTH, -1);
			int lastYear = c.get(Calendar.YEAR);
			int lastMonth = c.get(Calendar.MONTH) + 1; // + 1 because January = 0 
			int lastYearMonth = (lastYear * 100 + lastMonth);
			
			
			// 12 + 88 = 100
			Boolean isYearTicket = (storedLastMonth != null ? storedLastMonth : lastYearMonth) + 88 < currentYear * 100;
			
			if(storedLastMonth != null && lastYearMonth > storedLastMonth)
			{
				if((GetIsOutOfOrder() || GetIsCriticalError()) 
						&& isYearTicket)
				{
					// year ticket
					// signature has to work!!
					JConfirmDialog.showError(m_app, 
							caller, 
							null,
							AppLocal.getIntString("signature.yearticket.outoforder.failure")
							);
					m_isCriticalError = true;
					return;
				}
				
				// signature unit is working
				JConfirmDialog.showInformation(m_app, 
						caller, 
						null,
						AppLocal.getIntString("signature.monthticket")
						);
				
				
				// create and print month ticket
				TicketInfo monthTicket = new TicketInfo();
				PaymentInfoCash paymentInfoCash = new PaymentInfoCash(0, 0); 
				List<PaymentInfo> payments = new ArrayList<PaymentInfo>();
				payments.add(paymentInfoCash);
				monthTicket.setPayments(payments);
				monthTicket.setActiveCash(m_app.getLastCashIndex());
				monthTicket.setDate(new Date());
				monthTicket.setTicketType(TicketInfo.RECEIPT_NORMAL);
				monthTicket.setUser(m_app.getAppUserView().getUser().getUserInfo());
				if(isYearTicket)
				{
					monthTicket.setProperty("rksvnotes", String.format("Jahresbeleg %1$s", lastYearMonth));
					monthTicket.setValidation(0); // needs fiscal validation
				}
				else
				{
					monthTicket.setProperty("rksvnotes", String.format("Monatsbeleg %1$s", lastYearMonth));
					monthTicket.setValidation(null); // validation not required
				}
				monthTicket.setMonth(lastYearMonth);
				
				m_taxeslogic.calculateTaxes(monthTicket);
				m_dlSales.saveTicket(monthTicket, m_app.getInventoryLocation(), m_taxeslogic);
				
				// Print receipt.
				String[] bonsize = m_app.getProperties().getProperty("machine.printer").split(",");
				String ticketsuffix = "";
				if (bonsize.length > 2)
					ticketsuffix = "." + bonsize[2];
				m_panelticket.printTicket("Printer.Ticket" + ticketsuffix, monthTicket, null);
				
				
				if(isYearTicket)
				{
					// year ticket has to be checked via fiscal services
					CheckTicketOnline(caller, monthTicket);
				}
			}
			
		} catch (Exception e) {
			m_isCriticalError = true;
			JConfirmDialog.showError(m_app, 
					caller, 
					null,
					AppLocal.getIntString("signature.monthticket.failure"),
					e
					);
			
		}
		
		
		
		
		
	}
	
	
	public void CheckSignatureOutage(Component caller)
	{
		if(m_isActive 
				&& !m_isOutOfOrder
				&& m_isOutOfOrderInPast
				)
		{
			// signature unit is working
			JConfirmDialog.showInformation(m_app, 
					caller, 
					null,
					AppLocal.getIntString("signature.outoforderend")
					);
			
			try {
				
				
				Date signatureUnitOutageStart = GetSignatureUnitOutageStartTime();
				if(signatureUnitOutageStart != null)
				{
					// outage end notification to fiscal office
					// online or offline
					int resOnline = JConfirmDialog.showConfirm(m_app, caller, 
							null,
							AppLocal.getIntString("signature.outoforderend.online"));
					if (resOnline == JOptionPane.YES_OPTION) {
						SetFONSignatureUnitOutOfOrderEnd(caller, Calendar.getInstance());
						Log.Info2DB("Signature: online out of order end notification");
						SetSignatureUnitOutageEnd();
					}
					else
					{
						int resOffline = JConfirmDialog.showConfirm(m_app, caller, 
								null,
								AppLocal.getIntString("signature.outoforderend.offline"));
						if (resOffline == JOptionPane.YES_OPTION) {
							SetSignatureUnitOutageEnd();
							Log.Info2DB("Signature: offline out of order end notification");
						}
						else
						{
							// stay offline
							Log.Info2DB("Signature: out of order end canceled");
							return; // exit immediately
						}
					}
				}
			
				m_isOutOfOrderInPast = false;
				m_isOutOfOrder = false;
				
				// print collective ticket
				List<Integer> cashTicketIds = GetTicketOutageCashTicketIds();
				Date outageStart = GetTicketOutageStartTime();
				
				Integer startcashTicketId = cashTicketIds.get(0);
				Integer endcashTicketId = cashTicketIds.get(cashTicketIds.size()-1);
				
				TicketInfo collectiveTicket = new TicketInfo();
				PaymentInfoCash paymentInfoCash = new PaymentInfoCash(0, 0); 
				List<PaymentInfo> payments = new ArrayList<PaymentInfo>();
				payments.add(paymentInfoCash);
				collectiveTicket.setPayments(payments);
				collectiveTicket.setActiveCash(m_app.getActiveCashIndex(true, true));
				Date endDate = new Date();
				collectiveTicket.setDate(endDate);
				collectiveTicket.setTicketType(TicketInfo.RECEIPT_NORMAL);
				collectiveTicket.setUser(m_app.getAppUserView().getUser().getUserInfo());
				collectiveTicket.setProperty("rksvnotes", String.format("Sammelbeleg - Signaturausfall von %1$s bis %2$s - Barbeleg von %3$s bis %4$s", Formats.TIMESTAMP.formatValue(outageStart), Formats.TIMESTAMP.formatValue(endDate), startcashTicketId, endcashTicketId));
				collectiveTicket.setValidation(null); // validation not required
				collectiveTicket.setMonth(null);
				m_taxeslogic.calculateTaxes(collectiveTicket);
				m_dlSales.saveTicket(collectiveTicket, m_app.getInventoryLocation(), m_taxeslogic);
				
				// Print receipt.
				String[] bonsize = m_app.getProperties().getProperty("machine.printer").split(",");
				String ticketsuffix = "";
				if (bonsize.length > 2)
					ticketsuffix = "." + bonsize[2];
				m_panelticket.printTicket("Printer.Ticket" + ticketsuffix, collectiveTicket, null);
				
			} catch(Exception ex)
			{
				Log.Exception(ex);
				
				m_isOutOfOrderInPast = true;
				m_isOutOfOrder = false;
				m_isCriticalError = true;
				JConfirmDialog.showError(m_app, 
						caller, 
						null, 
						AppLocal.getIntString("signature.outoforderend.failure"));
			}
		}
		
	}
	
	
	public void CheckStartTicket(Component caller)
	{
		if(GetIsActive()
			&& !GetIsOutOfOrder()
			&& GetIsFirstTicket())
		{
			JConfirmDialog.showInformation(m_app, 
					caller, 
					AppLocal.getIntString("error.information"),
					AppLocal.getIntString("signature.startticket")
					);
			
			try {
				TicketInfo startTicket = new TicketInfo();
				PaymentInfoCash paymentInfoCash = new PaymentInfoCash(0, 0); 
				List<PaymentInfo> payments = new ArrayList<PaymentInfo>();
				payments.add(paymentInfoCash);
				startTicket.setPayments(payments);
				startTicket.setActiveCash(m_app.getActiveCashIndex(true, true));
				startTicket.setDate(new Date());
				startTicket.setTicketType(TicketInfo.RECEIPT_NORMAL);
				startTicket.setUser(m_app.getAppUserView().getUser().getUserInfo());
				startTicket.setProperty("rksvnotes", "Startbeleg");
				// TaxInfo tax0 = taxeslogic.getTaxInfo(0.0);
				// TicketLineInfo tl = new TicketLineInfo("Start Beleg", tax0.getTaxCategoryID(), 0.0, 0.0, tax0);
				// startTicket.addLine(tl);
				startTicket.setValidation(0); // validation required
				startTicket.setMonth(0);
				m_taxeslogic.calculateTaxes(startTicket);
				m_dlSales.saveTicket(startTicket, m_app.getInventoryLocation(), m_taxeslogic);
				
				// Print receipt.
				String[] bonsize = m_app.getProperties().getProperty("machine.printer").split(",");
				String ticketsuffix = "";
				if (bonsize.length > 2)
					ticketsuffix = "." + bonsize[2];
				m_panelticket.printTicket("Printer.Ticket" + ticketsuffix, startTicket, null);
				
				// start ticket has to be checked via FON
				CheckTicketOnline(caller, startTicket);
				
			} catch(Exception ex)
			{
				Log.Exception(ex);
				
				JConfirmDialog.showError(m_app, 
						caller, 
						null, 
						AppLocal.getIntString("signature.startticket.failure"));
				
				((JPrincipalApp) m_app.getAppUserView()).getAppview().closeAppView();
			}
		}
	}
	
	
	public void CheckOutageInterval48h(Component caller)
	{
		if(m_isOutOfOrder)
		{
			Date signatureUnitOutageStartTime = GetSignatureUnitOutageStartTime();
			if(signatureUnitOutageStartTime == null)
			{
				Date outageStartTime = GetTicketOutageStartTime();
				
				Calendar past48h = Calendar.getInstance();
				past48h.add(Calendar.HOUR_OF_DAY, -48);
				if(outageStartTime != null && 
						outageStartTime.before(past48h.getTime()))
				{
					// notify fiscal office
					int resOnline = JConfirmDialog.showConfirm(m_app, caller, 
							null,
							AppLocal.getIntString("signature.signatureunitoutage48h.online"));
					if (resOnline == JOptionPane.YES_OPTION) {
						Calendar calOutageStartTime = Calendar.getInstance();
						calOutageStartTime.setTime(outageStartTime);
						try {	
							SetFONSignatureUnitOutOfOrder(caller, calOutageStartTime);
							SetSignatureUnitOutageStartTime(outageStartTime, false);
							Log.Info2DB("Signature: out of order 48 h online");
						} catch(Exception e)
						{
							Log.Exception(e);
							JConfirmDialog.showError(m_app, 
									caller, 
									null,
									AppLocal.getIntString("signature.signatureunitoutage48h.failure")
									);
						}
					}
					else
					{
						int resOffline = JConfirmDialog.showConfirm(m_app, caller, 
								null,
								AppLocal.getIntString("signature.signatureunitoutage48h.offline"));
						if (resOffline == JOptionPane.YES_OPTION) {
							SetSignatureUnitOutageStartTime(outageStartTime, true);
							Log.Info2DB("Signature: out of order 48 h offline");
						}
					}
				}
			}
		}
	}
	
	private void SetSignatureUnitOutageStartTime(Date outageStartTime, Boolean isOutageRegOffline)
	{
		try {
			new StaticSentence(m_session, 
						"UPDATE SIGNATURES SET OUTAGESINCE=?, OUTAGEREGOFFLINE=? WHERE ACTIVE = 1 AND DBSERVERNAME = SYS_CONTEXT('USERENV', 'SERVER_HOST') ",
						new SerializerWriteBasic(new Datas[] {Datas.TIMESTAMP, Datas.INT})
					)
				.exec(new Object[] {outageStartTime, isOutageRegOffline ? 1 : 0});
		} catch (BasicException e) {
			Log.Exception(e);
			m_isCriticalError = true;
		}
	}
	
	private void SetSignatureUnitOutageEnd()
	{
		try {
			new PreparedSentence(m_session, 
						"UPDATE SIGNATURES SET OUTAGESINCE=null, OUTAGEREGOFFLINE=null WHERE ACTIVE = 1 AND DBSERVERNAME = SYS_CONTEXT('USERENV', 'SERVER_HOST') "
					).exec();
		} catch (BasicException e) {
			Log.Exception(e);
			m_isCriticalError = true;
		}
	}
	
	private Date GetSignatureUnitOutageStartTime()
	{
		Object oDboutagestarttime = null;
		try {
			oDboutagestarttime = new StaticSentence(m_session, 
					"SELECT OUTAGESINCE FROM SIGNATURES WHERE ACTIVE = 1 AND DBSERVERNAME = SYS_CONTEXT('USERENV', 'SERVER_HOST') ",
					null,
					SerializerReadTimestamp.INSTANCE
					).find();
		} catch (BasicException e) {
			m_isCriticalError = true;
		}
		
		if(oDboutagestarttime != null)
			return (Date)oDboutagestarttime;
		else
			return null;
	}
	
	
	private List<Integer> GetTicketOutageCashTicketIds()
	{
		List oDbticketIds = null;
		try {
			oDbticketIds = new StaticSentence(m_session, 
					"SELECT T.CASHTICKETID " +
					"	FROM TICKETS T " +
					"	INNER JOIN " +
					"	RECEIPTS R " +
					"	on T.ID = R.ID " +
					"	WHERE  " +
					"	SIGNATUREOUTOFORDER IS NOT NULL " + 
					"	AND SIGNATUREOUTOFORDER=1 " +
					"	AND CASHTICKETID IS NOT NULL " +
					"	AND CASHTICKETID <= (SELECT NVL(MAX(CASHTICKETID),-1) FROM TICKETS WHERE CASHTICKETID IS NOT NULL) " +
					"	AND CASHTICKETID > (SELECT NVL(MAX(CASHTICKETID),-1) FROM TICKETS WHERE CASHTICKETID IS NOT NULL AND SIGNATUREOUTOFORDER IS NOT NULL AND SIGNATUREOUTOFORDER=0) " +
					"ORDER BY T.CASHTICKETID ",
					null,
					SerializerReadInteger.INSTANCE
					).list();
		} catch (BasicException e) {
			m_isCriticalError = true;
		}
		
		if(oDbticketIds != null)
			return (List<Integer>)oDbticketIds;
		else
			return null;
	}
	
	private Date GetTicketOutageStartTime()
	{
		Object oDboutagestarttime = null;
		try {
			oDboutagestarttime = new StaticSentence(m_session, 
					"SELECT MIN(R.DATENEW) " +
					"	FROM TICKETS T " +
					"	INNER JOIN " +
					"	RECEIPTS R " +
					"	on T.ID = R.ID " +
					"	WHERE  " +
					"	SIGNATUREOUTOFORDER IS NOT NULL " + 
					"	AND SIGNATUREOUTOFORDER=1 " +
					"	AND CASHTICKETID IS NOT NULL " +
					"	AND CASHTICKETID <= (SELECT NVL(MAX(CASHTICKETID),-1) FROM TICKETS WHERE CASHTICKETID IS NOT NULL) " +
					"	AND CASHTICKETID > (SELECT NVL(MAX(CASHTICKETID),-1) FROM TICKETS WHERE CASHTICKETID IS NOT NULL AND SIGNATUREOUTOFORDER IS NOT NULL AND SIGNATUREOUTOFORDER=0)",
					null,
					SerializerReadTimestamp.INSTANCE
					).find();
		} catch (BasicException e) {
			m_isCriticalError = true;
		}
		
		if(oDboutagestarttime != null)
			return (Date)oDboutagestarttime;
		else
			return null;
	}
	
	public void CheckSignatureExpiration(Component caller)
	{
		if(m_sigCert != null)
		{
			Calendar c = Calendar.getInstance();
			if(m_sigCert.getNotAfter().before(c.getTime()))
			{
				// certificate is expired
				// signature unit is working => notify finance office 
				JConfirmDialog.showError(m_app, 
						caller, 
						null,
						AppLocal.getIntString("signature.expired")
						);
				m_isCriticalError = true;
				return;
			}
			
			c.add(Calendar.MONTH, 1);
			if(m_sigCert.getNotAfter().before(c.getTime()))
			{
				// certificate expires within the next 2 months
				String expDate = new SimpleDateFormat("dd.MM.yyyy").format(m_sigCert.getNotAfter());
				JConfirmDialog.showInformation(m_app, 
						caller, 
						null,
						String.format(AppLocal.getIntString("signature.expiresinXdays"), expDate)
						);
				Log.Info2DB("Signature: certificate expiration (within next month))");
				return;
			}
			
		}
	}

	public void CheckTicketOnline(Component caller, TicketInfo ticket)
	{
		try {
			String rksvnotes = ticket.getProperty("rksvnotes", "Barbelegnr.:" + ticket.getCashTicketId());
			int dialogres = JConfirmDialog.showConfirm(m_app, 
					caller, 
					null,
					AppLocal.getIntString("signature.ticket.validation")
					);
			if(dialogres == JOptionPane.YES_OPTION)
			{
				CheckFONTicket(caller, ticket);
				SetTicketValidation(ticket, false);
				Log.Info2DB(String.format("Signature: check ticket %1$s online", rksvnotes));
			}
			else
			{
				int dialogresoffline = JConfirmDialog.showConfirm(m_app, 
						caller, 
						null,
						AppLocal.getIntString("signature.ticket.validation.offline")
						);
				if(dialogresoffline == JOptionPane.YES_OPTION)
				{
					SetTicketValidation(ticket, true);
					Log.Info2DB(String.format("Signature: check ticket %1$s offline", rksvnotes));
				}
				else
				{
					Log.Info2DB(String.format("Signature: check ticket %1$s cancelled", rksvnotes));
				}
			}
		} catch(Exception e)
		{
			Log.Exception(e);
			JConfirmDialog.showInformation(m_app, 
					caller, 
					null,
					AppLocal.getIntString("signature.ticket.validation.failure")
					);
		}
	}
	
	
	public void DEPExport(Component caller)
	{
		DEPExport(caller, null, null);
	}
	
	public void DEPExport(String exportPath)
	{
		DEPExport(null, null, exportPath);
	}
	
	public void DEPExport(String exportPath, String posID)
	{
		DEPExport(null, posID, exportPath);
	}
	
	public void DEPExport(Component caller, String posID, String exportPath)
	{
		if(caller != null && exportPath == null)
		{
			JFileChooser chooser = new JFileChooser(); 
			chooser.setDialogTitle("Export Verzeichnis");
			chooser.setCurrentDirectory(
					chooser.getFileSystemView().getParentDirectory(
							new File("C:\\"))
					);
		    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    chooser.showDialog(caller, null);
		    File f = chooser.getSelectedFile();
		    if(f != null)
		    	exportPath = f.getAbsolutePath();
		    else
		    	return; // return and do nothing
		}
		
		
		try {
			List<DEPExportTicket> depExportTickets = (List<DEPExportTicket>)new StaticSentence(m_session,
					"select " 
					+ "  '_R' " 
					+ "  || t.ALGORITHMID "
					+ "  || '-'   "
					+ "  || s.CERTPROVIDERID "
					+ "  || '_'   "
					+ "  || t.POSID "
					+ "  || '_'   "
					+ "  || t.CASHTICKETID "
					+ "  || '_'   "
					+ "  || TO_CHAR(r.DATENEW,'YYYY-MM-dd\"T\"HH24:MI:SS') "
					+ "  || '_'   "
					+ "  || REPLACE(LTRIM(TO_CHAR(NVL(ROUND(TL20.BASE + TL20.AMOUNT,2),0),'999999999990D00')),'.',',') "
					+ "  || '_'   "
					+ "  || REPLACE(LTRIM(TO_CHAR(NVL(ROUND(TL10.BASE + TL10.AMOUNT,2),0),'999999999990D00')),'.',',') "
					+ "  || '_'   "
					+ "  || REPLACE(LTRIM(TO_CHAR(NVL(ROUND(TL13.BASE + TL13.AMOUNT,2),0),'999999999990D00')),'.',',') "
					+ "  || '_'   "
					+ "  || REPLACE(LTRIM(TO_CHAR(NVL(ROUND(TL00.BASE + TL00.AMOUNT,2),0),'999999999990D00')),'.',',') "
					+ "  || '_'   "
					+ "  || REPLACE(LTRIM(TO_CHAR(NVL(ROUND(TL19.BASE + TL19.AMOUNT,2),0),'999999999990D00')),'.',',') "
					+ "  || '_'  "
					+ "  || UTL_RAW.CAST_TO_VARCHAR2(t.CASHSUMCOUNTERENC) "
					+ "  || '_'  "
					+ "  || LTRIM(TO_CHAR(TO_NUMBER(SYS.UTL_RAW.CAST_TO_VARCHAR2(CERTSERIAL)),'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX')) "
					+ "  || '_'  "
					+ "  || UTL_RAW.CAST_TO_VARCHAR2(t.CHAINVALUE) "
					+ "    as ClearText, "
					+ "  t.SignatureValue, "
					+ "  s.ID as SignatureId, "
					+ "  t.POSID, "
					+ "  t.CashTicketId, "
					+ "  t.isBranchTicket "
					+ "from (select 0 as isBranchTicket, Tickets.* from Tickets UNION ALL select 1 as isBranchTicket, BRANCH_TICKETS.* from BRANCH_TICKETS) t " 
					+ "inner join "
					+ "    (select * from Receipts UNION ALL select * from  BRANCH_RECEIPTS) r "
					+ "    on r.ID=t.ID "
					+ "inner join "
					+ "    Signatures s on t.SIGNATUREID = s.ID "
					+ "inner join "
					+ "    CERTIFICATECHAIN cc on s.ID = cc.SIGNATUREID and cc.CHAINNO = 1 "
					+ "left join "
					+ "    (select * from TAXLINES UNION ALL select * from BRANCH_TAXLINES) TL20 INNER JOIN TAXES TA20 ON TL20.TAXID=TA20.ID " 
	                + "    on TL20.RECEIPT=T.ID AND TA20.RATE=0.2 "
	                + "left join "
	                + "    (select * from TAXLINES UNION ALL select * from BRANCH_TAXLINES) TL10 INNER JOIN TAXES TA10 ON TL10.TAXID=TA10.ID " 
	                + "    on TL10.RECEIPT=T.ID AND TA10.RATE=0.1 "
	                + "left join "
	                + "    (select * from TAXLINES UNION ALL select * from BRANCH_TAXLINES) TL13 INNER JOIN TAXES TA13 ON TL13.TAXID=TA13.ID " 
	                + "    on TL13.RECEIPT=T.ID AND TA13.RATE=0.13 "
	                + "left join "
	                + "    (select * from TAXLINES UNION ALL select * from BRANCH_TAXLINES) TL00 INNER JOIN TAXES TA00 ON TL00.TAXID=TA00.ID " 
	                + "    on TL00.RECEIPT=T.ID AND TA00.RATE=0 "
	                + "left join "
	                + "    (select * from TAXLINES UNION ALL select * from BRANCH_TAXLINES) TL19 INNER JOIN TAXES TA19 ON TL19.TAXID=TA19.ID " 
	                + "    on TL19.RECEIPT=T.ID AND TA19.RATE=0.19 "
					+ "WHERE CashTicketID is not null "
					+ "    and (? is null or t.POSID = ?) "
					+ "ORDER BY t.POSID, t.CASHTICKETID ",
					new SerializerWriteBasic(new Datas[] {Datas.STRING}), 
					new SerializerReadClass(DEPExportTicket.class))
				.list(new Object[] { posID, posID });
			
			
			
			HashMap<String, String> signaturePosMap = new HashMap<String, String>();
			HashMap<String, JsonObjectBuilder> signatureJsonMap = new HashMap<String, JsonObjectBuilder>();
			HashMap<String, JsonArrayBuilder> signarureJsonTicktesMap = new HashMap<String, JsonArrayBuilder>();
			
			JsonBuilderFactory factory = Json.createBuilderFactory(null);
			JsonArrayBuilder qrExportBuilder = factory.createArrayBuilder(); 
			
			for(DEPExportTicket depExportticket : depExportTickets)
			{
				if(GetIsDevelopment())
				{
					if(m_dlSales != null 
							&& m_taxeslogic != null 
							&& depExportticket.getIsBranchTicket() == 0)
					{
						TicketInfo ticket = m_dlSales.loadTicket(true, depExportticket.getCashTicketId(), m_taxeslogic);
						String qrCodeRaw = ticket.getSigningClearText() + "_" + ticket.getSignatureValue();
						qrExportBuilder.add(qrCodeRaw);
					}
				}
				
				
				String signatureId = depExportticket.getSignatureId();
				String posId = signaturePosMap.get(signatureId);
				if(posId == null)
				{
					posId = depExportticket.getPosId();
					signaturePosMap.put(signatureId, posId);
				}
					
				JsonObjectBuilder jsonExport = signatureJsonMap.get(signatureId);
				if(jsonExport == null)
				{
					jsonExport = factory.createObjectBuilder();
					jsonExport = jsonExport.add("Signaturzertifikat", convert2base64UrlSafe(GetSignatureDERBase64(depExportticket.getSignatureId())));	
					JsonArrayBuilder certificationChain = factory.createArrayBuilder().add(convert2base64UrlSafe(GetSignatureProviderDERBase64(depExportticket.getSignatureId())));
					jsonExport = jsonExport.add("Zertifizierungsstellen", certificationChain);
					signatureJsonMap.put(signatureId, jsonExport);
				}
				
				JsonArrayBuilder jsonExportTickets = signarureJsonTicktesMap.get(signatureId);
				if(jsonExportTickets == null)
				{
					jsonExportTickets = factory.createArrayBuilder();
				}
				String ticketLine = String.format("%1$s.%2$s.%3$s",
						base64Encode("{\"alg\":\"ES256\"}".getBytes(StandardCharsets.UTF_8) , true),
						base64Encode(depExportticket.getClearText().getBytes(StandardCharsets.UTF_8) , true),
						convert2base64UrlSafe(depExportticket.getSignatureValueBase64())
						);
				
				jsonExportTickets = jsonExportTickets.add(ticketLine);
				signarureJsonTicktesMap.put(signatureId, jsonExportTickets);
			}
			
			HashMap<String, JsonArrayBuilder> posJsonMap = new HashMap<String, JsonArrayBuilder>();
			for(Entry<String, String> signaturePos : signaturePosMap.entrySet())
			{
				String posId = signaturePos.getValue();
				String signatureId = signaturePos.getKey();
				
				JsonArrayBuilder rootJson = posJsonMap.get(posId);
				if(rootJson == null)
					rootJson = factory.createArrayBuilder();
				
				// add every signature for posid
				JsonObjectBuilder jsonSignature = signatureJsonMap.get(signatureId);
				JsonArrayBuilder jsonTickets = signarureJsonTicktesMap.get(signatureId);
				jsonSignature = jsonSignature.add("Belege-kompakt", jsonTickets);
				rootJson = rootJson.add(jsonSignature);
				
				posJsonMap.put(posId, rootJson);
				
			}
			
			for(Entry<String, JsonArrayBuilder> posExport : posJsonMap.entrySet())
			{
				String posId = posExport.getKey();
				JsonArrayBuilder jsonExportTicketsGroup = posExport.getValue();
				JsonObjectBuilder jsonExport = factory.createObjectBuilder();
				jsonExport = jsonExport.add("Belege-Gruppe", jsonExportTicketsGroup);
				JsonObject jsonExportObject = jsonExport.build();
				String rawExport = jsonExportObject
						.toString();
						//.replace("{", "{\n")
						//.replace("[", "[\n")
						//.replace("}", "\n}")
						//.replace("]", "\n]")
						//.replace(",", ",\n");
				
				byte data[] = rawExport.getBytes(StandardCharsets.UTF_8);
				
				FileOutputStream out = new FileOutputStream(exportPath + "\\dep-export-" + posId + ".json");
				out.write(data);
				out.close();
				
				
				
			}
			
			// only on development systems
			if(GetIsDevelopment())
			{
				String qrExportRaw = qrExportBuilder.build().toString();
				byte qrdata[] = qrExportRaw.getBytes(StandardCharsets.UTF_8);
			
				FileOutputStream out = new FileOutputStream(exportPath + "\\qr-export.json");
				out.write(qrdata);
				out.close();
			}
			
			
			
		} catch (Exception e) {
			if(caller != null)
			{
				JConfirmDialog.showError(m_app, 
						caller, 
						null, 
						AppLocal.getIntString("signature.depexport.failure"));
			}
			
			Log.Exception("DEP export failed", e);
			Log.Info2DB("Signature: DEP Export failed");
		}
		
		
		
	}
	
	
	public String GetPOSID()
	{
		return m_posid;
	}
	
	public String GetSignatureUnitInformation(Boolean withAES)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Signatureinheit:");
		sb.append("\n");
		
		sb.append("Art der Sicherheitseinrichtung: Signaturkarte");
		sb.append("\n");
		
		sb.append("Vertraunsdienstanbieter: ");
		if(m_zdaId != null && m_zdaId.equals("AT1"))
			sb.append("AT1 A-TRUST");
		sb.append("\n");
		
		sb.append("Seriennummer der Signaturzertifikates: ");
		if(m_sigserialnumber != null)
			sb.append(m_sigserialnumber);
		sb.append("\n");
		sb.append("\n");
		
		sb.append("Registrierkasse:");
		sb.append("\n");
		
		sb.append("Kassenidentifikationsnummer: ");
		if(m_posid != null)
			sb.append(m_posid);
		
		if(withAES)
		{
			sb.append("\n");
			sb.append("Benutzerschluessel AES-256: ");
			if(m_aeskey != null)
			{
				String aesKeyBase64 = base64Encode(m_aeskey.getEncoded(), false);
				sb.append(aesKeyBase64);
				sb.append("\n");
				
				sb.append("Pruefwert fuer Benutzerschluessel: ");
				
				String checkSum = "";
				try {
					checkSum = calcCheckSumFromKey(aesKeyBase64, 3);
				} catch (NoSuchAlgorithmException e) {
				}
				sb.append(checkSum);		
			}
		}
		
		return sb.toString();
	}
	
	public static String calcCheckSumFromKey(String base64AESKey, int N) throws NoSuchAlgorithmException 
	{ 
		MessageDigest md = MessageDigest.getInstance("SHA-256"); 
		byte[] sha256hash = md.digest(base64AESKey.getBytes()); 
		byte[] sha256hashNbytes = new byte[N]; 
		System.arraycopy(sha256hash , 0, sha256hashNbytes, 0, N); 
		String base64sha256hashNbytes = base64Encode(sha256hashNbytes, false); 
		String valSumCalc = base64sha256hashNbytes.replace("=", ""); 
		return valSumCalc; 
	}
	
	public String Sign(String payload) throws SignatureUnitException
	{
        
		if(!m_isActive)
			return base64Encode("Sicherheitseinrichtung nicht in Betrieb".getBytes(StandardCharsets.UTF_8), false);
		
		if(m_isOutOfOrder)
			return base64Encode("Sicherheitseinrichtung ausgefallen".getBytes(StandardCharsets.UTF_8), false);
		
		String JWS_Protected_Header = base64Encode("{\"alg\":\"ES256\"}".getBytes(StandardCharsets.UTF_8), true);
        String JWS_Payload = base64Encode(payload.getBytes(StandardCharsets.UTF_8), true);
        String toBeSignedStr = JWS_Protected_Header + "." + JWS_Payload;
		
		byte[] ret = JWSModul.Sign(toBeSignedStr.getBytes(StandardCharsets.UTF_8));
		if(ret == null)
		{
			m_isCriticalError = true;
			throw new SignatureUnitException("Signature device in critical error sate!");
		}
		
		return base64Encode(ret, false);
	}
	
	
	private Boolean AddNewSignatureDevice(Component caller, Boolean isFirst, Boolean isOfflineRegistration)
	{
		
		
		try {
			try {
				m_session.begin();
				
				if(isFirst)
				{
					SetPOSID();
				}
				
				// deactivate old signature units
				new PreparedSentence(m_session,
						"UPDATE SIGNATURES SET ACTIVE = 0 WHERE DBSERVERNAME = SYS_CONTEXT('USERENV', 'SERVER_HOST') ").exec();
				
				String signatureID = UUID.randomUUID().toString();
				// insert new signature unit
				new PreparedSentence(m_session,
						"INSERT INTO SIGNATURES (ID, DBSERVERNAME, CERTSERIAL, CERTDERBASE64, CERTPROVIDERID, ACTIVE, REGOFFLINE) "
						+ "VALUES (?, SYS_CONTEXT('USERENV', 'SERVER_HOST'), ?, ?, ?, 1, ?) ",
						new SerializerWriteBasic(new Datas[] { Datas.STRING, Datas.BYTES, Datas.BYTES, Datas.STRING, Datas.INT }))
				.exec(new Object[] { 
						signatureID, 
						m_sigserialnumber.getBytes(StandardCharsets.UTF_8), 
						base64Encode(m_sigCert.getEncoded(), false).getBytes(StandardCharsets.UTF_8), 
						m_zdaId,
						isOfflineRegistration ? 1 : 0
						});
				
				// insert certificate chain data
				new PreparedSentence(m_session,
						"INSERT INTO CERTIFICATECHAIN (ID, SIGNATUREID, CHAINNO, CERTDERBASE64) "
						+ "VALUES (?, ?, 1, ?) ",
						new SerializerWriteBasic(new Datas[] { Datas.STRING, Datas.STRING, Datas.BYTES }))
				.exec(new Object[] { 
						UUID.randomUUID().toString(), 
						signatureID,
 						base64Encode(m_issuerCert.getEncoded(), false).getBytes(StandardCharsets.UTF_8)
						});
				
				// FON WS add cash box (isFirst && !isOfflineRegistration)
				if(isFirst && !isOfflineRegistration)
				{
					AddFONCashBox(caller);
				}
				// FON WS add signature unit (!isOfflineRegistration)
				if(!isOfflineRegistration)
				{
					AddFONSignatureUnit(caller);
				}
				
				m_session.commit();
				m_dbactivesignatureid = signatureID; // set new active signature id
				return true;
			} catch (Exception e) {
				m_isCriticalError = true;
				m_session.rollback();
				Log.Exception(e);
			}
		} catch (Exception e) {
			m_isCriticalError = true;
			Log.Exception(e);
		}
		return false;
	}
	
	
	private void SetPOSID() throws Exception
	{
		
		if(m_sigserialnumber != null)
		{
			String hostCheckSum = calcCheckSumFromKey(m_app.getWindowsHost(), 2);
			String posid = hostCheckSum + (new StringBuilder(m_sigserialnumber).reverse().toString());
			
			// first certificate serial number as unique POS identification
			if(posid.length() > 200)
				posid = posid.substring(0, 200);
			
			new PreparedSentence(m_session,
					"UPDATE APPLICATIONS SET POSID = ? "
					+ "WHERE ID = 'w4cashdb' AND DBSERVERNAME = SYS_CONTEXT('USERENV', 'SERVER_HOST')",
					new SerializerWriteBasic(new Datas[] { Datas.STRING })).exec(new Object[] { posid });
			
			m_posid = posid;
			
		}
		else
		{
			throw new Exception("Signature serial number missing");
		}
	}
	
	private void SetSession(Session session)
	{
		m_session = session;
	}
	
	private void SetSignatureUnitWSAddress()
	{
		String serverAddress = "localhost";
		
		String serverAddressProperties = m_app.getProperties().getProperty("signature.address");
		
		if(serverAddressProperties == null || "".equals(serverAddressProperties))
		{
			try {
				String url = m_session.getURL();
				if(m_session.DB.getClass().equals(SessionDBOracle.class))
				{
					if(url.split("@").length == 2)
					{
						serverAddress = url.split("@")[1].split(":")[0];
					}
				}
			} catch (SQLException e) {
				Log.Exception(e);
			}
		}
		else
			serverAddress = serverAddressProperties;
		
		JWSModul.SetEndpointServerAddress(serverAddress);
	}
	
	
	public String getTurnOverCounterEncrypted(int algorithmId, double cashSumCounter, String posID, int cashTicketId) throws BasicException {
        try
        {
			//NEW METHOD: convert sum to Euro-cent and add to turnover counter
            long turnoverCounter = Math.round(cashSumCounter * 100);

            //prepare IV for encryption process, the Initialisation Vector (IV) is calculating by concatenating and then
            //hashing the
            //receipt-identifier (Belegnummer) and
            //the cashbox-ID (Kassen-ID)

            //Get UTF-8 String representation of cashBox-ID (Kassen-ID), STRING in Java are already UTF-8 encoded, thus no
            //encoding transformation is done here
            //IMPORTANT HINT: NEVER EVER use the same "Kassen-ID" and "Belegnummer" for different receipts!!!!
            String cashBoxIDUTF8String = posID;
            String receiptIdentifierUTF8String = cashTicketId + ""; //the simple way to convert the long value to an UTF-8 String
            String IVUTF8StringRepresentation = cashBoxIDUTF8String + receiptIdentifierUTF8String;

            ///hash the String with the hash-algorithm defined
            // maybe in future use different algorithm based on algorithmId
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashValue = messageDigest.digest(IVUTF8StringRepresentation.getBytes());
            byte[] concatenatedHashValue = new byte[16];
            System.arraycopy(hashValue, 0, concatenatedHashValue, 0, 16);
            
            //encrypt the turnover counter using the AES key
            String base64EncryptedTurnOverValue = encryptCTR(concatenatedHashValue, turnoverCounter, m_aeskey);
            
            return base64EncryptedTurnOverValue;
		}catch(Exception ex)
		{
			throw new BasicException("Encrypt turnover counter failed", ex);
		}
    }
	
	public long getTurnOverCounterDecrypted(int algorithmId, String cashSumCounterEncrypted, String posID, int cashTicketId) throws BasicException {
        try
        {
            String cashBoxIDUTF8String = posID;
            String receiptIdentifierUTF8String = cashTicketId + ""; //the simple way to convert the long value to an UTF-8 String
            String IVUTF8StringRepresentation = cashBoxIDUTF8String + receiptIdentifierUTF8String;

            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashValue = messageDigest.digest(IVUTF8StringRepresentation.getBytes());
            byte[] concatenatedHashValue = new byte[16];
            System.arraycopy(hashValue, 0, concatenatedHashValue, 0, 16);
            
            //decrypt the turnover counter using the AES key
            long turnOverCounter = decryptCTR(concatenatedHashValue, cashSumCounterEncrypted, m_aeskey);
            
            return turnOverCounter;
		}catch(Exception ex)
		{
			throw new BasicException("Decrypt turnover counter failed", ex);
		}
    }
	
	public String calculateSignatureValuePreviousReceipt(int algorithmId, String posId, String lastTicketPayload, String lastSignatureValue) throws BasicException {
        try {
        	String inputForChainCalculation;
            //Detailspezifikation Abs 4 "Sig-Voriger-Beleg"
            //if the first receipt is stored, then the cashbox-identifier is hashed and is used as chaining value
            //otherwise the complete last receipt is hased and the result is used as chaining value
            if (lastSignatureValue == null || lastTicketPayload == null) {
                inputForChainCalculation = posId;
            } else {
            	String JWS_Protected_Header = base64Encode("{\"alg\":\"ES256\"}".getBytes(StandardCharsets.UTF_8), true);
		        String JWS_Payload = base64Encode(lastTicketPayload.getBytes(StandardCharsets.UTF_8), true);
		        inputForChainCalculation = JWS_Protected_Header + "." + JWS_Payload + "." + convert2base64UrlSafe(lastSignatureValue);
            }

            //set hash algorithm from RK suite, in this case SHA-256
            // maybe in future use different algorithm based on algorithmIdFF
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            //calculate hash value
            md.update(inputForChainCalculation.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();

            //extract number of bytes (N, defined in RKsuite) from hash value
            // maybe in future use different length based on algorithmId
            int bytesToExtract = 8;
            byte[] conDigest = new byte[bytesToExtract];
            System.arraycopy(digest, 0, conDigest, 0, bytesToExtract);

            //encode value as BASE64 String ==> chainValue
            return base64Encode(conDigest, false);

        } catch (NoSuchAlgorithmException e) {
            throw new BasicException("Calculating signature value of previous receipt failed", e);
        }
    }
	
	
	/**
     * method for AES encryption in CTR mode
     *
     * @param concatenatedHashValue
     * @param turnoverCounter
     * @param symmetricKey
     */
    public String encryptCTR(byte[] concatenatedHashValue, Long turnoverCounter, SecretKey symmetricKey)
            throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

        // extract bytes 0-15 from hash value
        ByteBuffer byteBufferIV = ByteBuffer.allocate(16);
        byteBufferIV.put(concatenatedHashValue);
        byte[] IV = byteBufferIV.array();

        // prepare data
        // here, 8 bytes are used for the turnover counter (more then enough for
        // every possible turnover...), however
        // the specification only requires 5 bytes at a minimum
        // bytes 0-7 are used for the turnover counter, which is represented by
        // 8-byte
        // two-complement, Big Endian representation (equal to Java LONG), bytes
        // 8-15 are set to 0
        // negative values are possible (very rare)
        ByteBuffer byteBufferData = ByteBuffer.allocate(16);
        byteBufferData.putLong(turnoverCounter);
        byte[] data = byteBufferData.array();

        // prepare AES cipher with CTR/ICM mode, NoPadding is essential for the
        // decryption process. Padding could not be reconstructed due
        // to storing only 8 bytes of the cipher text (not the full 16 bytes)
        // (or 5 bytes if the mininum turnover length is used)
        IvParameterSpec ivSpec = new IvParameterSpec(IV);

        
        
        if(m_cipher == null)
        {
        	// switch off JCE key 128bit restrictions
            try {
                Field isRestricted = Class.forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted");
                if (Modifier.isFinal(isRestricted.getModifiers()) ) {
            		Field modifiers = Field.class.getDeclaredField("modifiers");
            		modifiers.setAccessible(true);
            		modifiers.setInt(isRestricted, isRestricted.getModifiers() & ~Modifier.FINAL);
            	}
                
                isRestricted.setAccessible(true);
                isRestricted.set(null, false); // isRestricted = false;
            } catch (Exception ex) {
            	Log.Exception("Switch off JCE key 128bit restrictions failed", ex);
            }
        	m_cipher = Cipher.getInstance("AES/CTR/NoPadding", "BC");
        }
        
        m_cipher.init(Cipher.ENCRYPT_MODE, symmetricKey, ivSpec);

        // encrypt the turnover value with the prepared cipher
        byte[] encryptedTurnOverValueComplete = m_cipher.doFinal(data);

        // extract bytes that will be stored in the receipt (only bytes 0-7)
        // cryptographic NOTE: this is only possible due to the use of the CTR
        // mode, would not work for ECB/CBC etc. modes
        byte[] encryptedTurnOverValue = new byte[8]; // or 5 bytes if min.
        // turnover length is
        // used
        System.arraycopy(encryptedTurnOverValueComplete, 0, encryptedTurnOverValue, 0, encryptedTurnOverValue.length);

        // encode result as BASE64

        return base64Encode(encryptedTurnOverValue, false);

    }

    /**
     * method for AES decryption in CTR mode
     *
     * @param concatenatedHashValue
     * @param base64EncryptedTurnOverValue
     * @param symmetricKey
     */
    public long decryptCTR(byte[] concatenatedHashValue, 
    		String base64EncryptedTurnOverValue, SecretKey symmetricKey)
            throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

        // extract bytes 0-15 from hash value
        ByteBuffer byteBufferIV = ByteBuffer.allocate(16);
        byteBufferIV.put(concatenatedHashValue);
        byte[] IV = byteBufferIV.array();

        byte[] encryptedTurnOverValue = base64Decode(base64EncryptedTurnOverValue, false);
        int lengthOfEncryptedTurnOverValue = encryptedTurnOverValue.length;

        // prepare AES cipher with CTR/ICM mode
        IvParameterSpec ivSpec = new IvParameterSpec(IV);

        // provider independent
        if(m_cipher == null)
        {
        	// switch off JCE key 128bit restrictions
            try {
                Field isRestricted = Class.forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted");
                if (Modifier.isFinal(isRestricted.getModifiers()) ) {
            		Field modifiers = Field.class.getDeclaredField("modifiers");
            		modifiers.setAccessible(true);
            		modifiers.setInt(isRestricted, isRestricted.getModifiers() & ~Modifier.FINAL);
            	}
                
                isRestricted.setAccessible(true);
                isRestricted.set(null, false); // isRestricted = false;
            } catch (Exception ex) {
            	Log.Exception("Switch off JCE key 128bit restrictions failed", ex);
            }
        	m_cipher = Cipher.getInstance("AES/CTR/NoPadding", "BC");
        }
        
        
        m_cipher.init(Cipher.DECRYPT_MODE, symmetricKey, ivSpec);
        byte[] testPlainTurnOverValueComplete = m_cipher.doFinal(encryptedTurnOverValue);

        // extract relevant bytes from decryption result
        byte[] testPlainTurnOverValue = new byte[lengthOfEncryptedTurnOverValue];
        System.arraycopy(testPlainTurnOverValueComplete, 0, testPlainTurnOverValue, 0, lengthOfEncryptedTurnOverValue);

        // create java LONG out of ByteArray
        ByteBuffer testPlainTurnOverValueByteBuffer = ByteBuffer.wrap(testPlainTurnOverValue);
        return testPlainTurnOverValueByteBuffer.getLong();
    }
	
	
	/**
     * Generates a random AES key for encrypting/decrypting the turnover value
     * ATTENTION: In a real cash box this key would be generated during the init process and stored in a secure area
     *
     * @return generated AES key
     */
    public static SecretKey createAESKey() {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            int keySize = 256;
            kgen.init(keySize);
            return kgen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    public static String convert2base64UrlSafe(String base64Data)
    {
    	Base64 encoderurlSafe = new Base64(true);
    	Base64 encoder = new Base64(false);
    	String ret = encoderurlSafe.encodeAsString(encoder.decode(base64Data)).replace("\r\n", "");
    	return ret;
    }
    
    
    /**
     * BASE64 encoding helper function
     *
     * @param data      binary representation of data to be encoded
     * @param isUrlSafe indicates whether BASE64 URL-safe encoding should be used (required for JWS)
     * @return BASE64 encoded representation of input data
     */
    public static String base64Encode(byte[] data, boolean isUrlSafe) {
        Base64 encoder = new Base64(isUrlSafe);
        String ret = new String(encoder.encode(data), StandardCharsets.UTF_8).replace("\r\n", "");
        return ret;
    }
    
    
    

    /**
     * BASE64 decoder helper function
     *
     * @param base64Data BASE64 encoded data
     * @param isUrlSafe  indicates whether BASE64 URL-safe encoding was used (required for JWS)
     * @return binary representation of decoded data
     */
    public static byte[] base64Decode(String base64Data, boolean isUrlSafe) {
    	Base64 encoder = new Base64(isUrlSafe);
    	return encoder.decode(base64Data);
    }

    public void AddFONCashBox(Component caller) throws Exception
    {
    	Rkdb rkdb = new Rkdb();
    	Registrierung_kasse regCashBox = new Registrierung_kasse();
    	regCashBox.setAnmerkung(m_app.getHost());
    	regCashBox.setBenutzerschluessel(base64Encode(m_aeskey.getEncoded(), false));
    	regCashBox.setKassenidentifikationsnummer(m_posid);
    	regCashBox.setSatznr(new PositiveInteger("1"));
    	rkdb.setRegistrierung_kasse(new Registrierung_kasse[] { regCashBox });
    	DoFONAction(caller, rkdb);
    }
    
    public void AddFONSignatureUnit(Component caller) throws Exception
    {
    	Rkdb rkdb = new Rkdb();
    	Registrierung_se signatureUnit = new Registrierung_se(); 
    	signatureUnit.setArt_se(Art_se.SIGNATURKARTE);
    	signatureUnit.setSatznr(new PositiveInteger("2"));
    	signatureUnit.setVda_id(GetZDAId());
    	signatureUnit.setZertifikatsseriennummer(new Zertifikatsseriennummer(m_sigserialnumber));
    	rkdb.setRegistrierung_se(new Registrierung_se[] { signatureUnit });
    	DoFONAction(caller, rkdb);
    }
    
    
    public void CheckFONTicket(Component caller, TicketInfo ticket) throws Exception
    {
    	Rkdb rkdb = new Rkdb();
    	Belegpruefung ticketCheck = new Belegpruefung();
    	ticketCheck.setSatznr(new PositiveInteger("3"));
    	String payload = ticket.getSigningClearText();
    	String signvalue = ticket.getSignatureValue();
    	String ticketText = payload + "_" + signvalue;
    	// ticketText = base64Encode(ticketText.getBytes(StandardCharsets.UTF_8), true);
    	ticketCheck.setBeleg(ticketText);
    	rkdb.setBelegpruefung(ticketCheck);
    	DoFONAction(caller, rkdb);
    }
    
    public void SetFONSignatureUnitOutOfOrder(Component caller, Calendar startOutage) throws Exception
    {
    	Rkdb rkdb = new Rkdb();
    	Ausfall_se outOfOrder = new Ausfall_se(); 
    	outOfOrder.setSatznr(new PositiveInteger("4"));
    	// 2 == Signatur- bzw. Siegelerstellung unmoeglich oder fehlerhaft
    	outOfOrder.setAusfall(new Ausfall(2, startOutage));
    	// use signatureserial from DB
    	outOfOrder.setZertifikatsseriennummer(new Zertifikatsseriennummer(m_dbsigserialnumber));
    	rkdb.setAusfall_se(new Ausfall_se[] { outOfOrder });
    	DoFONAction(caller, rkdb);
    }
    
    public void SetFONSignatureUnitOutOfOrderEnd(Component caller, Calendar endOutage) throws Exception
    {
    	Rkdb rkdb = new Rkdb();
    	Wiederinbetriebnahme_se outOfOrderEnd = new Wiederinbetriebnahme_se(); 
    	outOfOrderEnd.setSatznr(new PositiveInteger("5"));
    	outOfOrderEnd.setEnde_ausfall(endOutage);
    	outOfOrderEnd.setZertifikatsseriennummer(new Zertifikatsseriennummer(m_sigserialnumber));
    	rkdb.setWiederinbetriebnahme_se(new Wiederinbetriebnahme_se[] { outOfOrderEnd });
    	DoFONAction(caller, rkdb);
    }
    
    
    public void DoFONAction(Component caller, Rkdb rkdb) throws Exception
    {
    	LoginFON(caller);
    	if(m_wssessionid != null && !"".equals(m_wssessionid))
    	{
    		RkdbServicePortProxy proxy = new RkdbServicePortProxy();
    		((javax.xml.rpc.Stub)proxy.getRkdbServicePort())._setProperty(org.apache.axis.components.net.DefaultCommonsHTTPClientProperties.CONNECTION_DEFAULT_SO_TIMEOUT_KEY, 20000);
    		((javax.xml.rpc.Stub)proxy.getRkdbServicePort())._setProperty(org.apache.axis.components.net.DefaultCommonsHTTPClientProperties.CONNECTION_DEFAULT_CONNECTION_TIMEOUT_KEY, 20000);
    		
    		Calendar c = Calendar.getInstance();
    		rkdb.setTs_erstellung(c);
    		rkdb.setPaket_nr(new PositiveInteger("1"));
    		RkdbRequest request = new RkdbRequest();
    		request.setTid(m_wstid);
    		request.setBenid(m_wsbenid);
    		request.setId(m_wssessionid);
    		
    		
    		if(GetIsDevelopment())
    		{
    			// development/test
    			request.setArt_uebermittlung(Art_uebermittlung.T);
    		}
    		else
    		{
    			// production
    			request.setArt_uebermittlung(Art_uebermittlung.P);
    		}
    		request.setErzwinge_asynchron(false);
    		request.setRkdb(rkdb);

    		RkdbResponse response;
			try {

				response = proxy.rkdb(request);
				RkdbMessage message = response.getResult(0).getRkdbMessage(0);
				StringBuilder msgVerification = new StringBuilder();
				if(response.getResult(0).getVerificationResultList() != null)
				{
					for(VerificationResult verificationResult : response.getResult(0).getVerificationResultList())
					{
						String detailedMessage = verificationResult.getVerificationResultDetailedMessage();
						msgVerification.append('\n');
						msgVerification.append(detailedMessage);
					}
				}
				
				if(message.getRc() == null || "".equals(message.getRc()) || "0".equals(message.getRc()))
				{
					// OK
					JConfirmDialog.showInformation(m_app, caller, 
							null,
							AppLocal.getIntString("signature.wsaction.success"));
				}
				else if("-1".equals(message.getRc()))
				{
					// session timeout
					m_wssessionid = null;
					String msg = String.format("%1$s: %2$s", message.getRc(), message.getMsg());
					throw new Exception(msg);
				}
				else if("B1".equals(message.getRc()))
				{
					// cash box already registered
					Log.Info2DB("Signature: cashbox already registered");
					JConfirmDialog.showInformation(m_app, caller, 
							null,
							AppLocal.getIntString("signature.wsaction.success"));
				}
				else if("B10".equals(message.getRc()))
				{
					// signature unit already registered
					Log.Info2DB("Signature: signature unit already registered");
					JConfirmDialog.showInformation(m_app, caller, 
							null,
							AppLocal.getIntString("signature.wsaction.success"));
				}
				else
				{
					String msg = String.format("%1$s: %2$s%3$s", message.getRc(), message.getMsg(), msgVerification.toString());
					Log.Info2DB(msg);
					throw new Exception(msg);
				}
			} catch (Exception e) {
				int res = JConfirmDialog.showConfirm(m_app, caller, 
						null,
						AppLocal.getIntString("signature.wsaction.repeat", e.getMessage()));
				if (res == JOptionPane.YES_OPTION) {
					DoFONAction(caller, rkdb);
				}
				else
				{
					throw e;
				}
			}
    	}
    }
    
    
    public void LoginFON(Component caller) throws Exception
    {
    	if(m_wssessionid == null || "".equals(m_wssessionid))
    	{
    		// FON WS user login dialog
    		WSLoginDialog dialog = WSLoginDialog.newJDialog(m_app, caller);
    		LoginRequest loginRequest = dialog.showLoginDialog(m_wstid, m_wsbenid, m_wspin);
    		if(loginRequest != null)
    		{
    			loginRequest.setHerstellerid("ATU64043056"); // HB-SOFTSOLUTION.COM
				m_wstid = loginRequest.getTid();
				m_wsbenid = loginRequest.getBenid();
				m_wspin = loginRequest.getPin();
			
				SessionServicePortProxy proxy = new SessionServicePortProxy();
				((javax.xml.rpc.Stub)proxy.getSessionServicePort())._setProperty(org.apache.axis.components.net.DefaultCommonsHTTPClientProperties.CONNECTION_DEFAULT_SO_TIMEOUT_KEY, 20000);
	    		((javax.xml.rpc.Stub)proxy.getSessionServicePort())._setProperty(org.apache.axis.components.net.DefaultCommonsHTTPClientProperties.CONNECTION_DEFAULT_CONNECTION_TIMEOUT_KEY, 20000);

				try {
					LoginResponse response = proxy.login(loginRequest);
					m_wssessionid = response.getId();
					
					if(response.getRc() != 0)
					{
						String msg = response.getMsg();

						int res = JConfirmDialog.showConfirm(m_app, caller, 
								null,
								AppLocal.getIntString("signature.wslogin.repeat", msg));
						if (res == JOptionPane.YES_OPTION) {
							LoginFON(caller);
						}
						else
						{
							throw new Exception(msg);
						}
						
					}
				} catch (Exception e) {
					int res = JConfirmDialog.showConfirm(m_app, caller, 
							null,
							AppLocal.getIntString("signature.wslogin.repeat", e.getMessage()));
					if (res == JOptionPane.YES_OPTION) {
						LoginFON(caller);
					}
					else
					{
						throw e;
					}
				}
    		}
    		else
    		{
    			throw new Exception("No login data provided.");
    		}
    	}
    }
    
}
