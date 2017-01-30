/**
 * SessionServicePort.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package at.gv.bmf.finanzonline.fon.ws.session;

public interface SessionServicePort extends java.rmi.Remote {
    public at.gv.bmf.finanzonline.fon.ws.session.LoginResponse login(at.gv.bmf.finanzonline.fon.ws.session.LoginRequest parameters) throws java.rmi.RemoteException;
    public at.gv.bmf.finanzonline.fon.ws.session.LogoutResponse logout(at.gv.bmf.finanzonline.fon.ws.session.LogoutRequest parameters) throws java.rmi.RemoteException;
}
