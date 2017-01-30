/**
 * Belegpruefung.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package at.gv.bmf.finanzonline.rkdb;

public class Belegpruefung  implements java.io.Serializable {
    private org.apache.axis.types.PositiveInteger satznr;

    private java.lang.String kundeninfo;

    private java.lang.String beleg;

    public Belegpruefung() {
    }

    public Belegpruefung(
           org.apache.axis.types.PositiveInteger satznr,
           java.lang.String kundeninfo,
           java.lang.String beleg) {
           this.satznr = satznr;
           this.kundeninfo = kundeninfo;
           this.beleg = beleg;
    }


    /**
     * Gets the satznr value for this Belegpruefung.
     * 
     * @return satznr
     */
    public org.apache.axis.types.PositiveInteger getSatznr() {
        return satznr;
    }


    /**
     * Sets the satznr value for this Belegpruefung.
     * 
     * @param satznr
     */
    public void setSatznr(org.apache.axis.types.PositiveInteger satznr) {
        this.satznr = satznr;
    }


    /**
     * Gets the kundeninfo value for this Belegpruefung.
     * 
     * @return kundeninfo
     */
    public java.lang.String getKundeninfo() {
        return kundeninfo;
    }


    /**
     * Sets the kundeninfo value for this Belegpruefung.
     * 
     * @param kundeninfo
     */
    public void setKundeninfo(java.lang.String kundeninfo) {
        this.kundeninfo = kundeninfo;
    }


    /**
     * Gets the beleg value for this Belegpruefung.
     * 
     * @return beleg
     */
    public java.lang.String getBeleg() {
        return beleg;
    }


    /**
     * Sets the beleg value for this Belegpruefung.
     * 
     * @param beleg
     */
    public void setBeleg(java.lang.String beleg) {
        this.beleg = beleg;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Belegpruefung)) return false;
        Belegpruefung other = (Belegpruefung) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.satznr==null && other.getSatznr()==null) || 
             (this.satznr!=null &&
              this.satznr.equals(other.getSatznr()))) &&
            ((this.kundeninfo==null && other.getKundeninfo()==null) || 
             (this.kundeninfo!=null &&
              this.kundeninfo.equals(other.getKundeninfo()))) &&
            ((this.beleg==null && other.getBeleg()==null) || 
             (this.beleg!=null &&
              this.beleg.equals(other.getBeleg())));
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
        if (getSatznr() != null) {
            _hashCode += getSatznr().hashCode();
        }
        if (getKundeninfo() != null) {
            _hashCode += getKundeninfo().hashCode();
        }
        if (getBeleg() != null) {
            _hashCode += getBeleg().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Belegpruefung.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">belegpruefung"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("satznr");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "satznr"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">satznr"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("kundeninfo");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "kundeninfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">kundeninfo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("beleg");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "beleg"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">beleg"));
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
