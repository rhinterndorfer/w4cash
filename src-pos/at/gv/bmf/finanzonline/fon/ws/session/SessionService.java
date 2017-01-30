/**
 * SessionService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package at.gv.bmf.finanzonline.fon.ws.session;

public interface SessionService extends javax.xml.rpc.Service {
    public java.lang.String getsessionAddress();

    public at.gv.bmf.finanzonline.fon.ws.session.SessionServicePort getsession() throws javax.xml.rpc.ServiceException;

    public at.gv.bmf.finanzonline.fon.ws.session.SessionServicePort getsession(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
