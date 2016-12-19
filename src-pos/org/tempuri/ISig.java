/**
 * ISig.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tempuri;

public interface ISig extends java.rmi.Remote {
    public java.lang.String getInfoZDAId() throws java.rmi.RemoteException;
    public byte[] getInfoSigCert() throws java.rmi.RemoteException;
    public byte[] getInfoIssuerCert() throws java.rmi.RemoteException;
    public byte[] sign(byte[] toBeSigned) throws java.rmi.RemoteException;
}
