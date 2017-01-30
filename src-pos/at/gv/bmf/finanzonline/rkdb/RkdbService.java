/**
 * RkdbService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package at.gv.bmf.finanzonline.rkdb;

public interface RkdbService extends javax.xml.rpc.Service {
    public java.lang.String getrkdbAddress();

    public at.gv.bmf.finanzonline.rkdb.RkdbServicePort getrkdb() throws javax.xml.rpc.ServiceException;

    public at.gv.bmf.finanzonline.rkdb.RkdbServicePort getrkdb(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
