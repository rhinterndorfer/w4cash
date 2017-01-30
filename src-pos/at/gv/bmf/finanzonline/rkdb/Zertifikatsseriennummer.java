/**
 * Zertifikatsseriennummer.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package at.gv.bmf.finanzonline.rkdb;

public class Zertifikatsseriennummer  implements java.io.Serializable, org.apache.axis.encoding.SimpleType {
    private java.lang.Boolean hex;  // attribute
    private java.lang.String _value;
    
    public Zertifikatsseriennummer() {
    }

    // Simple Types must have a String constructor
    public Zertifikatsseriennummer(java.lang.String _value) {
        this._value = _value;
    }

    public java.lang.String toString() {
        return _value.toString();
    }
    
    /**
     * Gets the hex value for this Zertifikatsseriennummer.
     * 
     * @return hex
     */
    public java.lang.Boolean getHex() {
        return hex;
    }


    /**
     * Sets the hex value for this Zertifikatsseriennummer.
     * 
     * @param hex
     */
    public void setHex(java.lang.Boolean hex) {
        this.hex = hex;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Zertifikatsseriennummer)) return false;
        Zertifikatsseriennummer other = (Zertifikatsseriennummer) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.hex==null && other.getHex()==null) || 
             (this.hex!=null &&
              this.hex.equals(other.getHex())));
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
        if (getHex() != null) {
            _hashCode += getHex().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Zertifikatsseriennummer.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">zertifikatsseriennummer"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("hex");
        attrField.setXmlName(new javax.xml.namespace.QName("", "hex"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
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
          new  org.apache.axis.encoding.ser.SimpleSerializer(
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
          new  org.apache.axis.encoding.ser.SimpleDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
