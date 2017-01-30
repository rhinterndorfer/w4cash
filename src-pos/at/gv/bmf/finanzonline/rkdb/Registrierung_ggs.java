/**
 * Registrierung_ggs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package at.gv.bmf.finanzonline.rkdb;

public class Registrierung_ggs  implements java.io.Serializable {
    private org.apache.axis.types.PositiveInteger satznr;

    private java.lang.String kundeninfo;

    private at.gv.bmf.finanzonline.rkdb.Art_ob art_ob;

    private java.lang.String ob;

    private java.lang.String zusatz_ob;

    private at.gv.bmf.finanzonline.rkdb.Art_se_ggs art_se_ggs;

    private java.lang.String public_key;

    private java.lang.String zertifikat;

    public Registrierung_ggs() {
    }

    public Registrierung_ggs(
           org.apache.axis.types.PositiveInteger satznr,
           java.lang.String kundeninfo,
           at.gv.bmf.finanzonline.rkdb.Art_ob art_ob,
           java.lang.String ob,
           java.lang.String zusatz_ob,
           at.gv.bmf.finanzonline.rkdb.Art_se_ggs art_se_ggs,
           java.lang.String public_key,
           java.lang.String zertifikat) {
           this.satznr = satznr;
           this.kundeninfo = kundeninfo;
           this.art_ob = art_ob;
           this.ob = ob;
           this.zusatz_ob = zusatz_ob;
           this.art_se_ggs = art_se_ggs;
           this.public_key = public_key;
           this.zertifikat = zertifikat;
    }


    /**
     * Gets the satznr value for this Registrierung_ggs.
     * 
     * @return satznr
     */
    public org.apache.axis.types.PositiveInteger getSatznr() {
        return satznr;
    }


    /**
     * Sets the satznr value for this Registrierung_ggs.
     * 
     * @param satznr
     */
    public void setSatznr(org.apache.axis.types.PositiveInteger satznr) {
        this.satznr = satznr;
    }


    /**
     * Gets the kundeninfo value for this Registrierung_ggs.
     * 
     * @return kundeninfo
     */
    public java.lang.String getKundeninfo() {
        return kundeninfo;
    }


    /**
     * Sets the kundeninfo value for this Registrierung_ggs.
     * 
     * @param kundeninfo
     */
    public void setKundeninfo(java.lang.String kundeninfo) {
        this.kundeninfo = kundeninfo;
    }


    /**
     * Gets the art_ob value for this Registrierung_ggs.
     * 
     * @return art_ob
     */
    public at.gv.bmf.finanzonline.rkdb.Art_ob getArt_ob() {
        return art_ob;
    }


    /**
     * Sets the art_ob value for this Registrierung_ggs.
     * 
     * @param art_ob
     */
    public void setArt_ob(at.gv.bmf.finanzonline.rkdb.Art_ob art_ob) {
        this.art_ob = art_ob;
    }


    /**
     * Gets the ob value for this Registrierung_ggs.
     * 
     * @return ob
     */
    public java.lang.String getOb() {
        return ob;
    }


    /**
     * Sets the ob value for this Registrierung_ggs.
     * 
     * @param ob
     */
    public void setOb(java.lang.String ob) {
        this.ob = ob;
    }


    /**
     * Gets the zusatz_ob value for this Registrierung_ggs.
     * 
     * @return zusatz_ob
     */
    public java.lang.String getZusatz_ob() {
        return zusatz_ob;
    }


    /**
     * Sets the zusatz_ob value for this Registrierung_ggs.
     * 
     * @param zusatz_ob
     */
    public void setZusatz_ob(java.lang.String zusatz_ob) {
        this.zusatz_ob = zusatz_ob;
    }


    /**
     * Gets the art_se_ggs value for this Registrierung_ggs.
     * 
     * @return art_se_ggs
     */
    public at.gv.bmf.finanzonline.rkdb.Art_se_ggs getArt_se_ggs() {
        return art_se_ggs;
    }


    /**
     * Sets the art_se_ggs value for this Registrierung_ggs.
     * 
     * @param art_se_ggs
     */
    public void setArt_se_ggs(at.gv.bmf.finanzonline.rkdb.Art_se_ggs art_se_ggs) {
        this.art_se_ggs = art_se_ggs;
    }


    /**
     * Gets the public_key value for this Registrierung_ggs.
     * 
     * @return public_key
     */
    public java.lang.String getPublic_key() {
        return public_key;
    }


    /**
     * Sets the public_key value for this Registrierung_ggs.
     * 
     * @param public_key
     */
    public void setPublic_key(java.lang.String public_key) {
        this.public_key = public_key;
    }


    /**
     * Gets the zertifikat value for this Registrierung_ggs.
     * 
     * @return zertifikat
     */
    public java.lang.String getZertifikat() {
        return zertifikat;
    }


    /**
     * Sets the zertifikat value for this Registrierung_ggs.
     * 
     * @param zertifikat
     */
    public void setZertifikat(java.lang.String zertifikat) {
        this.zertifikat = zertifikat;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Registrierung_ggs)) return false;
        Registrierung_ggs other = (Registrierung_ggs) obj;
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
            ((this.art_ob==null && other.getArt_ob()==null) || 
             (this.art_ob!=null &&
              this.art_ob.equals(other.getArt_ob()))) &&
            ((this.ob==null && other.getOb()==null) || 
             (this.ob!=null &&
              this.ob.equals(other.getOb()))) &&
            ((this.zusatz_ob==null && other.getZusatz_ob()==null) || 
             (this.zusatz_ob!=null &&
              this.zusatz_ob.equals(other.getZusatz_ob()))) &&
            ((this.art_se_ggs==null && other.getArt_se_ggs()==null) || 
             (this.art_se_ggs!=null &&
              this.art_se_ggs.equals(other.getArt_se_ggs()))) &&
            ((this.public_key==null && other.getPublic_key()==null) || 
             (this.public_key!=null &&
              this.public_key.equals(other.getPublic_key()))) &&
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
        if (getArt_ob() != null) {
            _hashCode += getArt_ob().hashCode();
        }
        if (getOb() != null) {
            _hashCode += getOb().hashCode();
        }
        if (getZusatz_ob() != null) {
            _hashCode += getZusatz_ob().hashCode();
        }
        if (getArt_se_ggs() != null) {
            _hashCode += getArt_se_ggs().hashCode();
        }
        if (getPublic_key() != null) {
            _hashCode += getPublic_key().hashCode();
        }
        if (getZertifikat() != null) {
            _hashCode += getZertifikat().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Registrierung_ggs.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">registrierung_ggs"));
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
        elemField.setFieldName("art_ob");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "art_ob"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">art_ob"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ob");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "ob"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">ob"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("zusatz_ob");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "zusatz_ob"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">zusatz_ob"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("art_se_ggs");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "art_se_ggs"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">art_se_ggs"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("public_key");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "public_key"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">public_key"));
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
