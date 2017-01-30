/**
 * Abfrage_ergebnisStatus.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package at.gv.bmf.finanzonline.rkdb;

public class Abfrage_ergebnisStatus implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected Abfrage_ergebnisStatus(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _AKTIVIERT = "AKTIVIERT";
    public static final java.lang.String _REGISTRIERT = "REGISTRIERT";
    public static final java.lang.String _IN_BETRIEB = "IN_BETRIEB";
    public static final java.lang.String _AUSFALL = "AUSFALL";
    public static final Abfrage_ergebnisStatus AKTIVIERT = new Abfrage_ergebnisStatus(_AKTIVIERT);
    public static final Abfrage_ergebnisStatus REGISTRIERT = new Abfrage_ergebnisStatus(_REGISTRIERT);
    public static final Abfrage_ergebnisStatus IN_BETRIEB = new Abfrage_ergebnisStatus(_IN_BETRIEB);
    public static final Abfrage_ergebnisStatus AUSFALL = new Abfrage_ergebnisStatus(_AUSFALL);
    public java.lang.String getValue() { return _value_;}
    public static Abfrage_ergebnisStatus fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        Abfrage_ergebnisStatus enumeration = (Abfrage_ergebnisStatus)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static Abfrage_ergebnisStatus fromString(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumSerializer(
            _javaType, _xmlType);
    }
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumDeserializer(
            _javaType, _xmlType);
    }
    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Abfrage_ergebnisStatus.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">>abfrage_ergebnis>status"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
