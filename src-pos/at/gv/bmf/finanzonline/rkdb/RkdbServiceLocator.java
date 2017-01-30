/**
 * RkdbServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package at.gv.bmf.finanzonline.rkdb;

public class RkdbServiceLocator extends org.apache.axis.client.Service implements at.gv.bmf.finanzonline.rkdb.RkdbService {

    public RkdbServiceLocator() {
    }


    public RkdbServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public RkdbServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for rkdb
    private java.lang.String rkdb_address = "https://finanzonline.bmf.gv.at:443/fonws/ws/rkdb";

    public java.lang.String getrkdbAddress() {
        return rkdb_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String rkdbWSDDServiceName = "rkdb";

    public java.lang.String getrkdbWSDDServiceName() {
        return rkdbWSDDServiceName;
    }

    public void setrkdbWSDDServiceName(java.lang.String name) {
        rkdbWSDDServiceName = name;
    }

    public at.gv.bmf.finanzonline.rkdb.RkdbServicePort getrkdb() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(rkdb_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getrkdb(endpoint);
    }

    public at.gv.bmf.finanzonline.rkdb.RkdbServicePort getrkdb(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            at.gv.bmf.finanzonline.rkdb.RkdbServiceBindingStub _stub = new at.gv.bmf.finanzonline.rkdb.RkdbServiceBindingStub(portAddress, this);
            _stub.setPortName(getrkdbWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setrkdbEndpointAddress(java.lang.String address) {
        rkdb_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (at.gv.bmf.finanzonline.rkdb.RkdbServicePort.class.isAssignableFrom(serviceEndpointInterface)) {
                at.gv.bmf.finanzonline.rkdb.RkdbServiceBindingStub _stub = new at.gv.bmf.finanzonline.rkdb.RkdbServiceBindingStub(new java.net.URL(rkdb_address), this);
                _stub.setPortName(getrkdbWSDDServiceName());
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
        if ("rkdb".equals(inputPortName)) {
            return getrkdb();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "rkdbService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "rkdb"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("rkdb".equals(portName)) {
            setrkdbEndpointAddress(address);
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
