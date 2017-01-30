/**
 * Abfrage_ergebnis.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package at.gv.bmf.finanzonline.rkdb;

public class Abfrage_ergebnis  implements java.io.Serializable {
    private java.util.Calendar ts_registrierung;

    private at.gv.bmf.finanzonline.rkdb.Abfrage_ergebnisStatus status;

    private java.util.Calendar ts_status;

    public Abfrage_ergebnis() {
    }

    public Abfrage_ergebnis(
           java.util.Calendar ts_registrierung,
           at.gv.bmf.finanzonline.rkdb.Abfrage_ergebnisStatus status,
           java.util.Calendar ts_status) {
           this.ts_registrierung = ts_registrierung;
           this.status = status;
           this.ts_status = ts_status;
    }


    /**
     * Gets the ts_registrierung value for this Abfrage_ergebnis.
     * 
     * @return ts_registrierung
     */
    public java.util.Calendar getTs_registrierung() {
        return ts_registrierung;
    }


    /**
     * Sets the ts_registrierung value for this Abfrage_ergebnis.
     * 
     * @param ts_registrierung
     */
    public void setTs_registrierung(java.util.Calendar ts_registrierung) {
        this.ts_registrierung = ts_registrierung;
    }


    /**
     * Gets the status value for this Abfrage_ergebnis.
     * 
     * @return status
     */
    public at.gv.bmf.finanzonline.rkdb.Abfrage_ergebnisStatus getStatus() {
        return status;
    }


    /**
     * Sets the status value for this Abfrage_ergebnis.
     * 
     * @param status
     */
    public void setStatus(at.gv.bmf.finanzonline.rkdb.Abfrage_ergebnisStatus status) {
        this.status = status;
    }


    /**
     * Gets the ts_status value for this Abfrage_ergebnis.
     * 
     * @return ts_status
     */
    public java.util.Calendar getTs_status() {
        return ts_status;
    }


    /**
     * Sets the ts_status value for this Abfrage_ergebnis.
     * 
     * @param ts_status
     */
    public void setTs_status(java.util.Calendar ts_status) {
        this.ts_status = ts_status;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Abfrage_ergebnis)) return false;
        Abfrage_ergebnis other = (Abfrage_ergebnis) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.ts_registrierung==null && other.getTs_registrierung()==null) || 
             (this.ts_registrierung!=null &&
              this.ts_registrierung.equals(other.getTs_registrierung()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            ((this.ts_status==null && other.getTs_status()==null) || 
             (this.ts_status!=null &&
              this.ts_status.equals(other.getTs_status())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getTs_registrierung() != null) {
            _hashCode += getTs_registrierung().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getTs_status() != null) {
            _hashCode += getTs_status().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Abfrage_ergebnis.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">abfrage_ergebnis"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ts_registrierung");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ts_registrierung"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("", "status"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">>abfrage_ergebnis>status"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ts_status");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ts_status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
