//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2009 Openbravo, S.L.
//    http://www.openbravo.com/product/pos
//
//    This file is part of Openbravo POS.
//
//    Openbravo POS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Openbravo POS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.
package com.openbravo.pos.ticket;

import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import com.openbravo.pos.payment.PaymentInfo;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.LocalRes;
import com.openbravo.pos.customers.CustomerInfoExt;
import com.openbravo.pos.payment.PaymentInfoMagcard;
import com.openbravo.pos.util.StringUtils;
import at.w4cash.signature.SignatureModul;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

/**
 *
 * @author adrianromero
 */
public class TicketInfo implements SerializableRead, Externalizable {

    private static final long serialVersionUID = 2765650092387265178L;

    public static final int RECEIPT_NORMAL = 0;
    public static final int RECEIPT_REFUND = 1;
    public static final int RECEIPT_PAYMENT = 2;

    private static DateFormat m_dateformat = new SimpleDateFormat("HH:mm:ss");

    private String m_sId;
    private int tickettype;
    private int m_iTicketId;
    private Integer m_iCashTicketId;
    private String m_SignatureId;
    private byte[] m_SignatureValue;
    private Double m_CashSumCounter;
    private Integer m_algorithmId;
    private String m_posId;
    private Integer m_signatureoutoforder;
    private byte[] m_chainvalue;
    private byte[] m_cashSumCounterEnc;
    private Integer m_validation;
    private Integer m_month;
    private java.util.Date m_dDate;
    private Properties attributes;
    private UserInfo m_User;
    private CustomerInfoExt m_Customer;
    private String m_sActiveCash;
    private List<TicketLineInfo> m_aLines;
    private List<TicketLineInfo> m_aLinesSorted;
    private List<PaymentInfo> payments;
    private List<TicketTaxInfo> taxes;
    private String m_sResponse;
    private String m_sTempComment;
    private Object m_info;
    private String m_qrcode;
    
    public void SetInfo(Object info)
    {
    	m_info = info;
    }

    /** Creates new TicketModel */
    public TicketInfo() {
        m_sId = UUID.randomUUID().toString();
        tickettype = RECEIPT_NORMAL;
        m_iTicketId = 0; // incrementamos
        m_iCashTicketId = null;
        m_SignatureId = null;
        m_SignatureValue = null;
        m_CashSumCounter = null;
        m_algorithmId = null;
        m_signatureoutoforder = null;
        m_chainvalue = null;
        m_cashSumCounterEnc = null;
        m_validation = null;
        m_month = null;
        m_posId = null;
        m_dDate = new Date();
        attributes = new Properties();
        m_User = null;
        m_Customer = null;
        m_sActiveCash = null;
        m_aLines = new ArrayList<TicketLineInfo>(); // vacio de lineas
        m_aLinesSorted = new ArrayList<TicketLineInfo>(); 

        payments = new ArrayList<PaymentInfo>();
        taxes = null;
        m_sResponse = null;
        m_sTempComment = null;
        m_info = null;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        // esto es solo para serializar tickets que no estan en la bolsa de tickets pendientes
        out.writeObject(m_sId);
        out.writeInt(tickettype);
        out.writeInt(m_iTicketId);
        out.writeObject(m_Customer);
        out.writeObject(m_dDate);
        out.writeObject(attributes);
        out.writeObject(m_aLines);
        out.writeObject(m_sTempComment);
        out.writeObject(m_info);
        out.writeObject(m_iCashTicketId);
        out.writeObject(m_SignatureId);
        out.writeObject(m_SignatureValue);
        out.writeObject(m_CashSumCounter);
        out.writeObject(m_algorithmId);
        out.writeObject(m_posId);
        out.writeObject(m_signatureoutoforder);
        out.writeObject(m_chainvalue);
        out.writeObject(m_cashSumCounterEnc);
        out.writeObject(m_validation);
        out.writeObject(m_month);
    }

