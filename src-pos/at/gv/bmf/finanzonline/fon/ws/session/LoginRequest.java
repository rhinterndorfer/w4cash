/**
 * LoginRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package at.gv.bmf.finanzonline.fon.ws.session;

public class LoginRequest  implements java.io.Serializable {
    private java.lang.String tid;

    private java.lang.String benid;

    private java.lang.String pin;

    private java.lang.String herstellerid;

    public LoginRequest() {
    }

    public LoginRequest(
           java.lang.String tid,
           java.lang.String benid,
           java.lang.String pin,
           java.lang.String herstellerid) {
           this.tid = tid;
           this.benid = benid;
           this.pin = pin;
           this.herstellerid = herstellerid;
    }


    /**
     * Gets the tid value for this LoginRequest.
     * 
     * @return tid
     */
    public java.lang.String getTid() {
        return tid;
    }


    /**
     * Sets the tid value for this LoginRequest.
     * 
     * @param tid
     */
    public void setTid(java.lang.String tid) {
        this.tid = tid;
    }


    /**
     * Gets the benid value for this LoginRequest.
     * 
     * @return benid
     */
    public java.lang.String getBenid() {
        return benid;
    }


    /**
     * Sets the benid value for this LoginRequest.
     * 
     * @param benid
     */
    public void setBenid(java.lang.String benid) {
        this.benid = benid;
    }


    /**
     * Gets the pin value for this LoginRequest.
     * 
     * @return pin
     */
    public java.lang.String getPin() {
        return pin;
    }


    /**
     * Sets the pin value for this LoginRequest.
     * 
     * @param pin
     */
    public void setPin(java.lang.String pin) {
        this.pin = pin;
    }


    /**
     * Gets the herstellerid value for this LoginRequest.
     * 
     * @return herstellerid
     */
    public java.lang.String getHerstellerid() {
        return herstellerid;
    }


    /**
     * Sets the herstellerid value for this LoginRequest.
     * 
     * @param herstellerid
     */
    public void setHerstellerid(java.lang.String herstellerid) {
        this.herstellerid = herstellerid;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof LoginRequest)) return false;
        LoginRequest other = (LoginRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.tid==null && other.getTid()==null) || 
             (this.tid!=null &&
              this.tid.equals(other.getTid()))) &&
            ((this.benid==null && other.getBenid()==null) || 
             (this.benid!=null &&
              this.benid.equals(other.getBenid()))) &&
            ((this.pin==null && other.getPin()==null) || 
             (this.pin!=null &&
              this.pin.equals(other.getPin()))) &&
            ((this.herstellerid==null && other.getHerstellerid()==null) || 
             (this.herstellerid!=null &&
              this.herstellerid.equals(other.getHerstellerid())));
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
        if (getTid() != null) {
            _hashCode += getTid().hashCode();
        }
        if (getBenid() != null) {
            _hashCode += getBenid().hashCode();
        }
        if (getPin() != null) {
            _hashCode += getPin().hashCode();
        }
        if (getHerstellerid() != null) {
            _hashCode += getHerstellerid().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(LoginRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/fon/ws/session", ">loginRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tid");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/fon/ws/session", "tid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("benid");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/fon/ws/session", "benid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pin");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/fon/ws/session", "pin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("herstellerid");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/fon/ws/session", "herstellerid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
