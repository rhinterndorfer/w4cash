/**
 * Rkdb.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package at.gv.bmf.finanzonline.rkdb;

public class Rkdb  implements java.io.Serializable {
    private java.lang.String fastnr;

    private org.apache.axis.types.PositiveInteger paket_nr;

    private java.util.Calendar ts_erstellung;

    private at.gv.bmf.finanzonline.rkdb.Registrierung_se[] registrierung_se;

    private at.gv.bmf.finanzonline.rkdb.Registrierung_kasse[] registrierung_kasse;

    private at.gv.bmf.finanzonline.rkdb.Registrierung_ggs[] registrierung_ggs;

    private at.gv.bmf.finanzonline.rkdb.Ausfall_se[] ausfall_se;

    private at.gv.bmf.finanzonline.rkdb.Wiederinbetriebnahme_se[] wiederinbetriebnahme_se;

    private at.gv.bmf.finanzonline.rkdb.Ausfall_kasse[] ausfall_kasse;

    private at.gv.bmf.finanzonline.rkdb.Wiederinbetriebnahme_kasse[] wiederinbetriebnahme_kasse;

    private at.gv.bmf.finanzonline.rkdb.Ausfall_ggs[] ausfall_ggs;

    private at.gv.bmf.finanzonline.rkdb.Wiederinbetriebnahme_ggs[] wiederinbetriebnahme_ggs;

    private at.gv.bmf.finanzonline.rkdb.Belegpruefung belegpruefung;

    public Rkdb() {
    }

    public Rkdb(
           java.lang.String fastnr,
           org.apache.axis.types.PositiveInteger paket_nr,
           java.util.Calendar ts_erstellung,
           at.gv.bmf.finanzonline.rkdb.Registrierung_se[] registrierung_se,
           at.gv.bmf.finanzonline.rkdb.Registrierung_kasse[] registrierung_kasse,
           at.gv.bmf.finanzonline.rkdb.Registrierung_ggs[] registrierung_ggs,
           at.gv.bmf.finanzonline.rkdb.Ausfall_se[] ausfall_se,
           at.gv.bmf.finanzonline.rkdb.Wiederinbetriebnahme_se[] wiederinbetriebnahme_se,
           at.gv.bmf.finanzonline.rkdb.Ausfall_kasse[] ausfall_kasse,
           at.gv.bmf.finanzonline.rkdb.Wiederinbetriebnahme_kasse[] wiederinbetriebnahme_kasse,
           at.gv.bmf.finanzonline.rkdb.Ausfall_ggs[] ausfall_ggs,
           at.gv.bmf.finanzonline.rkdb.Wiederinbetriebnahme_ggs[] wiederinbetriebnahme_ggs,
           at.gv.bmf.finanzonline.rkdb.Belegpruefung belegpruefung) {
           this.fastnr = fastnr;
           this.paket_nr = paket_nr;
           this.ts_erstellung = ts_erstellung;
           this.registrierung_se = registrierung_se;
           this.registrierung_kasse = registrierung_kasse;
           this.registrierung_ggs = registrierung_ggs;
           this.ausfall_se = ausfall_se;
           this.wiederinbetriebnahme_se = wiederinbetriebnahme_se;
           this.ausfall_kasse = ausfall_kasse;
           this.wiederinbetriebnahme_kasse = wiederinbetriebnahme_kasse;
           this.ausfall_ggs = ausfall_ggs;
           this.wiederinbetriebnahme_ggs = wiederinbetriebnahme_ggs;
           this.belegpruefung = belegpruefung;
    }


    /**
     * Gets the fastnr value for this Rkdb.
     * 
     * @return fastnr
     */
    public java.lang.String getFastnr() {
        return fastnr;
    }


    /**
     * Sets the fastnr value for this Rkdb.
     * 
     * @param fastnr
     */
    public void setFastnr(java.lang.String fastnr) {
        this.fastnr = fastnr;
    }


    /**
     * Gets the paket_nr value for this Rkdb.
     * 
     * @return paket_nr
     */
    public org.apache.axis.types.PositiveInteger getPaket_nr() {
        return paket_nr;
    }


    /**
     * Sets the paket_nr value for this Rkdb.
     * 
     * @param paket_nr
     */
    public void setPaket_nr(org.apache.axis.types.PositiveInteger paket_nr) {
        this.paket_nr = paket_nr;
    }


    /**
     * Gets the ts_erstellung value for this Rkdb.
     * 
     * @return ts_erstellung
     */
    public java.util.Calendar getTs_erstellung() {
        return ts_erstellung;
    }


    /**
     * Sets the ts_erstellung value for this Rkdb.
     * 
     * @param ts_erstellung
     */
    public void setTs_erstellung(java.util.Calendar ts_erstellung) {
        this.ts_erstellung = ts_erstellung;
    }


    /**
     * Gets the registrierung_se value for this Rkdb.
     * 
     * @return registrierung_se
     */
    public at.gv.bmf.finanzonline.rkdb.Registrierung_se[] getRegistrierung_se() {
        return registrierung_se;
    }


    /**
     * Sets the registrierung_se value for this Rkdb.
     * 
     * @param registrierung_se
     */
    public void setRegistrierung_se(at.gv.bmf.finanzonline.rkdb.Registrierung_se[] registrierung_se) {
        this.registrierung_se = registrierung_se;
    }

    public at.gv.bmf.finanzonline.rkdb.Registrierung_se getRegistrierung_se(int i) {
        return this.registrierung_se[i];
    }

    public void setRegistrierung_se(int i, at.gv.bmf.finanzonline.rkdb.Registrierung_se _value) {
        this.registrierung_se[i] = _value;
    }


    /**
     * Gets the registrierung_kasse value for this Rkdb.
     * 
     * @return registrierung_kasse
     */
    public at.gv.bmf.finanzonline.rkdb.Registrierung_kasse[] getRegistrierung_kasse() {
        return registrierung_kasse;
    }


    /**
     * Sets the registrierung_kasse value for this Rkdb.
     * 
     * @param registrierung_kasse
     */
    public void setRegistrierung_kasse(at.gv.bmf.finanzonline.rkdb.Registrierung_kasse[] registrierung_kasse) {
        this.registrierung_kasse = registrierung_kasse;
    }

    public at.gv.bmf.finanzonline.rkdb.Registrierung_kasse getRegistrierung_kasse(int i) {
        return this.registrierung_kasse[i];
    }

    public void setRegistrierung_kasse(int i, at.gv.bmf.finanzonline.rkdb.Registrierung_kasse _value) {
        this.registrierung_kasse[i] = _value;
    }


    /**
     * Gets the registrierung_ggs value for this Rkdb.
     * 
     * @return registrierung_ggs
     */
    public at.gv.bmf.finanzonline.rkdb.Registrierung_ggs[] getRegistrierung_ggs() {
        return registrierung_ggs;
    }


    /**
     * Sets the registrierung_ggs value for this Rkdb.
     * 
     * @param registrierung_ggs
     */
    public void setRegistrierung_ggs(at.gv.bmf.finanzonline.rkdb.Registrierung_ggs[] registrierung_ggs) {
        this.registrierung_ggs = registrierung_ggs;
    }

    public at.gv.bmf.finanzonline.rkdb.Registrierung_ggs getRegistrierung_ggs(int i) {
        return this.registrierung_ggs[i];
    }

    public void setRegistrierung_ggs(int i, at.gv.bmf.finanzonline.rkdb.Registrierung_ggs _value) {
        this.registrierung_ggs[i] = _value;
    }


    /**
     * Gets the ausfall_se value for this Rkdb.
     * 
     * @return ausfall_se
     */
    public at.gv.bmf.finanzonline.rkdb.Ausfall_se[] getAusfall_se() {
        return ausfall_se;
    }


    /**
     * Sets the ausfall_se value for this Rkdb.
     * 
     * @param ausfall_se
     */
    public void setAusfall_se(at.gv.bmf.finanzonline.rkdb.Ausfall_se[] ausfall_se) {
        this.ausfall_se = ausfall_se;
    }

    public at.gv.bmf.finanzonline.rkdb.Ausfall_se getAusfall_se(int i) {
        return this.ausfall_se[i];
    }

    public void setAusfall_se(int i, at.gv.bmf.finanzonline.rkdb.Ausfall_se _value) {
        this.ausfall_se[i] = _value;
    }


    /**
     * Gets the wiederinbetriebnahme_se value for this Rkdb.
     * 
     * @return wiederinbetriebnahme_se
     */
    public at.gv.bmf.finanzonline.rkdb.Wiederinbetriebnahme_se[] getWiederinbetriebnahme_se() {
        return wiederinbetriebnahme_se;
    }


    /**
     * Sets the wiederinbetriebnahme_se value for this Rkdb.
     * 
     * @param wiederinbetriebnahme_se
     */
    public void setWiederinbetriebnahme_se(at.gv.bmf.finanzonline.rkdb.Wiederinbetriebnahme_se[] wiederinbetriebnahme_se) {
        this.wiederinbetriebnahme_se = wiederinbetriebnahme_se;
    }

    public at.gv.bmf.finanzonline.rkdb.Wiederinbetriebnahme_se getWiederinbetriebnahme_se(int i) {
        return this.wiederinbetriebnahme_se[i];
    }

    public void setWiederinbetriebnahme_se(int i, at.gv.bmf.finanzonline.rkdb.Wiederinbetriebnahme_se _value) {
        this.wiederinbetriebnahme_se[i] = _value;
    }


    /**
     * Gets the ausfall_kasse value for this Rkdb.
     * 
     * @return ausfall_kasse
     */
    public at.gv.bmf.finanzonline.rkdb.Ausfall_kasse[] getAusfall_kasse() {
        return ausfall_kasse;
    }


    /**
     * Sets the ausfall_kasse value for this Rkdb.
     * 
     * @param ausfall_kasse
     */
    public void setAusfall_kasse(at.gv.bmf.finanzonline.rkdb.Ausfall_kasse[] ausfall_kasse) {
        this.ausfall_kasse = ausfall_kasse;
    }

    public at.gv.bmf.finanzonline.rkdb.Ausfall_kasse getAusfall_kasse(int i) {
        return this.ausfall_kasse[i];
    }

    public void setAusfall_kasse(int i, at.gv.bmf.finanzonline.rkdb.Ausfall_kasse _value) {
        this.ausfall_kasse[i] = _value;
    }


    /**
     * Gets the wiederinbetriebnahme_kasse value for this Rkdb.
     * 
     * @return wiederinbetriebnahme_kasse
     */
    public at.gv.bmf.finanzonline.rkdb.Wiederinbetriebnahme_kasse[] getWiederinbetriebnahme_kasse() {
        return wiederinbetriebnahme_kasse;
    }


    /**
     * Sets the wiederinbetriebnahme_kasse value for this Rkdb.
     * 
     * @param wiederinbetriebnahme_kasse
     */
    public void setWiederinbetriebnahme_kasse(at.gv.bmf.finanzonline.rkdb.Wiederinbetriebnahme_kasse[] wiederinbetriebnahme_kasse) {
        this.wiederinbetriebnahme_kasse = wiederinbetriebnahme_kasse;
    }

    public at.gv.bmf.finanzonline.rkdb.Wiederinbetriebnahme_kasse getWiederinbetriebnahme_kasse(int i) {
        return this.wiederinbetriebnahme_kasse[i];
    }

    public void setWiederinbetriebnahme_kasse(int i, at.gv.bmf.finanzonline.rkdb.Wiederinbetriebnahme_kasse _value) {
        this.wiederinbetriebnahme_kasse[i] = _value;
    }


    /**
     * Gets the ausfall_ggs value for this Rkdb.
     * 
     * @return ausfall_ggs
     */
    public at.gv.bmf.finanzonline.rkdb.Ausfall_ggs[] getAusfall_ggs() {
        return ausfall_ggs;
    }


    /**
     * Sets the ausfall_ggs value for this Rkdb.
     * 
     * @param ausfall_ggs
     */
    public void setAusfall_ggs(at.gv.bmf.finanzonline.rkdb.Ausfall_ggs[] ausfall_ggs) {
        this.ausfall_ggs = ausfall_ggs;
    }

    public at.gv.bmf.finanzonline.rkdb.Ausfall_ggs getAusfall_ggs(int i) {
        return this.ausfall_ggs[i];
    }

    public void setAusfall_ggs(int i, at.gv.bmf.finanzonline.rkdb.Ausfall_ggs _value) {
        this.ausfall_ggs[i] = _value;
    }


    /**
     * Gets the wiederinbetriebnahme_ggs value for this Rkdb.
     * 
     * @return wiederinbetriebnahme_ggs
     */
    public at.gv.bmf.finanzonline.rkdb.Wiederinbetriebnahme_ggs[] getWiederinbetriebnahme_ggs() {
        return wiederinbetriebnahme_ggs;
    }


    /**
     * Sets the wiederinbetriebnahme_ggs value for this Rkdb.
     * 
     * @param wiederinbetriebnahme_ggs
     */
    public void setWiederinbetriebnahme_ggs(at.gv.bmf.finanzonline.rkdb.Wiederinbetriebnahme_ggs[] wiederinbetriebnahme_ggs) {
        this.wiederinbetriebnahme_ggs = wiederinbetriebnahme_ggs;
    }

    public at.gv.bmf.finanzonline.rkdb.Wiederinbetriebnahme_ggs getWiederinbetriebnahme_ggs(int i) {
        return this.wiederinbetriebnahme_ggs[i];
    }

    public void setWiederinbetriebnahme_ggs(int i, at.gv.bmf.finanzonline.rkdb.Wiederinbetriebnahme_ggs _value) {
        this.wiederinbetriebnahme_ggs[i] = _value;
    }


    /**
     * Gets the belegpruefung value for this Rkdb.
     * 
     * @return belegpruefung
     */
    public at.gv.bmf.finanzonline.rkdb.Belegpruefung getBelegpruefung() {
        return belegpruefung;
    }


    /**
     * Sets the belegpruefung value for this Rkdb.
     * 
     * @param belegpruefung
     */
    public void setBelegpruefung(at.gv.bmf.finanzonline.rkdb.Belegpruefung belegpruefung) {
        this.belegpruefung = belegpruefung;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Rkdb)) return false;
        Rkdb other = (Rkdb) obj;
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
            ((this.registrierung_se==null && other.getRegistrierung_se()==null) || 
             (this.registrierung_se!=null &&
              java.util.Arrays.equals(this.registrierung_se, other.getRegistrierung_se()))) &&
            ((this.registrierung_kasse==null && other.getRegistrierung_kasse()==null) || 
             (this.registrierung_kasse!=null &&
              java.util.Arrays.equals(this.registrierung_kasse, other.getRegistrierung_kasse()))) &&
            ((this.registrierung_ggs==null && other.getRegistrierung_ggs()==null) || 
             (this.registrierung_ggs!=null &&
              java.util.Arrays.equals(this.registrierung_ggs, other.getRegistrierung_ggs()))) &&
            ((this.ausfall_se==null && other.getAusfall_se()==null) || 
             (this.ausfall_se!=null &&
              java.util.Arrays.equals(this.ausfall_se, other.getAusfall_se()))) &&
            ((this.wiederinbetriebnahme_se==null && other.getWiederinbetriebnahme_se()==null) || 
             (this.wiederinbetriebnahme_se!=null &&
              java.util.Arrays.equals(this.wiederinbetriebnahme_se, other.getWiederinbetriebnahme_se()))) &&
            ((this.ausfall_kasse==null && other.getAusfall_kasse()==null) || 
             (this.ausfall_kasse!=null &&
              java.util.Arrays.equals(this.ausfall_kasse, other.getAusfall_kasse()))) &&
            ((this.wiederinbetriebnahme_kasse==null && other.getWiederinbetriebnahme_kasse()==null) || 
             (this.wiederinbetriebnahme_kasse!=null &&
              java.util.Arrays.equals(this.wiederinbetriebnahme_kasse, other.getWiederinbetriebnahme_kasse()))) &&
            ((this.ausfall_ggs==null && other.getAusfall_ggs()==null) || 
             (this.ausfall_ggs!=null &&
              java.util.Arrays.equals(this.ausfall_ggs, other.getAusfall_ggs()))) &&
            ((this.wiederinbetriebnahme_ggs==null && other.getWiederinbetriebnahme_ggs()==null) || 
             (this.wiederinbetriebnahme_ggs!=null &&
              java.util.Arrays.equals(this.wiederinbetriebnahme_ggs, other.getWiederinbetriebnahme_ggs()))) &&
            ((this.belegpruefung==null && other.getBelegpruefung()==null) || 
             (this.belegpruefung!=null &&
              this.belegpruefung.equals(other.getBelegpruefung())));
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
        if (getRegistrierung_se() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRegistrierung_se());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRegistrierung_se(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getRegistrierung_kasse() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRegistrierung_kasse());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRegistrierung_kasse(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getRegistrierung_ggs() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRegistrierung_ggs());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRegistrierung_ggs(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getAusfall_se() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAusfall_se());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAusfall_se(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getWiederinbetriebnahme_se() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getWiederinbetriebnahme_se());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getWiederinbetriebnahme_se(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getAusfall_kasse() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAusfall_kasse());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAusfall_kasse(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getWiederinbetriebnahme_kasse() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getWiederinbetriebnahme_kasse());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getWiederinbetriebnahme_kasse(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getAusfall_ggs() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAusfall_ggs());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAusfall_ggs(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getWiederinbetriebnahme_ggs() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getWiederinbetriebnahme_ggs());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getWiederinbetriebnahme_ggs(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getBelegpruefung() != null) {
            _hashCode += getBelegpruefung().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Rkdb.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">rkdb"));
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
        elemField.setFieldName("registrierung_se");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "registrierung_se"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "registrierung_se"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("registrierung_kasse");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "registrierung_kasse"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "registrierung_kasse"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("registrierung_ggs");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "registrierung_ggs"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "registrierung_ggs"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ausfall_se");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "ausfall_se"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "ausfall_se"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("wiederinbetriebnahme_se");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "wiederinbetriebnahme_se"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "wiederinbetriebnahme_se"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ausfall_kasse");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "ausfall_kasse"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "ausfall_kasse"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("wiederinbetriebnahme_kasse");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "wiederinbetriebnahme_kasse"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "wiederinbetriebnahme_kasse"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ausfall_ggs");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "ausfall_ggs"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "ausfall_ggs"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("wiederinbetriebnahme_ggs");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "wiederinbetriebnahme_ggs"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "wiederinbetriebnahme_ggs"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("belegpruefung");
        elemField.setXmlName(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", "belegpruefung"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://finanzonline.bmf.gv.at/rkdb", ">belegpruefung"));
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