    @SuppressWarnings("unchecked")
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        // esto es solo para serializar tickets que no estan en la bolsa de tickets pendientes
        m_sId = (String) in.readObject();
        tickettype = in.readInt();
        m_iTicketId = in.readInt();
        m_Customer = (CustomerInfoExt) in.readObject();
        m_dDate = (Date) in.readObject();
        attributes = (Properties) in.readObject();
        m_aLines = (List<TicketLineInfo>) in.readObject();
        try {
        	m_sTempComment = (String) in.readObject();
        	m_info = in.readObject();
        	
            m_iCashTicketId = (Integer)in.readObject();
            m_SignatureId = (String) in.readObject();
            m_SignatureValue = (byte[]) in.readObject();
            m_CashSumCounter = (Double) in.readObject();
            m_algorithmId = (Integer)in.readObject();
            m_posId = (String) in.readObject();
            m_signatureoutoforder = (Integer)in.readObject();
            m_chainvalue = (byte[]) in.readObject();
            m_cashSumCounterEnc = (byte[]) in.readObject();
            m_validation = (Integer)in.readObject();
            m_month = (Integer)in.readObject();
        } catch(Exception ex) {
        	// do nothing
        	// optional additional data
        	// possible not filled
        }
        m_User = null;
        m_sActiveCash = null;

