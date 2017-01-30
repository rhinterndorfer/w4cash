/**
 * Ausfall.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package at.gv.bmf.finanzonline.rkdb;

public class Ausfall  implements java.io.Serializable {
    private int begruendung;

    private java.util.Calendar beginn_ausfall;

    public Ausfall() {
    }

    public Ausfall(
           int begruendung,
           java.util.Calendar beginn_ausfall) {
           this.begruendung = begruendung;
           this.beginn_ausfall = beginn_ausfall;
    }


    /**
     * Gets the begruendung value for this Ausfall.
     * 
     * @return begruendung
     */
    public int getBegruendung() {
        return begruendung;
    }


    /**
     * Sets the begruendung value for this Ausfall.
     * 
     * @param begruendung
     */
    public void setBegruendung(int begruendung) {
        this.begruendung = begruendung;
    }


    /**
     * Gets the beginn_ausfall value for this Ausfall.
     * 
     * @return beginn_ausfall
     */
    public java.util.Calendar getBeginn_ausfall() {
        return beginn_ausfall;
    }


    /**
     * Sets the beginn_ausfall value for this Ausfall.
     * 
     * @param beginn_ausfall
     */
    public void setBeginn_ausfall(java.util.Calendar beginn_ausfall) {
        this.beginn_ausfall = beginn_ausfall;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Ausfall)) return false;
        Ausfall other = (Ausfall) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.begruendung == other.getBegruendung() &&
            ((this.beginn_ausfall==null && other.getBeginn_ausfall()==null) || 
             (this.beginn_ausfall!=null &&
              this.beginn_ausfall.equals(other.getBeginn_ausfall())));
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
        _hashCode += getBegruendung();
        if (getBeginn_ausfall() != null) {
            _hashCode += getBeginn_ausfall().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Ausfall.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">ausfall"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("begruendung");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "begruendung"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">begruendung"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("beginn_ausfall");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "beginn_ausfall"));
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
