package at.gv.bmf.finanzonline.fon.ws.session;

public class SessionServicePortProxy implements at.gv.bmf.finanzonline.fon.ws.session.SessionServicePort {
  private String _endpoint = null;
  private at.gv.bmf.finanzonline.fon.ws.session.SessionServicePort sessionServicePort = null;
  
  public SessionServicePortProxy() {
    _initSessionServicePortProxy();
  }
  
  public SessionServicePortProxy(String endpoint) {
    _endpoint = endpoint;
    _initSessionServicePortProxy();
  }
  
  private void _initSessionServicePortProxy() {
    try {
      sessionServicePort = (new at.gv.bmf.finanzonline.fon.ws.session.SessionServiceLocator()).getsession();
      if (sessionServicePort != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)sessionServicePort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)sessionServicePort)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (sessionServicePort != null)
      ((javax.xml.rpc.Stub)sessionServicePort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public at.gv.bmf.finanzonline.fon.ws.session.SessionServicePort getSessionServicePort() {
    if (sessionServicePort == null)
      _initSessionServicePortProxy();
    return sessionServicePort;
  }
  
  public at.gv.bmf.finanzonline.fon.ws.session.LoginResponse login(at.gv.bmf.finanzonline.fon.ws.session.LoginRequest parameters) throws java.rmi.RemoteException{
    if (sessionServicePort == null)
      _initSessionServicePortProxy();
    return sessionServicePort.login(parameters);
  }
  
  public at.gv.bmf.finanzonline.fon.ws.session.LogoutResponse logout(at.gv.bmf.finanzonline.fon.ws.session.LogoutRequest parameters) throws java.rmi.RemoteException{
    if (sessionServicePort == null)
      _initSessionServicePortProxy();
    return sessionServicePort.logout(parameters);
  }
  
  
}