        payments = new ArrayList<PaymentInfo>();
        taxes = null;
    }

    public void readValues(DataRead dr) throws BasicException {
        m_sId = dr.getString(1);
        tickettype = dr.getInt(2).intValue();
        m_iTicketId = dr.getInt(3).intValue();
        m_dDate = dr.getTimestamp(4);
        m_sActiveCash = dr.getString(5);
        try {
            byte[] img = dr.getBytes(6);
            if (img != null) {
                attributes.loadFromXML(new ByteArrayInputStream(img));
            }
        } catch (IOException e) {
        }
        m_User = new UserInfo(dr.getString(7), dr.getString(8));
        m_Customer = new CustomerInfoExt(dr.getString(9));
        
        m_iCashTicketId = dr.getInt(10);
        m_SignatureId = dr.getString(11);
        m_SignatureValue = dr.getBytes(12);
        m_CashSumCounter = dr.getDouble(13);
        m_algorithmId = dr.getInt(14);
        m_posId = dr.getString(15);
        m_signatureoutoforder = dr.getInt(16);
 
        m_chainvalue = dr.getBytes(17);
        m_cashSumCounterEnc = dr.getBytes(18);
        m_validation = dr.getInt(19);
        m_month = dr.getInt(20);
        
        m_aLines = new ArrayList<TicketLineInfo>();

        payments = new ArrayList<PaymentInfo>();
        taxes = null;
    }

    public TicketInfo copyTicketHeader() {
    	TicketInfo t = new TicketInfo();

        t.tickettype = tickettype;
        t.m_iTicketId = m_iTicketId;
        t.m_dDate = m_dDate;
        t.m_sActiveCash = m_sActiveCash;
        t.attributes = (Properties) attributes.clone();
        t.m_User = m_User;
        t.m_Customer = m_Customer;
        t.m_sTempComment = m_sTempComment;
        

        t.m_aLines = new ArrayList<TicketLineInfo>();

        t.payments = new LinkedList<PaymentInfo>();

        return t;
    }
    
    public TicketInfo copyTicket() {
    	TicketInfo t = copyTicketHeader();
        
    	for (TicketLineInfo l : m_aLines) {
            t.m_aLines.add(l.copyTicketLine());
        }
        t.refreshLines();

        for (PaymentInfo p : payments) {
            t.payments.add(p.copyPayment());
        }

        // taxes are not copied, must be calculated again.

        return t;
    }

    public void SetTicketLinesMultiplyClone()
    {
    	for (TicketLineInfo l : m_aLines) {
    		l.CloneMultiply();
    	}
    }
    
    public void SetTicketLinesMultiplyCloneInvalid()
    {
    	for (TicketLineInfo l : m_aLines) {
    		l.InvalidateMultiply();
    	}
    }
    
    public String getId() {
        return m_sId;
    }

    public int getTicketType() {
        return tickettype;
    }

    public void setTicketType(int tickettype) {
        this.tickettype = tickettype;
    }

    public int getTicketId() {
        return m_iTicketId;
    }

    public void setTicketId(int iTicketId) {
        m_iTicketId = iTicketId;
    // refreshLines();
    }
    
    public String getPosId() {
    	return m_posId;
    }
    
    public void setPosId(String posId) {
    	m_posId = posId;
    }
    
    public Boolean getSignatureOutOfOrder() {
    	return m_signatureoutoforder == null || m_signatureoutoforder == 0 ? false : true;
    }
    
    public void setSignatureOutOfOrder(Boolean outOfOrder) {
    	m_signatureoutoforder = outOfOrder ? 1 : 0;
    }
    
    public String getChainValue() {
    	if(m_chainvalue != null)
    		return new String(m_chainvalue, StandardCharsets.UTF_8);
    	return null;
    }
    
    public byte[] getChainValueBlob() {
    	return m_chainvalue;
    }
    
    public void setChainValue(String chainValue) {
    	if(chainValue != null)
    		m_chainvalue = chainValue.getBytes(StandardCharsets.UTF_8);
    	else
    		m_chainvalue = null;
    }

    public String getCashSumCounterEnc() {
    	if(m_cashSumCounterEnc != null)
    		return new String(m_cashSumCounterEnc, StandardCharsets.UTF_8);
    	return null;
    }
    
    public byte[] getCashSumCounterEncBlob() {
    	return m_cashSumCounterEnc;
    }
    
    public void setCashSumCounterEnc(String cashSumCounterEnc) {
    	if(cashSumCounterEnc != null)
    		m_cashSumCounterEnc = cashSumCounterEnc.getBytes(StandardCharsets.UTF_8);
    	else
    		m_cashSumCounterEnc = null;
    }
    
    
    
    public Integer getAlgorithmId() {
    	return m_algorithmId;
    }
    
    public void setAlgorithmId(Integer algorithmId) {
    	m_algorithmId = algorithmId;
    }
    
    public Integer getCashTicketId() {
    	return m_iCashTicketId;
    }
    
    public void setCashTicketId(Integer cashId) {
    	m_iCashTicketId = cashId;
    }
    
    
    public Integer getValidation() {
    	return m_validation;
    }
    
    public void setValidation(Integer validation) {
    	m_validation = validation;
    }
    
    public Integer getMonth() {
    	return m_month;
    }
    
    public void setMonth(Integer month) {
    	m_month = month;
    }
    
    public String getSignatureId() {
    	return m_SignatureId;
    }
    
    public void setSignatureId(String signatureId) {
    	m_SignatureId = signatureId;
    }
    
    public String getSignatureValue() {
    	return new String(m_SignatureValue, StandardCharsets.UTF_8);
    }
    
    public byte[] getSignatureValueBlob() {
    	return m_SignatureValue;
    }
    
    public void setSignatureValue(String signatureValue) {
    	if(signatureValue != null)
    		m_SignatureValue = signatureValue.getBytes(StandardCharsets.UTF_8);
    	else
    		m_SignatureValue = null;
    }
    
    public String getSigningClearText() throws BasicException {
    	SignatureModul sig = SignatureModul.getInstance();
    	
    	StringBuilder sb = new StringBuilder();
    	sb.append("_");
    	sb.append("R");
    	sb.append(getAlgorithmId());
    	sb.append("-");
    	sb.append(sig.GetZDAId(getSignatureId()));
    	sb.append("_");
    	sb.append(getPosId());
    	sb.append("_");
    	sb.append(getCashTicketId());
    	sb.append("_");
    	String ticketDateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(getDate());
    	sb.append(ticketDateTime);
    	sb.append("_");
    	double tax20 = 0.0;
    	double tax10 = 0.0;
    	double tax13 = 0.0;
    	double tax0 = 0.0;
    	double tax19 = 0.0;
    	for(TicketTaxInfo tax : getTaxes())
    	{
    		if(tax.getTaxInfo().getRate() == 0.2)
    		{
    			tax20 = tax.getTotal();
    		}
    		if(tax.getTaxInfo().getRate() == 0.1)
    		{
    			tax10 = tax.getTotal();
    		}
    		if(tax.getTaxInfo().getRate() == 0.13)
    		{
    			tax13 = tax.getTotal();
    		}
    		if(tax.getTaxInfo().getRate() == 0.0)
    		{
    			tax0 = tax.getTotal();
    		}
    		if(tax.getTaxInfo().getRate() == 0.19)
    		{
    			tax19 = tax.getTotal();
    		}
    	}
    	NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMAN);
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        nf.setGroupingUsed(false);
        DecimalFormat df = (DecimalFormat) nf;
    	// 20%
    	sb.append(df.format(tax20));
    	sb.append("_");
    	// 10%
    	sb.append(df.format(tax10));
    	sb.append("_");
    	// 13%
    	sb.append(df.format(tax13));
    	sb.append("_");
    	// 0%
    	sb.append(df.format(tax0));
    	sb.append("_");
    	// 19%
    	sb.append(df.format(tax19));
    	sb.append("_");
    	// cash counter enc
    	sb.append(getCashSumCounterEnc());
    	sb.append("_");
    	// certificate serial id
    	sb.append(sig.GetSignatureSerialNumberHex(getSignatureId()));
    	sb.append("_");
    	String chainValue = getChainValue();
    	sb.append(chainValue);
    	
    	return sb.toString();
    }
    
    public void setQRCode(String qrcode)
    {
    	m_qrcode = qrcode;
    }
    
    public String getQRCode()
    {
    	return m_qrcode == null ? "" : m_qrcode;
    }
    
    public Double getCashSumCounter() {
    	return m_CashSumCounter;
    }
    
    public void setCashSumCounter(Double sum) {
    	m_CashSumCounter = sum;
    }

    public String getNameWithExt()
    {
    	return getName(m_info, false);
    }
    
    public String getName(Object info, Boolean withCustomer) {
    	m_info = info;
        StringBuffer name = new StringBuffer();

        if (withCustomer && getCustomerId() != null) {
            name.append(m_Customer.toString());
            name.append(" - ");
        }

        if (info == null) {
            if (m_iTicketId == 0) {
                name.append("(" + m_dateformat.format(m_dDate) + ")");
            } else {
                name.append(Integer.toString(m_iTicketId));
            }
        } else {
            name.append(info.toString());
        }
        
        if(getTempComment() != null && getTempComment() != "")
        {
        	name.append(" - ");
        	name.append(getTempComment());
        }
        return name.toString();
    }
    
    public String getName(Object info) {

    	return getName(info, true);
    }

    public String getTempComment()
    {
    	return m_sTempComment;
    }
    
    public void setTempComment(String comment)
    {
    	m_sTempComment = comment;
    }
    
    public String getName() {
        return getName(null);
    }

    public java.util.Date getDate() {
        return m_dDate;
    }

    public void setDate(java.util.Date dDate) {
        m_dDate = dDate;
    }

    public UserInfo getUser() {
        return m_User;
    }

    public void setUser(UserInfo value) {
        m_User = value;
    }

    public CustomerInfoExt getCustomer() {
        return m_Customer;
    }

    public void setCustomer(CustomerInfoExt value) {
        m_Customer = value;
    }

    public String getCustomerId() {
        if (m_Customer == null) {
            return null;
        } else {
            return m_Customer.getId();
        }
    }
    
    public String getTransactionID(){
        return (getPayments().size()>0)
            ? ( getPayments().get(getPayments().size()-1) ).getTransactionID()
            : StringUtils.getCardNumber(); //random transaction ID
    }
    
    public String getReturnMessage(){
        return ( (getPayments().get(getPayments().size()-1)) instanceof PaymentInfoMagcard )
            ? ((PaymentInfoMagcard)(getPayments().get(getPayments().size()-1))).getReturnMessage()
            : LocalRes.getIntString("button.ok");
    }

    public void setActiveCash(String value) {
        m_sActiveCash = value;
    }

    public String getActiveCash() {
        return m_sActiveCash;
    }

    public String getProperty(String key) {
        return attributes.getProperty(key);
    }

    public String getProperty(String key, String defaultvalue) {
        return attributes.getProperty(key, defaultvalue);
    }

    public void setProperty(String key, String value) {
        attributes.setProperty(key, value);
    }

    public Properties getProperties() {
        return attributes;
    }

    public TicketLineInfo getLine(int index) {
        return m_aLines.get(index);
    }

    public void addLine(TicketLineInfo oLine) {

        oLine.setTicket(m_sId, m_aLines.size());
        m_aLines.add(oLine);
    }

    public void insertLine(int index, TicketLineInfo oLine) {
        m_aLines.add(index, oLine);
        refreshLines();
    }

    public void setLine(int index, TicketLineInfo oLine) {
        oLine.setTicket(m_sId, index);
        m_aLines.set(index, oLine);
    }

    public void removeLine(int index) {
        m_aLines.remove(index);
        refreshLines();
    }

    private void refreshLines() {
        for (int i = 0; i < m_aLines.size(); i++) {
            getLine(i).setTicket(m_sId, i);
        }
    }

    public int getLinesCount() {
    	if(m_aLines == null)
    		return 0;
        return m_aLines.size();
    }
    
    public double getArticlesCount() {
        double dArticles = 0.0;
        TicketLineInfo oLine;

        for (Iterator<TicketLineInfo> i = m_aLines.iterator(); i.hasNext();) {
            oLine = i.next();
            dArticles += oLine.getMultiply();
        }

        return dArticles;
    }

    public double getSubTotal() {
        return getTotal()-getTax();
    }

    public double getTax() {

        double sum = 0.0;
        for (TicketLineInfo line : m_aLines) {
            sum += line.getTax();
        }
        return sum;
    }

    public double getTotal() {
        
    	double sum = 0.0;
        for (TicketLineInfo line : m_aLines) {
            sum += line.getValue();
        }
        return sum;
    }
    
    public double getTotal2() {
        return round(getTotal(),2);
    }

    public double getTotalPaid() {

        double sum = 0.0;
        for (PaymentInfo p : payments) {
            if (!"debtpaid".equals(p.getName())) {
                sum += p.getTotal();
            }
        }
        return sum;
    }

    public List<TicketLineInfo> getLines() {
        return m_aLines;
    }

    public void sortLines(Comparator<TicketLineInfo> c)
    {
    	m_aLinesSorted.clear();
    	m_aLinesSorted.addAll(m_aLines);
    	for(TicketLineInfo li : m_aLinesSorted)
    	{
    		li.setProperty("lineIndex", String.format("%010d", m_aLines.indexOf(li)));
    	}
    	m_aLinesSorted.sort(c);
    }
    
    public List<TicketLineInfo> getLinesSorted() {
        return m_aLinesSorted;
    }
    
    public void setLines(List<TicketLineInfo> l) {
        m_aLines = l;
    }

    public List<PaymentInfo> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentInfo> l) {
        payments = l;
    }

    public void resetPayments() {
        payments = new ArrayList<PaymentInfo>();
    }

    public List<TicketTaxInfo> getTaxes() {
        return taxes;
    }

    public boolean hasTaxesCalculated() {
        return taxes != null && !taxes.isEmpty();
    }

    public void setTaxes(List<TicketTaxInfo> l) {
        taxes = l;
    }

    public void resetTaxes() {
        taxes = null;
    }
    
    public void mergeDuplicateLines()
    {
    	if(getLinesCount() > 1)
    	{
    		for(int i=0;i < getLinesCount();i++)
    		{
    			TicketLineInfo current_ticketline = getLine(i);
    			double current_amount = current_ticketline.getMultiply();
    			
    			String current_productid = current_ticketline.getProductID();
         		String current_AttSetDesc = current_ticketline.getProductAttSetInstDesc();
         		String current_lineGroup = current_ticketline.getLineGroup();
         		
    			if(current_amount != 0 && !current_ticketline.isProductCom())
    			{
    				for (int j = i + 1 ; j < getLinesCount() ; j++) {
    					TicketLineInfo loop_ticketline = getLine(j);
    					double loop_amount  = loop_ticketline.getMultiply();
		         		
    					String loop_productid    = loop_ticketline.getProductID();
		         		String loop_AttSetDesc   = loop_ticketline.getProductAttSetInstDesc();
		         		String loop_lineGroup	 = loop_ticketline.getLineGroup();
		         		
		         		if (!loop_ticketline.isProductCom()
		         				&& loop_amount != 0
		         				&& current_AttSetDesc == "" 
		         				&& loop_AttSetDesc == "" 
		         				&& loop_productid != null 
		         				&& loop_productid.equals(current_productid)
		         				&& current_lineGroup.equals(loop_lineGroup)
		         				&& loop_ticketline.getPrice() == current_ticketline.getPrice() 
		         			){
		         			current_amount = current_amount + loop_amount;
							loop_ticketline.setMultiply(0);
							
							// move com products to i+1
							// check follower lines
							for(int k=j+1; k < getLinesCount(); k++)
							{
								TicketLineInfo comProductLine = getLine(k);
								if(comProductLine.isProductCom())
								{
									removeLine(k);
									insertLine(i+1, comProductLine);
									j++;
								}
								else
									break; // break when first not com product is found
							}
						}	
		         		
		         		
		         		
		         		
    				}
    				
    				current_ticketline.setMultiply(current_amount);
    			}
    		}
    	}
    	
    	// now remove the ticket lines where the unit = 0
		// start deleteing in reverse order
		for (int i = getLinesCount() - 1 ; i > 0 ; i--) { 
			TicketLineInfo loop_ticketline = getLine(i);
			double loop_amount  = loop_ticketline.getMultiply();
			if (loop_amount == 0){
				removeLine(i);
			}
		}
    	
    }

    public TicketTaxInfo getTaxLine(TaxInfo tax) {

        for (TicketTaxInfo taxline : taxes) {
            if (tax.getId().equals(taxline.getTaxInfo().getId())) {
                return taxline;
            }
        }

        return new TicketTaxInfo(tax);
    }

    public TicketTaxInfo[] getTaxLines() {

        Map<String, TicketTaxInfo> m = new HashMap<String, TicketTaxInfo>();

        TicketLineInfo oLine;
        for (Iterator<TicketLineInfo> i = m_aLines.iterator(); i.hasNext();) {
            oLine = i.next();

            TicketTaxInfo t = m.get(oLine.getTaxInfo().getId());
            if (t == null) {
                t = new TicketTaxInfo(oLine.getTaxInfo());
                m.put(t.getTaxInfo().getId(), t);
            }
            t.add(oLine.getSubValue());
        }

        // return dSuma;       
        Collection<TicketTaxInfo> avalues = m.values();
        return avalues.toArray(new TicketTaxInfo[avalues.size()]);
    }

    public String printId() {
        if (m_iTicketId > 0) {
            // valid ticket id
            return Formats.INT.formatValue(new Integer(m_iTicketId));
        } else {
            return "";
        }
    }

    public String printDate() {
        return Formats.TIMESTAMP.formatValue(m_dDate);
    }

    public String printUser() {
        return m_User == null ? "" : m_User.getName();
    }
    
    public Boolean hideUser() {
        return m_User == null ? true : m_User.getName().endsWith("$");
    }

    public String printCustomer() {
        return m_Customer == null ? "" : m_Customer.getName();
    }

    public String printArticlesCount() {
        return Formats.DOUBLE.formatValue(new Double(getArticlesCount()));
    }

    public String printSubTotal() {
        return Formats.CURRENCY.formatValue(getTotal()-getTax());
    }

    public String printTax() {
        return Formats.CURRENCY.formatValue(getTax());
    }

    public String printTotal() {
         return Formats.CURRENCY.formatValue(getTotal());
    }

    public String printTotalPaid() {
        return Formats.CURRENCY.formatValue(new Double(getTotalPaid()));
    }
    
    public String printCashTicketId() {
    	return Formats.INT.formatValue(m_iCashTicketId);
    }
    
    public String printPosId() {
    	return Formats.STRING.formatValue(m_posId);
    }
    
    public String printSignatureState()
    {
    	if(m_iCashTicketId != null && getSignatureOutOfOrder())
    		return "Sicherheitseinrichtung ausgefallen";
    	
    	return "";
    }
    
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    
}

