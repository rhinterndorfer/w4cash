package at.w4cash.signature;

import java.io.ByteArrayInputStream;
import java.io.Console;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.security.PublicKey;
import java.security.cert.*;
import java.util.stream.Stream;

import org.apache.axis.transport.http.HTTPConstants;
import org.tempuri.*;

import com.openbravo.pos.util.Base64Encoder;
import com.openbravo.pos.util.Log;

import bsh.This;

public class JWSModul {
	 
	private static ISigProxy proxy;
	private static String zdaId;
	private static X509Certificate sigCert;
	private static X509Certificate issuerCert;
	
	
	private static void reset()
	{
		zdaId = null;
		sigCert = null;
		issuerCert = null;
	}
	
	private static ISigProxy GetProxy()
	{
		if(proxy == null)
		{
			proxy = new ISigProxy();
	        ((javax.xml.rpc.Stub)(proxy.getISig()))._setProperty("axis.connection.timeout", 10000);
		}
		return proxy;
	}
	
	public static void SetEndpointServerAddress(String serverAddress)
	{
		String endPoint = String.format("http://%1$s:80/Temporary_Listen_Addresses/w4cashSig", serverAddress);
		GetProxy().setEndpoint(endPoint);
	}
	
	public static String GetZDAId()
	{
		if(zdaId == null)
		{
			try {
				zdaId = GetProxy().getInfoZDAId();
				if("".equals(zdaId))
					zdaId=null;
			} catch (RemoteException e) {
				Log.Exception(e);
				zdaId = null;
			}
		}
		
		return zdaId;
	}
	
	public static X509Certificate GetSigCert()
	{
		if(sigCert == null)
		{
			try {
				byte[] sigCertRaw = proxy.getInfoSigCert();
				ByteArrayInputStream biSigCert = new ByteArrayInputStream(sigCertRaw);
				sigCert = (X509Certificate)CertificateFactory.getInstance("X.509").generateCertificate(biSigCert);
			} catch (Exception e) {
				Log.Exception(e);
				sigCert = null;
			}
			
		}
		return sigCert;
	}
	
	public static X509Certificate GetIssuerCert()
	{
		if(issuerCert == null)
		{
			try {
				byte[] issuerCertRaw = proxy.getInfoIssuerCert();
				ByteArrayInputStream biIssuerCert = new ByteArrayInputStream(issuerCertRaw);
				issuerCert = (X509Certificate)CertificateFactory.getInstance("X.509").generateCertificate(biIssuerCert);
			} catch (Exception e) {
				Log.Exception(e);
				issuerCert = null;
			}
			
		}
		return issuerCert;
	}
	
	public static String GetIssuerCertSerialNumber()
	{
		X509Certificate cert = GetIssuerCert();
		if(cert != null)
			return cert.getSerialNumber().toString();
		else
			return null;
	}
	
	public static String GetSigCertSerialNumber()
	{
		X509Certificate cert = GetSigCert();
		if(cert != null)
			return cert.getSerialNumber().toString();
		else
			return null;
	}
	
	public static byte[] Sign(byte[] toBeSigned)
	{
		try {
			byte[] result = GetProxy().sign(toBeSigned);
			if(result == null)
				reset();
			
			return result;
		}
		catch(RemoteException ex)
		{
			Log.Exception(ex);
			reset();
			return null;
		}
	}

}
