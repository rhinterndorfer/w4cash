/**
 * VerificationResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package at.gv.bmf.finanzonline.rkdb;

public class VerificationResult  implements java.io.Serializable {
    private java.lang.String verificationId;

    private java.math.BigInteger version;

    private java.lang.String verificationName;

    private java.lang.String verificationTextualDescription;

    private at.gv.bmf.finanzonline.rkdb.VerificationState verificationState;

    private java.lang.String verificationResultDetailedMessage;

    private at.gv.bmf.finanzonline.rkdb.Input input;

    private at.gv.bmf.finanzonline.rkdb.Output output;

    private java.util.Calendar verificationTimestamp;

    private at.gv.bmf.finanzonline.rkdb.VerificationResult[] verificationResultList;

    public VerificationResult() {
    }

    public VerificationResult(
           java.lang.String verificationId,
           java.math.BigInteger version,
           java.lang.String verificationName,
           java.lang.String verificationTextualDescription,
           at.gv.bmf.finanzonline.rkdb.VerificationState verificationState,
           java.lang.String verificationResultDetailedMessage,
           at.gv.bmf.finanzonline.rkdb.Input input,
           at.gv.bmf.finanzonline.rkdb.Output output,
           java.util.Calendar verificationTimestamp,
           at.gv.bmf.finanzonline.rkdb.VerificationResult[] verificationResultList) {
           this.verificationId = verificationId;
           this.version = version;
           this.verificationName = verificationName;
           this.verificationTextualDescription = verificationTextualDescription;
           this.verificationState = verificationState;
           this.verificationResultDetailedMessage = verificationResultDetailedMessage;
           this.input = input;
           this.output = output;
           this.verificationTimestamp = verificationTimestamp;
           this.verificationResultList = verificationResultList;
    }


    /**
     * Gets the verificationId value for this VerificationResult.
     * 
     * @return verificationId
     */
    public java.lang.String getVerificationId() {
        return verificationId;
    }


    /**
     * Sets the verificationId value for this VerificationResult.
     * 
     * @param verificationId
     */
    public void setVerificationId(java.lang.String verificationId) {
        this.verificationId = verificationId;
    }


    /**
     * Gets the version value for this VerificationResult.
     * 
     * @return version
     */
    public java.math.BigInteger getVersion() {
        return version;
    }


    /**
     * Sets the version value for this VerificationResult.
     * 
     * @param version
     */
    public void setVersion(java.math.BigInteger version) {
        this.version = version;
    }


    /**
     * Gets the verificationName value for this VerificationResult.
     * 
     * @return verificationName
     */
    public java.lang.String getVerificationName() {
        return verificationName;
    }


    /**
     * Sets the verificationName value for this VerificationResult.
     * 
     * @param verificationName
     */
    public void setVerificationName(java.lang.String verificationName) {
        this.verificationName = verificationName;
    }


    /**
     * Gets the verificationTextualDescription value for this VerificationResult.
     * 
     * @return verificationTextualDescription
     */
    public java.lang.String getVerificationTextualDescription() {
        return verificationTextualDescription;
    }


    /**
     * Sets the verificationTextualDescription value for this VerificationResult.
     * 
     * @param verificationTextualDescription
     */
    public void setVerificationTextualDescription(java.lang.String verificationTextualDescription) {
        this.verificationTextualDescription = verificationTextualDescription;
    }


    /**
     * Gets the verificationState value for this VerificationResult.
     * 
     * @return verificationState
     */
    public at.gv.bmf.finanzonline.rkdb.VerificationState getVerificationState() {
        return verificationState;
    }


    /**
     * Sets the verificationState value for this VerificationResult.
     * 
     * @param verificationState
     */
    public void setVerificationState(at.gv.bmf.finanzonline.rkdb.VerificationState verificationState) {
        this.verificationState = verificationState;
    }


    /**
     * Gets the verificationResultDetailedMessage value for this VerificationResult.
     * 
     * @return verificationResultDetailedMessage
     */
    public java.lang.String getVerificationResultDetailedMessage() {
        return verificationResultDetailedMessage;
    }


    /**
     * Sets the verificationResultDetailedMessage value for this VerificationResult.
     * 
     * @param verificationResultDetailedMessage
     */
    public void setVerificationResultDetailedMessage(java.lang.String verificationResultDetailedMessage) {
        this.verificationResultDetailedMessage = verificationResultDetailedMessage;
    }


    /**
     * Gets the input value for this VerificationResult.
     * 
     * @return input
     */
    public at.gv.bmf.finanzonline.rkdb.Input getInput() {
        return input;
    }


    /**
     * Sets the input value for this VerificationResult.
     * 
     * @param input
     */
    public void setInput(at.gv.bmf.finanzonline.rkdb.Input input) {
        this.input = input;
    }


    /**
     * Gets the output value for this VerificationResult.
     * 
     * @return output
     */
    public at.gv.bmf.finanzonline.rkdb.Output getOutput() {
        return output;
    }


    /**
     * Sets the output value for this VerificationResult.
     * 
     * @param output
     */
    public void setOutput(at.gv.bmf.finanzonline.rkdb.Output output) {
        this.output = output;
    }


    /**
     * Gets the verificationTimestamp value for this VerificationResult.
     * 
     * @return verificationTimestamp
     */
    public java.util.Calendar getVerificationTimestamp() {
        return verificationTimestamp;
    }


    /**
     * Sets the verificationTimestamp value for this VerificationResult.
     * 
     * @param verificationTimestamp
     */
    public void setVerificationTimestamp(java.util.Calendar verificationTimestamp) {
        this.verificationTimestamp = verificationTimestamp;
    }


    /**
     * Gets the verificationResultList value for this VerificationResult.
     * 
     * @return verificationResultList
     */
    public at.gv.bmf.finanzonline.rkdb.VerificationResult[] getVerificationResultList() {
        return verificationResultList;
    }


    /**
     * Sets the verificationResultList value for this VerificationResult.
     * 
     * @param verificationResultList
     */
    public void setVerificationResultList(at.gv.bmf.finanzonline.rkdb.VerificationResult[] verificationResultList) {
        this.verificationResultList = verificationResultList;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof VerificationResult)) return false;
        VerificationResult other = (VerificationResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.verificationId==null && other.getVerificationId()==null) || 
             (this.verificationId!=null &&
              this.verificationId.equals(other.getVerificationId()))) &&
            ((this.version==null && other.getVersion()==null) || 
             (this.version!=null &&
              this.version.equals(other.getVersion()))) &&
            ((this.verificationName==null && other.getVerificationName()==null) || 
             (this.verificationName!=null &&
              this.verificationName.equals(other.getVerificationName()))) &&
            ((this.verificationTextualDescription==null && other.getVerificationTextualDescription()==null) || 
             (this.verificationTextualDescription!=null &&
              this.verificationTextualDescription.equals(other.getVerificationTextualDescription()))) &&
            ((this.verificationState==null && other.getVerificationState()==null) || 
             (this.verificationState!=null &&
              this.verificationState.equals(other.getVerificationState()))) &&
            ((this.verificationResultDetailedMessage==null && other.getVerificationResultDetailedMessage()==null) || 
             (this.verificationResultDetailedMessage!=null &&
              this.verificationResultDetailedMessage.equals(other.getVerificationResultDetailedMessage()))) &&
            ((this.input==null && other.getInput()==null) || 
             (this.input!=null &&
              this.input.equals(other.getInput()))) &&
            ((this.output==null && other.getOutput()==null) || 
             (this.output!=null &&
              this.output.equals(other.getOutput()))) &&
            ((this.verificationTimestamp==null && other.getVerificationTimestamp()==null) || 
             (this.verificationTimestamp!=null &&
              this.verificationTimestamp.equals(other.getVerificationTimestamp()))) &&
            ((this.verificationResultList==null && other.getVerificationResultList()==null) || 
             (this.verificationResultList!=null &&
              java.util.Arrays.equals(this.verificationResultList, other.getVerificationResultList())));
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
        if (getVerificationId() != null) {
            _hashCode += getVerificationId().hashCode();
        }
        if (getVersion() != null) {
            _hashCode += getVersion().hashCode();
        }
        if (getVerificationName() != null) {
            _hashCode += getVerificationName().hashCode();
        }
        if (getVerificationTextualDescription() != null) {
            _hashCode += getVerificationTextualDescription().hashCode();
        }
        if (getVerificationState() != null) {
            _hashCode += getVerificationState().hashCode();
        }
        if (getVerificationResultDetailedMessage() != null) {
            _hashCode += getVerificationResultDetailedMessage().hashCode();
        }
        if (getInput() != null) {
            _hashCode += getInput().hashCode();
        }
        if (getOutput() != null) {
            _hashCode += getOutput().hashCode();
        }
        if (getVerificationTimestamp() != null) {
            _hashCode += getVerificationTimestamp().hashCode();
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
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(VerificationResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">verificationResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("verificationId");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "verificationId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("version");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "version"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "integer"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("verificationName");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "verificationName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("verificationTextualDescription");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "verificationTextualDescription"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("verificationState");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "verificationState"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">verificationState"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("verificationResultDetailedMessage");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "verificationResultDetailedMessage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("input");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "input"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">input"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("output");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "output"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">output"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("verificationTimestamp");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "verificationTimestamp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("verificationResultList");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "verificationResultList"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">verificationResultList"));
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
