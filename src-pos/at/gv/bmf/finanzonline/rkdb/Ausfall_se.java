/**
 * Ausfall_se.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package at.gv.bmf.finanzonline.rkdb;

public class Ausfall_se  implements java.io.Serializable {
    private org.apache.axis.types.PositiveInteger satznr;

    private java.lang.String kundeninfo;

    private at.gv.bmf.finanzonline.rkdb.Zertifikatsseriennummer zertifikatsseriennummer;

    private at.gv.bmf.finanzonline.rkdb.Ausfall ausfall;

    private at.gv.bmf.finanzonline.rkdb.Ausserbetriebnahme ausserbetriebnahme;

    public Ausfall_se() {
    }

    public Ausfall_se(
           org.apache.axis.types.PositiveInteger satznr,
           java.lang.String kundeninfo,
           at.gv.bmf.finanzonline.rkdb.Zertifikatsseriennummer zertifikatsseriennummer,
           at.gv.bmf.finanzonline.rkdb.Ausfall ausfall,
           at.gv.bmf.finanzonline.rkdb.Ausserbetriebnahme ausserbetriebnahme) {
           this.satznr = satznr;
           this.kundeninfo = kundeninfo;
           this.zertifikatsseriennummer = zertifikatsseriennummer;
           this.ausfall = ausfall;
           this.ausserbetriebnahme = ausserbetriebnahme;
    }


    /**
     * Gets the satznr value for this Ausfall_se.
     * 
     * @return satznr
     */
    public org.apache.axis.types.PositiveInteger getSatznr() {
        return satznr;
    }


    /**
     * Sets the satznr value for this Ausfall_se.
     * 
     * @param satznr
     */
    public void setSatznr(org.apache.axis.types.PositiveInteger satznr) {
        this.satznr = satznr;
    }


    /**
     * Gets the kundeninfo value for this Ausfall_se.
     * 
     * @return kundeninfo
     */
    public java.lang.String getKundeninfo() {
        return kundeninfo;
    }


    /**
     * Sets the kundeninfo value for this Ausfall_se.
     * 
     * @param kundeninfo
     */
    public void setKundeninfo(java.lang.String kundeninfo) {
        this.kundeninfo = kundeninfo;
    }


    /**
     * Gets the zertifikatsseriennummer value for this Ausfall_se.
     * 
     * @return zertifikatsseriennummer
     */
    public at.gv.bmf.finanzonline.rkdb.Zertifikatsseriennummer getZertifikatsseriennummer() {
        return zertifikatsseriennummer;
    }


    /**
     * Sets the zertifikatsseriennummer value for this Ausfall_se.
     * 
     * @param zertifikatsseriennummer
     */
    public void setZertifikatsseriennummer(at.gv.bmf.finanzonline.rkdb.Zertifikatsseriennummer zertifikatsseriennummer) {
        this.zertifikatsseriennummer = zertifikatsseriennummer;
    }


    /**
     * Gets the ausfall value for this Ausfall_se.
     * 
     * @return ausfall
     */
    public at.gv.bmf.finanzonline.rkdb.Ausfall getAusfall() {
        return ausfall;
    }


    /**
     * Sets the ausfall value for this Ausfall_se.
     * 
     * @param ausfall
     */
    public void setAusfall(at.gv.bmf.finanzonline.rkdb.Ausfall ausfall) {
        this.ausfall = ausfall;
    }


    /**
     * Gets the ausserbetriebnahme value for this Ausfall_se.
     * 
     * @return ausserbetriebnahme
     */
    public at.gv.bmf.finanzonline.rkdb.Ausserbetriebnahme getAusserbetriebnahme() {
        return ausserbetriebnahme;
    }


    /**
     * Sets the ausserbetriebnahme value for this Ausfall_se.
     * 
     * @param ausserbetriebnahme
     */
    public void setAusserbetriebnahme(at.gv.bmf.finanzonline.rkdb.Ausserbetriebnahme ausserbetriebnahme) {
        this.ausserbetriebnahme = ausserbetriebnahme;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Ausfall_se)) return false;
        Ausfall_se other = (Ausfall_se) obj;
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
            ((this.zertifikatsseriennummer==null && other.getZertifikatsseriennummer()==null) || 
             (this.zertifikatsseriennummer!=null &&
              this.zertifikatsseriennummer.equals(other.getZertifikatsseriennummer()))) &&
            ((this.ausfall==null && other.getAusfall()==null) || 
             (this.ausfall!=null &&
              this.ausfall.equals(other.getAusfall()))) &&
            ((this.ausserbetriebnahme==null && other.getAusserbetriebnahme()==null) || 
             (this.ausserbetriebnahme!=null &&
              this.ausserbetriebnahme.equals(other.getAusserbetriebnahme())));
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
        if (getZertifikatsseriennummer() != null) {
            _hashCode += getZertifikatsseriennummer().hashCode();
        }
        if (getAusfall() != null) {
            _hashCode += getAusfall().hashCode();
        }
        if (getAusserbetriebnahme() != null) {
            _hashCode += getAusserbetriebnahme().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Ausfall_se.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">ausfall_se"));
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
        elemField.setFieldName("zertifikatsseriennummer");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "zertifikatsseriennummer"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">zertifikatsseriennummer"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ausfall");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "ausfall"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">ausfall"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ausserbetriebnahme");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "ausserbetriebnahme"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">ausserbetriebnahme"));
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
