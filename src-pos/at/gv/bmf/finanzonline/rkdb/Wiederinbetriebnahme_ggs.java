/**
 * Wiederinbetriebnahme_ggs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package at.gv.bmf.finanzonline.rkdb;

public class Wiederinbetriebnahme_ggs  implements java.io.Serializable {
    private org.apache.axis.types.PositiveInteger satznr;

    private java.lang.String kundeninfo;

    private at.gv.bmf.finanzonline.rkdb.Art_ob art_ob;

    private java.lang.String ob;

    private java.lang.String zusatz_ob;

    private java.util.Calendar ende_ausfall;

    public Wiederinbetriebnahme_ggs() {
    }

    public Wiederinbetriebnahme_ggs(
           org.apache.axis.types.PositiveInteger satznr,
           java.lang.String kundeninfo,
           at.gv.bmf.finanzonline.rkdb.Art_ob art_ob,
           java.lang.String ob,
           java.lang.String zusatz_ob,
           java.util.Calendar ende_ausfall) {
           this.satznr = satznr;
           this.kundeninfo = kundeninfo;
           this.art_ob = art_ob;
           this.ob = ob;
           this.zusatz_ob = zusatz_ob;
           this.ende_ausfall = ende_ausfall;
    }


    /**
     * Gets the satznr value for this Wiederinbetriebnahme_ggs.
     * 
     * @return satznr
     */
    public org.apache.axis.types.PositiveInteger getSatznr() {
        return satznr;
    }


    /**
     * Sets the satznr value for this Wiederinbetriebnahme_ggs.
     * 
     * @param satznr
     */
    public void setSatznr(org.apache.axis.types.PositiveInteger satznr) {
        this.satznr = satznr;
    }


    /**
     * Gets the kundeninfo value for this Wiederinbetriebnahme_ggs.
     * 
     * @return kundeninfo
     */
    public java.lang.String getKundeninfo() {
        return kundeninfo;
    }


    /**
     * Sets the kundeninfo value for this Wiederinbetriebnahme_ggs.
     * 
     * @param kundeninfo
     */
    public void setKundeninfo(java.lang.String kundeninfo) {
        this.kundeninfo = kundeninfo;
    }


    /**
     * Gets the art_ob value for this Wiederinbetriebnahme_ggs.
     * 
     * @return art_ob
     */
    public at.gv.bmf.finanzonline.rkdb.Art_ob getArt_ob() {
        return art_ob;
    }


    /**
     * Sets the art_ob value for this Wiederinbetriebnahme_ggs.
     * 
     * @param art_ob
     */
    public void setArt_ob(at.gv.bmf.finanzonline.rkdb.Art_ob art_ob) {
        this.art_ob = art_ob;
    }


    /**
     * Gets the ob value for this Wiederinbetriebnahme_ggs.
     * 
     * @return ob
     */
    public java.lang.String getOb() {
        return ob;
    }


    /**
     * Sets the ob value for this Wiederinbetriebnahme_ggs.
     * 
     * @param ob
     */
    public void setOb(java.lang.String ob) {
        this.ob = ob;
    }


    /**
     * Gets the zusatz_ob value for this Wiederinbetriebnahme_ggs.
     * 
     * @return zusatz_ob
     */
    public java.lang.String getZusatz_ob() {
        return zusatz_ob;
    }


    /**
     * Sets the zusatz_ob value for this Wiederinbetriebnahme_ggs.
     * 
     * @param zusatz_ob
     */
    public void setZusatz_ob(java.lang.String zusatz_ob) {
        this.zusatz_ob = zusatz_ob;
    }


    /**
     * Gets the ende_ausfall value for this Wiederinbetriebnahme_ggs.
     * 
     * @return ende_ausfall
     */
    public java.util.Calendar getEnde_ausfall() {
        return ende_ausfall;
    }


    /**
     * Sets the ende_ausfall value for this Wiederinbetriebnahme_ggs.
     * 
     * @param ende_ausfall
     */
    public void setEnde_ausfall(java.util.Calendar ende_ausfall) {
        this.ende_ausfall = ende_ausfall;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Wiederinbetriebnahme_ggs)) return false;
        Wiederinbetriebnahme_ggs other = (Wiederinbetriebnahme_ggs) obj;
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
        if (getArt_ob() != null) {
            _hashCode += getArt_ob().hashCode();
        }
        if (getOb() != null) {
            _hashCode += getOb().hashCode();
        }
        if (getZusatz_ob() != null) {
            _hashCode += getZusatz_ob().hashCode();
        }
        if (getEnde_ausfall() != null) {
            _hashCode += getEnde_ausfall().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Wiederinbetriebnahme_ggs.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">wiederinbetriebnahme_ggs"));
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
