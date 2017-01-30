/**
 * SessionServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package at.gv.bmf.finanzonline.fon.ws.session;

public class SessionServiceLocator extends org.apache.axis.client.Service implements at.gv.bmf.finanzonline.fon.ws.session.SessionService {

    public SessionServiceLocator() {
    }


    public SessionServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SessionServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for session
    private java.lang.String session_address = "https://finanzonline.bmf.gv.at:443/fonws/ws/sessionService";

    public java.lang.String getsessionAddress() {
        return session_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String sessionWSDDServiceName = "session";

    public java.lang.String getsessionWSDDServiceName() {
        return sessionWSDDServiceName;
    }

    public void setsessionWSDDServiceName(java.lang.String name) {
        sessionWSDDServiceName = name;
    }

    public at.gv.bmf.finanzonline.fon.ws.session.SessionServicePort getsession() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(session_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getsession(endpoint);
    }

    public at.gv.bmf.finanzonline.fon.ws.session.SessionServicePort getsession(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            at.gv.bmf.finanzonline.fon.ws.session.SessionServiceBindingStub _stub = new at.gv.bmf.finanzonline.fon.ws.session.SessionServiceBindingStub(portAddress, this);
            _stub.setPortName(getsessionWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setsessionEndpointAddress(java.lang.String address) {
        session_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (at.gv.bmf.finanzonline.fon.ws.session.SessionServicePort.class.isAssignableFrom(serviceEndpointInterface)) {
                at.gv.bmf.finanzonline.fon.ws.session.SessionServiceBindingStub _stub = new at.gv.bmf.finanzonline.fon.ws.session.SessionServiceBindingStub(new java.net.URL(session_address), this);
                _stub.setPortName(getsessionWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("session".equals(inputPortName)) {
            return getsession();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/fon/ws/session", "sessionService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/fon/ws/session", "session"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("session".equals(portName)) {
            setsessionEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
