package at.w4cash.JWS;

import java.io.ByteArrayInputStream;
import java.io.Console;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.security.PublicKey;
import java.security.cert.*;
import java.util.stream.Stream;

import org.tempuri.*;

import com.openbravo.pos.util.Base64Encoder;

import bsh.This;

public class JWSModul {
	 
	private static ISigProxy proxy;
	private static String zdaId;
	private static X509Certificate sigCert;
	private static X509Certificate issuerCert;
	
	private static ISigProxy GetProxy()
	{
		if(proxy == null)
		{
			proxy = new ISigProxy();
			proxy.setEndpoint("http://localhost:80/Temporary_Listen_Addresses/w4cashSig");
		}
		return proxy;
	}
	
	public static void SetEndpointServerAddress(String serverAddress)
	{
		String endPoint = String.format("http://%1$s:80/Temporary_Listen_Addresses/w4cashSig", serverAddress);
		proxy.setEndpoint(endPoint);
	}
	
	public static String GetZDAId()
	{
		if(zdaId == null)
		{
			try {
				zdaId = GetProxy().getInfoZDAId();
			} catch (RemoteException e) {
				// TODO: logging
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
				// TODO logging
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
				// TODO logging
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
	
	
	public static void main(String args[])
	{
		// for test
			SetEndpointServerAddress("192.168.0.66");
			String ZDAId = GetZDAId();
			/*
				
				byte[] cerRawDer = cer.getEncoded();
				String encoded = Base64Encoder.encode(cerRawDer);
				BigInteger serialNum = cer.getSerialNumber();
				
				byte[] cerIssuerRawDer = cerIssuer.getEncoded();
				String encodedIssuer = Base64Encoder.encode(cerIssuerRawDer);
				
				String x = cerIssuer.toString();
				
				
			} catch (CertificateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
			*/
		System.out.println(ZDAId);
	}

}
