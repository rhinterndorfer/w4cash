/**
 * Registrierung_se.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package at.gv.bmf.finanzonline.rkdb;

public class Registrierung_se  implements java.io.Serializable {
    private org.apache.axis.types.PositiveInteger satznr;

    private java.lang.String kundeninfo;

    private at.gv.bmf.finanzonline.rkdb.Art_se art_se;

    private java.lang.String vda_id;

    private at.gv.bmf.finanzonline.rkdb.Zertifikatsseriennummer zertifikatsseriennummer;

    private java.lang.String zertifikat;

    public Registrierung_se() {
    }

    public Registrierung_se(
           org.apache.axis.types.PositiveInteger satznr,
           java.lang.String kundeninfo,
           at.gv.bmf.finanzonline.rkdb.Art_se art_se,
           java.lang.String vda_id,
           at.gv.bmf.finanzonline.rkdb.Zertifikatsseriennummer zertifikatsseriennummer,
           java.lang.String zertifikat) {
           this.satznr = satznr;
           this.kundeninfo = kundeninfo;
           this.art_se = art_se;
           this.vda_id = vda_id;
           this.zertifikatsseriennummer = zertifikatsseriennummer;
           this.zertifikat = zertifikat;
    }


    /**
     * Gets the satznr value for this Registrierung_se.
     * 
     * @return satznr
     */
    public org.apache.axis.types.PositiveInteger getSatznr() {
        return satznr;
    }


    /**
     * Sets the satznr value for this Registrierung_se.
     * 
     * @param satznr
     */
    public void setSatznr(org.apache.axis.types.PositiveInteger satznr) {
        this.satznr = satznr;
    }


    /**
     * Gets the kundeninfo value for this Registrierung_se.
     * 
     * @return kundeninfo
     */
    public java.lang.String getKundeninfo() {
        return kundeninfo;
    }


    /**
     * Sets the kundeninfo value for this Registrierung_se.
     * 
     * @param kundeninfo
     */
    public void setKundeninfo(java.lang.String kundeninfo) {
        this.kundeninfo = kundeninfo;
    }


    /**
     * Gets the art_se value for this Registrierung_se.
     * 
     * @return art_se
     */
    public at.gv.bmf.finanzonline.rkdb.Art_se getArt_se() {
        return art_se;
    }


    /**
     * Sets the art_se value for this Registrierung_se.
     * 
     * @param art_se
     */
    public void setArt_se(at.gv.bmf.finanzonline.rkdb.Art_se art_se) {
        this.art_se = art_se;
    }


    /**
     * Gets the vda_id value for this Registrierung_se.
     * 
     * @return vda_id
     */
    public java.lang.String getVda_id() {
        return vda_id;
    }


    /**
     * Sets the vda_id value for this Registrierung_se.
     * 
     * @param vda_id
     */
    public void setVda_id(java.lang.String vda_id) {
        this.vda_id = vda_id;
    }


    /**
     * Gets the zertifikatsseriennummer value for this Registrierung_se.
     * 
     * @return zertifikatsseriennummer
     */
    public at.gv.bmf.finanzonline.rkdb.Zertifikatsseriennummer getZertifikatsseriennummer() {
        return zertifikatsseriennummer;
    }


    /**
     * Sets the zertifikatsseriennummer value for this Registrierung_se.
     * 
     * @param zertifikatsseriennummer
     */
    public void setZertifikatsseriennummer(at.gv.bmf.finanzonline.rkdb.Zertifikatsseriennummer zertifikatsseriennummer) {
        this.zertifikatsseriennummer = zertifikatsseriennummer;
    }


    /**
     * Gets the zertifikat value for this Registrierung_se.
     * 
     * @return zertifikat
     */
    public java.lang.String getZertifikat() {
        return zertifikat;
    }


    /**
     * Sets the zertifikat value for this Registrierung_se.
     * 
     * @param zertifikat
     */
    public void setZertifikat(java.lang.String zertifikat) {
        this.zertifikat = zertifikat;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Registrierung_se)) return false;
        Registrierung_se other = (Registrierung_se) obj;
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
            ((this.art_se==null && other.getArt_se()==null) || 
             (this.art_se!=null &&
              this.art_se.equals(other.getArt_se()))) &&
            ((this.vda_id==null && other.getVda_id()==null) || 
             (this.vda_id!=null &&
              this.vda_id.equals(other.getVda_id()))) &&
            ((this.zertifikatsseriennummer==null && other.getZertifikatsseriennummer()==null) || 
             (this.zertifikatsseriennummer!=null &&
              this.zertifikatsseriennummer.equals(other.getZertifikatsseriennummer()))) &&
            ((this.zertifikat==null && other.getZertifikat()==null) || 
             (this.zertifikat!=null &&
              this.zertifikat.equals(other.getZertifikat())));
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
        if (getArt_se() != null) {
            _hashCode += getArt_se().hashCode();
        }
        if (getVda_id() != null) {
            _hashCode += getVda_id().hashCode();
        }
        if (getZertifikatsseriennummer() != null) {
            _hashCode += getZertifikatsseriennummer().hashCode();
        }
        if (getZertifikat() != null) {
            _hashCode += getZertifikat().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Registrierung_se.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">registrierung_se"));
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
        elemField.setFieldName("art_se");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "art_se"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">art_se"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vda_id");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "vda_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">vda_id"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("zertifikatsseriennummer");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "zertifikatsseriennummer"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">zertifikatsseriennummer"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("zertifikat");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "zertifikat"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">zertifikat"));
        elemField.setMinOccurs(0);
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
