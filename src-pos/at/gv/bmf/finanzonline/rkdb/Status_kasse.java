/**
 * Status_kasse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package at.gv.bmf.finanzonline.rkdb;

public class Status_kasse  implements java.io.Serializable {
    private java.lang.String fastnr;

    private org.apache.axis.types.PositiveInteger paket_nr;

    private java.util.Calendar ts_erstellung;

    private org.apache.axis.types.PositiveInteger satznr;

    private java.lang.String kassenidentifikationsnummer;

    public Status_kasse() {
    }

    public Status_kasse(
           java.lang.String fastnr,
           org.apache.axis.types.PositiveInteger paket_nr,
           java.util.Calendar ts_erstellung,
           org.apache.axis.types.PositiveInteger satznr,
           java.lang.String kassenidentifikationsnummer) {
           this.fastnr = fastnr;
           this.paket_nr = paket_nr;
           this.ts_erstellung = ts_erstellung;
           this.satznr = satznr;
           this.kassenidentifikationsnummer = kassenidentifikationsnummer;
    }


    /**
     * Gets the fastnr value for this Status_kasse.
     * 
     * @return fastnr
     */
    public java.lang.String getFastnr() {
        return fastnr;
    }


    /**
     * Sets the fastnr value for this Status_kasse.
     * 
     * @param fastnr
     */
    public void setFastnr(java.lang.String fastnr) {
        this.fastnr = fastnr;
    }


    /**
     * Gets the paket_nr value for this Status_kasse.
     * 
     * @return paket_nr
     */
    public org.apache.axis.types.PositiveInteger getPaket_nr() {
        return paket_nr;
    }


    /**
     * Sets the paket_nr value for this Status_kasse.
     * 
     * @param paket_nr
     */
    public void setPaket_nr(org.apache.axis.types.PositiveInteger paket_nr) {
        this.paket_nr = paket_nr;
    }


    /**
     * Gets the ts_erstellung value for this Status_kasse.
     * 
     * @return ts_erstellung
     */
    public java.util.Calendar getTs_erstellung() {
        return ts_erstellung;
    }


    /**
     * Sets the ts_erstellung value for this Status_kasse.
     * 
     * @param ts_erstellung
     */
    public void setTs_erstellung(java.util.Calendar ts_erstellung) {
        this.ts_erstellung = ts_erstellung;
    }


    /**
     * Gets the satznr value for this Status_kasse.
     * 
     * @return satznr
     */
    public org.apache.axis.types.PositiveInteger getSatznr() {
        return satznr;
    }


    /**
     * Sets the satznr value for this Status_kasse.
     * 
     * @param satznr
     */
    public void setSatznr(org.apache.axis.types.PositiveInteger satznr) {
        this.satznr = satznr;
    }


    /**
     * Gets the kassenidentifikationsnummer value for this Status_kasse.
     * 
     * @return kassenidentifikationsnummer
     */
    public java.lang.String getKassenidentifikationsnummer() {
        return kassenidentifikationsnummer;
    }


    /**
     * Sets the kassenidentifikationsnummer value for this Status_kasse.
     * 
     * @param kassenidentifikationsnummer
     */
    public void setKassenidentifikationsnummer(java.lang.String kassenidentifikationsnummer) {
        this.kassenidentifikationsnummer = kassenidentifikationsnummer;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Status_kasse)) return false;
        Status_kasse other = (Status_kasse) obj;
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
            ((this.kassenidentifikationsnummer==null && other.getKassenidentifikationsnummer()==null) || 
             (this.kassenidentifikationsnummer!=null &&
              this.kassenidentifikationsnummer.equals(other.getKassenidentifikationsnummer())));
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
        if (getKassenidentifikationsnummer() != null) {
            _hashCode += getKassenidentifikationsnummer().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Status_kasse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">status_kasse"));
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
        elemField.setFieldName("kassenidentifikationsnummer");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "kassenidentifikationsnummer"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">kassenidentifikationsnummer"));
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
