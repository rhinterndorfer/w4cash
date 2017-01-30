/**
 * Wiederinbetriebnahme_kasse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package at.gv.bmf.finanzonline.rkdb;

public class Wiederinbetriebnahme_kasse  implements java.io.Serializable {
    private org.apache.axis.types.PositiveInteger satznr;

    private java.lang.String kundeninfo;

    private java.lang.String kassenidentifikationsnummer;

    private java.util.Calendar ende_ausfall;

    public Wiederinbetriebnahme_kasse() {
    }

    public Wiederinbetriebnahme_kasse(
           org.apache.axis.types.PositiveInteger satznr,
           java.lang.String kundeninfo,
           java.lang.String kassenidentifikationsnummer,
           java.util.Calendar ende_ausfall) {
           this.satznr = satznr;
           this.kundeninfo = kundeninfo;
           this.kassenidentifikationsnummer = kassenidentifikationsnummer;
           this.ende_ausfall = ende_ausfall;
    }


    /**
     * Gets the satznr value for this Wiederinbetriebnahme_kasse.
     * 
     * @return satznr
     */
    public org.apache.axis.types.PositiveInteger getSatznr() {
        return satznr;
    }


    /**
     * Sets the satznr value for this Wiederinbetriebnahme_kasse.
     * 
     * @param satznr
     */
    public void setSatznr(org.apache.axis.types.PositiveInteger satznr) {
        this.satznr = satznr;
    }


    /**
     * Gets the kundeninfo value for this Wiederinbetriebnahme_kasse.
     * 
     * @return kundeninfo
     */
    public java.lang.String getKundeninfo() {
        return kundeninfo;
    }


    /**
     * Sets the kundeninfo value for this Wiederinbetriebnahme_kasse.
     * 
     * @param kundeninfo
     */
    public void setKundeninfo(java.lang.String kundeninfo) {
        this.kundeninfo = kundeninfo;
    }


    /**
     * Gets the kassenidentifikationsnummer value for this Wiederinbetriebnahme_kasse.
     * 
     * @return kassenidentifikationsnummer
     */
    public java.lang.String getKassenidentifikationsnummer() {
        return kassenidentifikationsnummer;
    }


    /**
     * Sets the kassenidentifikationsnummer value for this Wiederinbetriebnahme_kasse.
     * 
     * @param kassenidentifikationsnummer
     */
    public void setKassenidentifikationsnummer(java.lang.String kassenidentifikationsnummer) {
        this.kassenidentifikationsnummer = kassenidentifikationsnummer;
    }


    /**
     * Gets the ende_ausfall value for this Wiederinbetriebnahme_kasse.
     * 
     * @return ende_ausfall
     */
    public java.util.Calendar getEnde_ausfall() {
        return ende_ausfall;
    }


    /**
     * Sets the ende_ausfall value for this Wiederinbetriebnahme_kasse.
     * 
     * @param ende_ausfall
     */
    public void setEnde_ausfall(java.util.Calendar ende_ausfall) {
        this.ende_ausfall = ende_ausfall;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Wiederinbetriebnahme_kasse)) return false;
        Wiederinbetriebnahme_kasse other = (Wiederinbetriebnahme_kasse) obj;
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
            ((this.kassenidentifikationsnummer==null && other.getKassenidentifikationsnummer()==null) || 
             (this.kassenidentifikationsnummer!=null &&
              this.kassenidentifikationsnummer.equals(other.getKassenidentifikationsnummer()))) &&
            ((this.ende_ausfall==null && other.getEnde_ausfall()==null) || 
             (this.ende_ausfall!=null &&
              this.ende_ausfall.equals(other.getEnde_ausfall())));
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
        if (getKassenidentifikationsnummer() != null) {
            _hashCode += getKassenidentifikationsnummer().hashCode();
        }
        if (getEnde_ausfall() != null) {
            _hashCode += getEnde_ausfall().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Wiederinbetriebnahme_kasse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">wiederinbetriebnahme_kasse"));
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
        elemField.setFieldName("kassenidentifikationsnummer");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "kassenidentifikationsnummer"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">kassenidentifikationsnummer"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ende_ausfall");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "ende_ausfall"));
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
