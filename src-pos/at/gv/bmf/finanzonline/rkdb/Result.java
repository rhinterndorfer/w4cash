/**
 * Result.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package at.gv.bmf.finanzonline.rkdb;

public class Result  implements java.io.Serializable {
    private org.apache.axis.types.PositiveInteger satznr;

    private java.lang.String kundeninfo;

    private at.gv.bmf.finanzonline.rkdb.RkdbMessage[] rkdbMessage;

    private at.gv.bmf.finanzonline.rkdb.VerificationResult[] verificationResultList;

    private at.gv.bmf.finanzonline.rkdb.Abfrage_ergebnis abfrage_ergebnis;

    public Result() {
    }

    public Result(
           org.apache.axis.types.PositiveInteger satznr,
           java.lang.String kundeninfo,
           at.gv.bmf.finanzonline.rkdb.RkdbMessage[] rkdbMessage,
           at.gv.bmf.finanzonline.rkdb.VerificationResult[] verificationResultList,
           at.gv.bmf.finanzonline.rkdb.Abfrage_ergebnis abfrage_ergebnis) {
           this.satznr = satznr;
           this.kundeninfo = kundeninfo;
           this.rkdbMessage = rkdbMessage;
           this.verificationResultList = verificationResultList;
           this.abfrage_ergebnis = abfrage_ergebnis;
    }


    /**
     * Gets the satznr value for this Result.
     * 
     * @return satznr
     */
    public org.apache.axis.types.PositiveInteger getSatznr() {
        return satznr;
    }


    /**
     * Sets the satznr value for this Result.
     * 
     * @param satznr
     */
    public void setSatznr(org.apache.axis.types.PositiveInteger satznr) {
        this.satznr = satznr;
    }


    /**
     * Gets the kundeninfo value for this Result.
     * 
     * @return kundeninfo
     */
    public java.lang.String getKundeninfo() {
        return kundeninfo;
    }


    /**
     * Sets the kundeninfo value for this Result.
     * 
     * @param kundeninfo
     */
    public void setKundeninfo(java.lang.String kundeninfo) {
        this.kundeninfo = kundeninfo;
    }


    /**
     * Gets the rkdbMessage value for this Result.
     * 
     * @return rkdbMessage
     */
    public at.gv.bmf.finanzonline.rkdb.RkdbMessage[] getRkdbMessage() {
        return rkdbMessage;
    }


    /**
     * Sets the rkdbMessage value for this Result.
     * 
     * @param rkdbMessage
     */
    public void setRkdbMessage(at.gv.bmf.finanzonline.rkdb.RkdbMessage[] rkdbMessage) {
        this.rkdbMessage = rkdbMessage;
    }

    public at.gv.bmf.finanzonline.rkdb.RkdbMessage getRkdbMessage(int i) {
        return this.rkdbMessage[i];
    }

    public void setRkdbMessage(int i, at.gv.bmf.finanzonline.rkdb.RkdbMessage _value) {
        this.rkdbMessage[i] = _value;
    }


    /**
     * Gets the verificationResultList value for this Result.
     * 
     * @return verificationResultList
     */
    public at.gv.bmf.finanzonline.rkdb.VerificationResult[] getVerificationResultList() {
        return verificationResultList;
    }


    /**
     * Sets the verificationResultList value for this Result.
     * 
     * @param verificationResultList
     */
    public void setVerificationResultList(at.gv.bmf.finanzonline.rkdb.VerificationResult[] verificationResultList) {
        this.verificationResultList = verificationResultList;
    }


    /**
     * Gets the abfrage_ergebnis value for this Result.
     * 
     * @return abfrage_ergebnis
     */
    public at.gv.bmf.finanzonline.rkdb.Abfrage_ergebnis getAbfrage_ergebnis() {
        return abfrage_ergebnis;
    }


    /**
     * Sets the abfrage_ergebnis value for this Result.
     * 
     * @param abfrage_ergebnis
     */
    public void setAbfrage_ergebnis(at.gv.bmf.finanzonline.rkdb.Abfrage_ergebnis abfrage_ergebnis) {
        this.abfrage_ergebnis = abfrage_ergebnis;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Result)) return false;
        Result other = (Result) obj;
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
            ((this.rkdbMessage==null && other.getRkdbMessage()==null) || 
             (this.rkdbMessage!=null &&
              java.util.Arrays.equals(this.rkdbMessage, other.getRkdbMessage()))) &&
            ((this.verificationResultList==null && other.getVerificationResultList()==null) || 
             (this.verificationResultList!=null &&
              java.util.Arrays.equals(this.verificationResultList, other.getVerificationResultList()))) &&
            ((this.abfrage_ergebnis==null && other.getAbfrage_ergebnis()==null) || 
             (this.abfrage_ergebnis!=null &&
              this.abfrage_ergebnis.equals(other.getAbfrage_ergebnis())));
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
        if (getRkdbMessage() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRkdbMessage());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRkdbMessage(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getVerificationResultList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getVerificationResultList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getVerificationResultList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getAbfrage_ergebnis() != null) {
            _hashCode += getAbfrage_ergebnis().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Result.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">result"));
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
        elemField.setFieldName("rkdbMessage");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "rkdbMessage"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "rkdbMessage"));
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("verificationResultList");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "verificationResultList"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">verificationResultList"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("abfrage_ergebnis");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "abfrage_ergebnis"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">abfrage_ergebnis"));
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
