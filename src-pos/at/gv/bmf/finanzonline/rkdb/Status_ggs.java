/**
 * Status_ggs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package at.gv.bmf.finanzonline.rkdb;

public class Status_ggs  implements java.io.Serializable {
    private java.lang.String fastnr;

    private org.apache.axis.types.PositiveInteger paket_nr;

    private java.util.Calendar ts_erstellung;

    private org.apache.axis.types.PositiveInteger satznr;

    private at.gv.bmf.finanzonline.rkdb.Art_ob art_ob;

    private java.lang.String ob;

    private java.lang.String zusatz_ob;

    public Status_ggs() {
    }

    public Status_ggs(
           java.lang.String fastnr,
           org.apache.axis.types.PositiveInteger paket_nr,
           java.util.Calendar ts_erstellung,
           org.apache.axis.types.PositiveInteger satznr,
           at.gv.bmf.finanzonline.rkdb.Art_ob art_ob,
           java.lang.String ob,
           java.lang.String zusatz_ob) {
           this.fastnr = fastnr;
           this.paket_nr = paket_nr;
           this.ts_erstellung = ts_erstellung;
           this.satznr = satznr;
           this.art_ob = art_ob;
           this.ob = ob;
           this.zusatz_ob = zusatz_ob;
    }


    /**
     * Gets the fastnr value for this Status_ggs.
     * 
     * @return fastnr
     */
    public java.lang.String getFastnr() {
        return fastnr;
    }


    /**
     * Sets the fastnr value for this Status_ggs.
     * 
     * @param fastnr
     */
    public void setFastnr(java.lang.String fastnr) {
        this.fastnr = fastnr;
    }


    /**
     * Gets the paket_nr value for this Status_ggs.
     * 
     * @return paket_nr
     */
    public org.apache.axis.types.PositiveInteger getPaket_nr() {
        return paket_nr;
    }


    /**
     * Sets the paket_nr value for this Status_ggs.
     * 
     * @param paket_nr
     */
    public void setPaket_nr(org.apache.axis.types.PositiveInteger paket_nr) {
        this.paket_nr = paket_nr;
    }


    /**
     * Gets the ts_erstellung value for this Status_ggs.
     * 
     * @return ts_erstellung
     */
    public java.util.Calendar getTs_erstellung() {
        return ts_erstellung;
    }


    /**
     * Sets the ts_erstellung value for this Status_ggs.
     * 
     * @param ts_erstellung
     */
    public void setTs_erstellung(java.util.Calendar ts_erstellung) {
        this.ts_erstellung = ts_erstellung;
    }


    /**
     * Gets the satznr value for this Status_ggs.
     * 
     * @return satznr
     */
    public org.apache.axis.types.PositiveInteger getSatznr() {
        return satznr;
    }


    /**
     * Sets the satznr value for this Status_ggs.
     * 
     * @param satznr
     */
    public void setSatznr(org.apache.axis.types.PositiveInteger satznr) {
        this.satznr = satznr;
    }


    /**
     * Gets the art_ob value for this Status_ggs.
     * 
     * @return art_ob
     */
    public at.gv.bmf.finanzonline.rkdb.Art_ob getArt_ob() {
        return art_ob;
    }


    /**
     * Sets the art_ob value for this Status_ggs.
     * 
     * @param art_ob
     */
    public void setArt_ob(at.gv.bmf.finanzonline.rkdb.Art_ob art_ob) {
        this.art_ob = art_ob;
    }


    /**
     * Gets the ob value for this Status_ggs.
     * 
     * @return ob
     */
    public java.lang.String getOb() {
        return ob;
    }


    /**
     * Sets the ob value for this Status_ggs.
     * 
     * @param ob
     */
    public void setOb(java.lang.String ob) {
        this.ob = ob;
    }


    /**
     * Gets the zusatz_ob value for this Status_ggs.
     * 
     * @return zusatz_ob
     */
    public java.lang.String getZusatz_ob() {
        return zusatz_ob;
    }


    /**
     * Sets the zusatz_ob value for this Status_ggs.
     * 
     * @param zusatz_ob
     */
    public void setZusatz_ob(java.lang.String zusatz_ob) {
        this.zusatz_ob = zusatz_ob;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Status_ggs)) return false;
        Status_ggs other = (Status_ggs) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.fastnr==null && other.getFastnr()==null) || 
             (this.fastnr!=null &&
              this.fastnr.equals(other.getFastnr()))) &&
            ((this.paket_nr==null && other.getPaket_nr()==null) || 
             (this.paket_nr!=null &&
              this.paket_nr.equals(other.getPaket_nr()))) &&
            ((this.ts_erstellung==null && other.getTs_erstellung()==null) || 
             (this.ts_erstellung!=null &&
              this.ts_erstellung.equals(other.getTs_erstellung()))) &&
            ((this.satznr==null && other.getSatznr()==null) || 
             (this.satznr!=null &&
              this.satznr.equals(other.getSatznr()))) &&
            ((this.art_ob==null && other.getArt_ob()==null) || 
             (this.art_ob!=null &&
              this.art_ob.equals(other.getArt_ob()))) &&
            ((this.ob==null && other.getOb()==null) || 
             (this.ob!=null &&
              this.ob.equals(other.getOb()))) &&
            ((this.zusatz_ob==null && other.getZusatz_ob()==null) || 
             (this.zusatz_ob!=null &&
              this.zusatz_ob.equals(other.getZusatz_ob())));
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
        if (getFastnr() != null) {
            _hashCode += getFastnr().hashCode();
        }
        if (getPaket_nr() != null) {
            _hashCode += getPaket_nr().hashCode();
        }
        if (getTs_erstellung() != null) {
            _hashCode += getTs_erstellung().hashCode();
        }
        if (getSatznr() != null) {
            _hashCode += getSatznr().hashCode();
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
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Status_ggs.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">status_ggs"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fastnr");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "fastnr"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">fastnr"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("paket_nr");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "paket_nr"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">paket_nr"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ts_erstellung");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "ts_erstellung"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">ts_erstellung"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("satznr");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "satznr"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">satznr"));
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
