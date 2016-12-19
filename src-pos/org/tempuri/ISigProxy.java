package org.tempuri;

public class ISigProxy implements org.tempuri.ISig {
  private String _endpoint = null;
  private org.tempuri.ISig iSig = null;
  
  public ISigProxy() {
    _initISigProxy();
  }
  
  public ISigProxy(String endpoint) {
    _endpoint = endpoint;
    _initISigProxy();
  }
  
  private void _initISigProxy() {
    try {
      iSig = (new org.tempuri.SigLocator()).getBasicHttpBinding_ISig();
      if (iSig != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)iSig)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)iSig)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (iSig != null)
      ((javax.xml.rpc.Stub)iSig)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public org.tempuri.ISig getISig() {
    if (iSig == null)
      _initISigProxy();
    return iSig;
  }
  
  public java.lang.String getInfoZDAId() throws java.rmi.RemoteException{
    if (iSig == null)
      _initISigProxy();
    return iSig.getInfoZDAId();
  }
  
  public byte[] getInfoSigCert() throws java.rmi.RemoteException{
    if (iSig == null)
      _initISigProxy();
    return iSig.getInfoSigCert();
  }
  
  public byte[] getInfoIssuerCert() throws java.rmi.RemoteException{
    if (iSig == null)
      _initISigProxy();
    return iSig.getInfoIssuerCert();
  }
  
  public byte[] sign(byte[] toBeSigned) throws java.rmi.RemoteException{
    if (iSig == null)
      _initISigProxy();
    return iSig.sign(toBeSigned);
  }
  
  
}