/**
 * Ausfall_ggs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package at.gv.bmf.finanzonline.rkdb;

public class Ausfall_ggs  implements java.io.Serializable {
    private org.apache.axis.types.PositiveInteger satznr;

    private java.lang.String kundeninfo;

    private at.gv.bmf.finanzonline.rkdb.Art_ob art_ob;

    private java.lang.String ob;

    private java.lang.String zusatz_ob;

    private at.gv.bmf.finanzonline.rkdb.Ausfall ausfall;

    private at.gv.bmf.finanzonline.rkdb.Ausserbetriebnahme ausserbetriebnahme;

    public Ausfall_ggs() {
    }

    public Ausfall_ggs(
           org.apache.axis.types.PositiveInteger satznr,
           java.lang.String kundeninfo,
           at.gv.bmf.finanzonline.rkdb.Art_ob art_ob,
           java.lang.String ob,
           java.lang.String zusatz_ob,
           at.gv.bmf.finanzonline.rkdb.Ausfall ausfall,
           at.gv.bmf.finanzonline.rkdb.Ausserbetriebnahme ausserbetriebnahme) {
           this.satznr = satznr;
           this.kundeninfo = kundeninfo;
           this.art_ob = art_ob;
           this.ob = ob;
           this.zusatz_ob = zusatz_ob;
           this.ausfall = ausfall;
           this.ausserbetriebnahme = ausserbetriebnahme;
    }


    /**
     * Gets the satznr value for this Ausfall_ggs.
     * 
     * @return satznr
     */
    public org.apache.axis.types.PositiveInteger getSatznr() {
        return satznr;
    }


    /**
     * Sets the satznr value for this Ausfall_ggs.
     * 
     * @param satznr
     */
    public void setSatznr(org.apache.axis.types.PositiveInteger satznr) {
        this.satznr = satznr;
    }


    /**
     * Gets the kundeninfo value for this Ausfall_ggs.
     * 
     * @return kundeninfo
     */
    public java.lang.String getKundeninfo() {
        return kundeninfo;
    }


    /**
     * Sets the kundeninfo value for this Ausfall_ggs.
     * 
     * @param kundeninfo
     */
    public void setKundeninfo(java.lang.String kundeninfo) {
        this.kundeninfo = kundeninfo;
    }


    /**
     * Gets the art_ob value for this Ausfall_ggs.
     * 
     * @return art_ob
     */
    public at.gv.bmf.finanzonline.rkdb.Art_ob getArt_ob() {
        return art_ob;
    }


    /**
     * Sets the art_ob value for this Ausfall_ggs.
     * 
     * @param art_ob
     */
    public void setArt_ob(at.gv.bmf.finanzonline.rkdb.Art_ob art_ob) {
        this.art_ob = art_ob;
    }


    /**
     * Gets the ob value for this Ausfall_ggs.
     * 
     * @return ob
     */
    public java.lang.String getOb() {
        return ob;
    }


    /**
     * Sets the ob value for this Ausfall_ggs.
     * 
     * @param ob
     */
    public void setOb(java.lang.String ob) {
        this.ob = ob;
    }


    /**
     * Gets the zusatz_ob value for this Ausfall_ggs.
     * 
     * @return zusatz_ob
     */
    public java.lang.String getZusatz_ob() {
        return zusatz_ob;
    }


    /**
     * Sets the zusatz_ob value for this Ausfall_ggs.
     * 
     * @param zusatz_ob
     */
    public void setZusatz_ob(java.lang.String zusatz_ob) {
        this.zusatz_ob = zusatz_ob;
    }


    /**
     * Gets the ausfall value for this Ausfall_ggs.
     * 
     * @return ausfall
     */
    public at.gv.bmf.finanzonline.rkdb.Ausfall getAusfall() {
        return ausfall;
    }


    /**
     * Sets the ausfall value for this Ausfall_ggs.
     * 
     * @param ausfall
     */
    public void setAusfall(at.gv.bmf.finanzonline.rkdb.Ausfall ausfall) {
        this.ausfall = ausfall;
    }


    /**
     * Gets the ausserbetriebnahme value for this Ausfall_ggs.
     * 
     * @return ausserbetriebnahme
     */
    public at.gv.bmf.finanzonline.rkdb.Ausserbetriebnahme getAusserbetriebnahme() {
        return ausserbetriebnahme;
    }


    /**
     * Sets the ausserbetriebnahme value for this Ausfall_ggs.
     * 
     * @param ausserbetriebnahme
     */
    public void setAusserbetriebnahme(at.gv.bmf.finanzonline.rkdb.Ausserbetriebnahme ausserbetriebnahme) {
        this.ausserbetriebnahme = ausserbetriebnahme;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Ausfall_ggs)) return false;
        Ausfall_ggs other = (Ausfall_ggs) obj;
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
        if (getArt_ob() != null) {
            _hashCode += getArt_ob().hashCode();
        }
        if (getOb() != null) {
            _hashCode += getOb().hashCode();
        }
        if (getZusatz_ob() != null) {
            _hashCode += getZusatz_ob().hashCode();
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
        new org.apache.axis.description.TypeDesc(Ausfall_ggs.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">ausfall_ggs"));
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
