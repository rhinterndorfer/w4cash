/**
 * RkdbRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package at.gv.bmf.finanzonline.rkdb;

public class RkdbRequest  implements java.io.Serializable {
    private java.lang.String tid;

    private java.lang.String benid;

    private java.lang.String id;

    private at.gv.bmf.finanzonline.rkdb.Art_uebermittlung art_uebermittlung;

    private java.lang.Boolean erzwinge_asynchron;

    private at.gv.bmf.finanzonline.rkdb.Status_kasse status_kasse;

    private at.gv.bmf.finanzonline.rkdb.Status_see status_see;

    private at.gv.bmf.finanzonline.rkdb.Status_ggs status_ggs;

    private at.gv.bmf.finanzonline.rkdb.Rkdb rkdb;

    public RkdbRequest() {
    }

    public RkdbRequest(
           java.lang.String tid,
           java.lang.String benid,
           java.lang.String id,
           at.gv.bmf.finanzonline.rkdb.Art_uebermittlung art_uebermittlung,
           java.lang.Boolean erzwinge_asynchron,
           at.gv.bmf.finanzonline.rkdb.Status_kasse status_kasse,
           at.gv.bmf.finanzonline.rkdb.Status_see status_see,
           at.gv.bmf.finanzonline.rkdb.Status_ggs status_ggs,
           at.gv.bmf.finanzonline.rkdb.Rkdb rkdb) {
           this.tid = tid;
           this.benid = benid;
           this.id = id;
           this.art_uebermittlung = art_uebermittlung;
           this.erzwinge_asynchron = erzwinge_asynchron;
           this.status_kasse = status_kasse;
           this.status_see = status_see;
           this.status_ggs = status_ggs;
           this.rkdb = rkdb;
    }


    /**
     * Gets the tid value for this RkdbRequest.
     * 
     * @return tid
     */
    public java.lang.String getTid() {
        return tid;
    }


    /**
     * Sets the tid value for this RkdbRequest.
     * 
     * @param tid
     */
    public void setTid(java.lang.String tid) {
        this.tid = tid;
    }


    /**
     * Gets the benid value for this RkdbRequest.
     * 
     * @return benid
     */
    public java.lang.String getBenid() {
        return benid;
    }


    /**
     * Sets the benid value for this RkdbRequest.
     * 
     * @param benid
     */
    public void setBenid(java.lang.String benid) {
        this.benid = benid;
    }


    /**
     * Gets the id value for this RkdbRequest.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this RkdbRequest.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the art_uebermittlung value for this RkdbRequest.
     * 
     * @return art_uebermittlung
     */
    public at.gv.bmf.finanzonline.rkdb.Art_uebermittlung getArt_uebermittlung() {
        return art_uebermittlung;
    }


    /**
     * Sets the art_uebermittlung value for this RkdbRequest.
     * 
     * @param art_uebermittlung
     */
    public void setArt_uebermittlung(at.gv.bmf.finanzonline.rkdb.Art_uebermittlung art_uebermittlung) {
        this.art_uebermittlung = art_uebermittlung;
    }


    /**
     * Gets the erzwinge_asynchron value for this RkdbRequest.
     * 
     * @return erzwinge_asynchron
     */
    public java.lang.Boolean getErzwinge_asynchron() {
        return erzwinge_asynchron;
    }


    /**
     * Sets the erzwinge_asynchron value for this RkdbRequest.
     * 
     * @param erzwinge_asynchron
     */
    public void setErzwinge_asynchron(java.lang.Boolean erzwinge_asynchron) {
        this.erzwinge_asynchron = erzwinge_asynchron;
    }


    /**
     * Gets the status_kasse value for this RkdbRequest.
     * 
     * @return status_kasse
     */
    public at.gv.bmf.finanzonline.rkdb.Status_kasse getStatus_kasse() {
        return status_kasse;
    }


    /**
     * Sets the status_kasse value for this RkdbRequest.
     * 
     * @param status_kasse
     */
    public void setStatus_kasse(at.gv.bmf.finanzonline.rkdb.Status_kasse status_kasse) {
        this.status_kasse = status_kasse;
    }


    /**
     * Gets the status_see value for this RkdbRequest.
     * 
     * @return status_see
     */
    public at.gv.bmf.finanzonline.rkdb.Status_see getStatus_see() {
        return status_see;
    }


    /**
     * Sets the status_see value for this RkdbRequest.
     * 
     * @param status_see
     */
    public void setStatus_see(at.gv.bmf.finanzonline.rkdb.Status_see status_see) {
        this.status_see = status_see;
    }


    /**
     * Gets the status_ggs value for this RkdbRequest.
     * 
     * @return status_ggs
     */
    public at.gv.bmf.finanzonline.rkdb.Status_ggs getStatus_ggs() {
        return status_ggs;
    }


    /**
     * Sets the status_ggs value for this RkdbRequest.
     * 
     * @param status_ggs
     */
    public void setStatus_ggs(at.gv.bmf.finanzonline.rkdb.Status_ggs status_ggs) {
        this.status_ggs = status_ggs;
    }


    /**
     * Gets the rkdb value for this RkdbRequest.
     * 
     * @return rkdb
     */
    public at.gv.bmf.finanzonline.rkdb.Rkdb getRkdb() {
        return rkdb;
    }


    /**
     * Sets the rkdb value for this RkdbRequest.
     * 
     * @param rkdb
     */
    public void setRkdb(at.gv.bmf.finanzonline.rkdb.Rkdb rkdb) {
        this.rkdb = rkdb;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RkdbRequest)) return false;
        RkdbRequest other = (RkdbRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.tid==null && other.getTid()==null) || 
             (this.tid!=null &&
              this.tid.equals(other.getTid()))) &&
            ((this.benid==null && other.getBenid()==null) || 
             (this.benid!=null &&
              this.benid.equals(other.getBenid()))) &&
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.art_uebermittlung==null && other.getArt_uebermittlung()==null) || 
             (this.art_uebermittlung!=null &&
              this.art_uebermittlung.equals(other.getArt_uebermittlung()))) &&
            ((this.erzwinge_asynchron==null && other.getErzwinge_asynchron()==null) || 
             (this.erzwinge_asynchron!=null &&
              this.erzwinge_asynchron.equals(other.getErzwinge_asynchron()))) &&
            ((this.status_kasse==null && other.getStatus_kasse()==null) || 
             (this.status_kasse!=null &&
              this.status_kasse.equals(other.getStatus_kasse()))) &&
            ((this.status_see==null && other.getStatus_see()==null) || 
             (this.status_see!=null &&
              this.status_see.equals(other.getStatus_see()))) &&
            ((this.status_ggs==null && other.getStatus_ggs()==null) || 
             (this.status_ggs!=null &&
              this.status_ggs.equals(other.getStatus_ggs()))) &&
            ((this.rkdb==null && other.getRkdb()==null) || 
             (this.rkdb!=null &&
              this.rkdb.equals(other.getRkdb())));
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
        if (getTid() != null) {
            _hashCode += getTid().hashCode();
        }
        if (getBenid() != null) {
            _hashCode += getBenid().hashCode();
        }
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getArt_uebermittlung() != null) {
            _hashCode += getArt_uebermittlung().hashCode();
        }
        if (getErzwinge_asynchron() != null) {
            _hashCode += getErzwinge_asynchron().hashCode();
        }
        if (getStatus_kasse() != null) {
            _hashCode += getStatus_kasse().hashCode();
        }
        if (getStatus_see() != null) {
            _hashCode += getStatus_see().hashCode();
        }
        if (getStatus_ggs() != null) {
            _hashCode += getStatus_ggs().hashCode();
        }
        if (getRkdb() != null) {
            _hashCode += getRkdb().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RkdbRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">rkdbRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tid");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "tid"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">tid"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("benid");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "benid"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">benid"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">id"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("art_uebermittlung");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "art_uebermittlung"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">art_uebermittlung"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("erzwinge_asynchron");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "erzwinge_asynchron"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">erzwinge_asynchron"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status_kasse");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "status_kasse"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">status_kasse"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status_see");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "status_see"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">status_see"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status_ggs");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "status_ggs"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">status_ggs"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rkdb");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "rkdb"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">rkdb"));
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
