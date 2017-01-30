/**
 * RkdbResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package at.gv.bmf.finanzonline.rkdb;

public class RkdbResponse  implements java.io.Serializable {
    private java.lang.String fastnr;

    private org.apache.axis.types.PositiveInteger paket_nr;

    private at.gv.bmf.finanzonline.rkdb.Art_uebermittlung art_uebermittlung;

    private java.util.Calendar ts_erstellung;

    private java.lang.String info;

    private at.gv.bmf.finanzonline.rkdb.Result[] result;

    public RkdbResponse() {
    }

    public RkdbResponse(
           java.lang.String fastnr,
           org.apache.axis.types.PositiveInteger paket_nr,
           at.gv.bmf.finanzonline.rkdb.Art_uebermittlung art_uebermittlung,
           java.util.Calendar ts_erstellung,
           java.lang.String info,
           at.gv.bmf.finanzonline.rkdb.Result[] result) {
           this.fastnr = fastnr;
           this.paket_nr = paket_nr;
           this.art_uebermittlung = art_uebermittlung;
           this.ts_erstellung = ts_erstellung;
           this.info = info;
           this.result = result;
    }


    /**
     * Gets the fastnr value for this RkdbResponse.
     * 
     * @return fastnr
     */
    public java.lang.String getFastnr() {
        return fastnr;
    }


    /**
     * Sets the fastnr value for this RkdbResponse.
     * 
     * @param fastnr
     */
    public void setFastnr(java.lang.String fastnr) {
        this.fastnr = fastnr;
    }


    /**
     * Gets the paket_nr value for this RkdbResponse.
     * 
     * @return paket_nr
     */
    public org.apache.axis.types.PositiveInteger getPaket_nr() {
        return paket_nr;
    }


    /**
     * Sets the paket_nr value for this RkdbResponse.
     * 
     * @param paket_nr
     */
    public void setPaket_nr(org.apache.axis.types.PositiveInteger paket_nr) {
        this.paket_nr = paket_nr;
    }


    /**
     * Gets the art_uebermittlung value for this RkdbResponse.
     * 
     * @return art_uebermittlung
     */
    public at.gv.bmf.finanzonline.rkdb.Art_uebermittlung getArt_uebermittlung() {
        return art_uebermittlung;
    }


    /**
     * Sets the art_uebermittlung value for this RkdbResponse.
     * 
     * @param art_uebermittlung
     */
    public void setArt_uebermittlung(at.gv.bmf.finanzonline.rkdb.Art_uebermittlung art_uebermittlung) {
        this.art_uebermittlung = art_uebermittlung;
    }


    /**
     * Gets the ts_erstellung value for this RkdbResponse.
     * 
     * @return ts_erstellung
     */
    public java.util.Calendar getTs_erstellung() {
        return ts_erstellung;
    }


    /**
     * Sets the ts_erstellung value for this RkdbResponse.
     * 
     * @param ts_erstellung
     */
    public void setTs_erstellung(java.util.Calendar ts_erstellung) {
        this.ts_erstellung = ts_erstellung;
    }


    /**
     * Gets the info value for this RkdbResponse.
     * 
     * @return info
     */
    public java.lang.String getInfo() {
        return info;
    }


    /**
     * Sets the info value for this RkdbResponse.
     * 
     * @param info
     */
    public void setInfo(java.lang.String info) {
        this.info = info;
    }


    /**
     * Gets the result value for this RkdbResponse.
     * 
     * @return result
     */
    public at.gv.bmf.finanzonline.rkdb.Result[] getResult() {
        return result;
    }


    /**
     * Sets the result value for this RkdbResponse.
     * 
     * @param result
     */
    public void setResult(at.gv.bmf.finanzonline.rkdb.Result[] result) {
        this.result = result;
    }

    public at.gv.bmf.finanzonline.rkdb.Result getResult(int i) {
        return this.result[i];
    }

    public void setResult(int i, at.gv.bmf.finanzonline.rkdb.Result _value) {
        this.result[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RkdbResponse)) return false;
        RkdbResponse other = (RkdbResponse) obj;
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
            ((this.art_uebermittlung==null && other.getArt_uebermittlung()==null) || 
             (this.art_uebermittlung!=null &&
              this.art_uebermittlung.equals(other.getArt_uebermittlung()))) &&
            ((this.ts_erstellung==null && other.getTs_erstellung()==null) || 
             (this.ts_erstellung!=null &&
              this.ts_erstellung.equals(other.getTs_erstellung()))) &&
            ((this.info==null && other.getInfo()==null) || 
             (this.info!=null &&
              this.info.equals(other.getInfo()))) &&
            ((this.result==null && other.getResult()==null) || 
             (this.result!=null &&
              java.util.Arrays.equals(this.result, other.getResult())));
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
        if (getArt_uebermittlung() != null) {
            _hashCode += getArt_uebermittlung().hashCode();
        }
        if (getTs_erstellung() != null) {
            _hashCode += getTs_erstellung().hashCode();
        }
        if (getInfo() != null) {
            _hashCode += getInfo().hashCode();
        }
        if (getResult() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getResult());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getResult(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RkdbResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">rkdbResponse"));
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
        elemField.setFieldName("art_uebermittlung");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "art_uebermittlung"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">art_uebermittlung"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ts_erstellung");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "ts_erstellung"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">ts_erstellung"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("info");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "info"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("result");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "result"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "result"));
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
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
