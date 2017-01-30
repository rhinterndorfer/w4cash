package at.gv.bmf.finanzonline.rkdb;

public class RkdbServicePortProxy implements at.gv.bmf.finanzonline.rkdb.RkdbServicePort {
  private String _endpoint = null;
  private at.gv.bmf.finanzonline.rkdb.RkdbServicePort rkdbServicePort = null;
  
  public RkdbServicePortProxy() {
    _initRkdbServicePortProxy();
  }
  
  public RkdbServicePortProxy(String endpoint) {
    _endpoint = endpoint;
    _initRkdbServicePortProxy();
  }
  
  private void _initRkdbServicePortProxy() {
    try {
      rkdbServicePort = (new at.gv.bmf.finanzonline.rkdb.RkdbServiceLocator()).getrkdb();
      if (rkdbServicePort != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)rkdbServicePort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)rkdbServicePort)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (rkdbServicePort != null)
      ((javax.xml.rpc.Stub)rkdbServicePort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public at.gv.bmf.finanzonline.rkdb.RkdbServicePort getRkdbServicePort() {
    if (rkdbServicePort == null)
      _initRkdbServicePortProxy();
    return rkdbServicePort;
  }
  
  public at.gv.bmf.finanzonline.rkdb.RkdbResponse rkdb(at.gv.bmf.finanzonline.rkdb.RkdbRequest rkdbRequest) throws java.rmi.RemoteException{
    if (rkdbServicePort == null)
      _initRkdbServicePortProxy();
    return rkdbServicePort.rkdb(rkdbRequest);
  }
  
  
}