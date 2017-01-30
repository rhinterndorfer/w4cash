/**
 * Registrierung_kasse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package at.gv.bmf.finanzonline.rkdb;

public class Registrierung_kasse  implements java.io.Serializable {
    private org.apache.axis.types.PositiveInteger satznr;

    private java.lang.String kundeninfo;

    private java.lang.String kassenidentifikationsnummer;

    private java.lang.String anmerkung;

    private java.lang.String benutzerschluessel;

    public Registrierung_kasse() {
    }

    public Registrierung_kasse(
           org.apache.axis.types.PositiveInteger satznr,
           java.lang.String kundeninfo,
           java.lang.String kassenidentifikationsnummer,
           java.lang.String anmerkung,
           java.lang.String benutzerschluessel) {
           this.satznr = satznr;
           this.kundeninfo = kundeninfo;
           this.kassenidentifikationsnummer = kassenidentifikationsnummer;
           this.anmerkung = anmerkung;
           this.benutzerschluessel = benutzerschluessel;
    }


    /**
     * Gets the satznr value for this Registrierung_kasse.
     * 
     * @return satznr
     */
    public org.apache.axis.types.PositiveInteger getSatznr() {
        return satznr;
    }


    /**
     * Sets the satznr value for this Registrierung_kasse.
     * 
     * @param satznr
     */
    public void setSatznr(org.apache.axis.types.PositiveInteger satznr) {
        this.satznr = satznr;
    }


    /**
     * Gets the kundeninfo value for this Registrierung_kasse.
     * 
     * @return kundeninfo
     */
    public java.lang.String getKundeninfo() {
        return kundeninfo;
    }


    /**
     * Sets the kundeninfo value for this Registrierung_kasse.
     * 
     * @param kundeninfo
     */
    public void setKundeninfo(java.lang.String kundeninfo) {
        this.kundeninfo = kundeninfo;
    }


    /**
     * Gets the kassenidentifikationsnummer value for this Registrierung_kasse.
     * 
     * @return kassenidentifikationsnummer
     */
    public java.lang.String getKassenidentifikationsnummer() {
        return kassenidentifikationsnummer;
    }


    /**
     * Sets the kassenidentifikationsnummer value for this Registrierung_kasse.
     * 
     * @param kassenidentifikationsnummer
     */
    public void setKassenidentifikationsnummer(java.lang.String kassenidentifikationsnummer) {
        this.kassenidentifikationsnummer = kassenidentifikationsnummer;
    }


    /**
     * Gets the anmerkung value for this Registrierung_kasse.
     * 
     * @return anmerkung
     */
    public java.lang.String getAnmerkung() {
        return anmerkung;
    }


    /**
     * Sets the anmerkung value for this Registrierung_kasse.
     * 
     * @param anmerkung
     */
    public void setAnmerkung(java.lang.String anmerkung) {
        this.anmerkung = anmerkung;
    }


    /**
     * Gets the benutzerschluessel value for this Registrierung_kasse.
     * 
     * @return benutzerschluessel
     */
    public java.lang.String getBenutzerschluessel() {
        return benutzerschluessel;
    }


    /**
     * Sets the benutzerschluessel value for this Registrierung_kasse.
     * 
     * @param benutzerschluessel
     */
    public void setBenutzerschluessel(java.lang.String benutzerschluessel) {
        this.benutzerschluessel = benutzerschluessel;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Registrierung_kasse)) return false;
        Registrierung_kasse other = (Registrierung_kasse) obj;
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
            ((this.anmerkung==null && other.getAnmerkung()==null) || 
             (this.anmerkung!=null &&
              this.anmerkung.equals(other.getAnmerkung()))) &&
            ((this.benutzerschluessel==null && other.getBenutzerschluessel()==null) || 
             (this.benutzerschluessel!=null &&
              this.benutzerschluessel.equals(other.getBenutzerschluessel())));
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
        if (getAnmerkung() != null) {
            _hashCode += getAnmerkung().hashCode();
        }
        if (getBenutzerschluessel() != null) {
            _hashCode += getBenutzerschluessel().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Registrierung_kasse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">registrierung_kasse"));
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
        elemField.setFieldName("anmerkung");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "anmerkung"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">anmerkung"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("benutzerschluessel");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "benutzerschluessel"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">benutzerschluessel"));
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
