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

package com.openbravo.pos.panels;

import java.util.*;
import javax.swing.table.AbstractTableModel;

import org.joda.time.DateTime;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.*;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.util.StringUtils;

/**
 *
 * @author adrianromero
 */
public class PaymentsModel {

    private String m_sHost;
            
    private Integer m_iPayments;
    private Double m_dPaymentsTotal;
    private Double m_dPaymentsCashTotal;
    private java.util.List<PaymentsLine> m_lpayments;
    
    private final static String[] PAYMENTHEADERS = {"Label.Payment", "Label.Description", "label.totalcash"};
    
    private Integer m_iSales;
    private Double m_dSalesBase;
    private Integer m_iSalesFree;
    private Double m_dSalesBaseFree;
    private Double m_dSalesTaxes;
    private Double m_dSalesTaxesFree;
    private java.util.List<SalesLine> m_lsales;
    private java.util.List<SalesLine> m_lsalesFree;
    private AppView m_app;
    private Date m_cashEndDate;
    private Date m_cashStartDate;
    private int m_sequence;
    
    private final static String[] SALEHEADERS = {"label.taxcash", "label.totalcash"};

    private PaymentsModel() {
    }    
    
    public static PaymentsModel emptyInstance() {
        
        PaymentsModel p = new PaymentsModel();
        
        p.m_iPayments = new Integer(0);
        p.m_dPaymentsTotal = new Double(0.0);
        p.m_lpayments = new ArrayList<PaymentsLine>();
        
        p.m_iSales = null;
        p.m_dSalesBase = null;
        p.m_iSalesFree = null;
        p.m_dSalesBaseFree = null;
        p.m_dSalesTaxes = null;
        p.m_dSalesTaxesFree = null;
        p.m_lsales = new ArrayList<SalesLine>();
        p.m_lsalesFree = new ArrayList<SalesLine>();
        return p;
    }
    
    public static PaymentsModel loadInstance(AppView app, String cashIndex) throws BasicException {
    	PaymentsModel p = new PaymentsModel();

        int currencyDecimals = Formats.getCurrencyDecimals();
        
        // Propiedades globales
        p.m_sHost = app.getProperties().getHost();
        p.m_app = app;
        
        
        
        // shift data
        Object[] shiftdata = (Object []) new StaticSentence(app.getSession()
            , "SELECT DATESTART, DATEEND, HOST, HOSTSEQUENCE " +
              "FROM CLOSEDCASH " +
              "WHERE MONEY = ?"
            , SerializerWriteString.INSTANCE
            , new SerializerReadBasic(new Datas[] {Datas.TIMESTAMP, Datas.TIMESTAMP, Datas.STRING, Datas.INT}))
            .find(cashIndex);
        
        if (shiftdata == null ) {
        	p.setDateEnd(null);
            p.setDateStart(null);
            p.setSequence(0);
        } else {
        	p.setDateStart((Date) shiftdata[0]);
        	p.setDateEnd((Date) shiftdata[1]);
        	p.setSequence((Integer) shiftdata[3]);
        }  
        
        // Pagos
        Object[] valtickets = (Object []) new StaticSentence(app.getSession()
            , "SELECT COUNT(*) " +
              "FROM PAYMENTS, RECEIPTS " +
              "WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND RECEIPTS.MONEY = ?"
            , SerializerWriteString.INSTANCE
            , new SerializerReadBasic(new Datas[] {Datas.INT}))
            .find(cashIndex);
        if (valtickets == null ) {
            p.m_iPayments = new Integer(0);
        } else {
            p.m_iPayments = (Integer) valtickets[0];
        }  
        
        Object[] valticketstotal = (Object []) new StaticSentence(app.getSession()
                , "SELECT SUM(PAYMENTS.TOTAL) " +
                  "FROM PAYMENTS, RECEIPTS " +
//                  "WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND RECEIPTS.MONEY = ?"
                  "WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND PAYMENTS.PAYMENT <> 'free' AND RECEIPTS.MONEY = ?"
                , SerializerWriteString.INSTANCE
                , new SerializerReadBasic(new Datas[] {Datas.DOUBLE}))
                .find(cashIndex);
            
        if (valticketstotal == null ) {
            p.m_dPaymentsTotal = new Double(0.0);
        } else {
            p.m_dPaymentsTotal = (Double) valticketstotal[0];
        }
        
        Object[] valticketscashtotal = (Object []) new StaticSentence(app.getSession()
                , "SELECT SUM(PAYMENTS.TOTAL) " +
                  "FROM PAYMENTS, RECEIPTS " +
//                  "WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND RECEIPTS.MONEY = ?"
                  "WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND PAYMENTS.PAYMENT in ('cash','cashrefund') AND RECEIPTS.MONEY = ?"
                , SerializerWriteString.INSTANCE
                , new SerializerReadBasic(new Datas[] {Datas.DOUBLE}))
                .find(cashIndex);
            
        if (valticketscashtotal == null ) {
            p.m_dPaymentsCashTotal = new Double(0.0);
        } else {
            p.m_dPaymentsCashTotal = (Double) valticketscashtotal[0];
        }
        
        
        
        List l = new StaticSentence(app.getSession()            
            //, "SELECT PAYMENTS.PAYMENT, SUM(PAYMENTS.TOTAL) " +
            , "SELECT PAYMENTS.PAYMENT, SUM(PAYMENTS.TOTAL), PAYMENTS.DESCRIPTION " +
              "FROM PAYMENTS, RECEIPTS " +
              "WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND RECEIPTS.MONEY = ? " +
//              "WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND PAYMENTS.PAYMENT <> 'free' AND RECEIPTS.MONEY = ? " +
              "GROUP BY PAYMENTS.DESCRIPTION, PAYMENTS.PAYMENT"
            , SerializerWriteString.INSTANCE
            , new SerializerReadClass(PaymentsModel.PaymentsLine.class)) //new SerializerReadBasic(new Datas[] {Datas.STRING, Datas.DOUBLE}))
            .list(cashIndex); 
        
        if (l == null) {
            p.m_lpayments = new ArrayList();
        } else {
            p.m_lpayments = l;
        }        
        
        // Sales
        Object[] recsales = (Object []) new StaticSentence(app.getSession(),
        	"SELECT COUNT(DISTINCT RECEIPTS.ID), SUM(ROUND(TAXLINES.BASE,?)) " +
        	"FROM RECEIPTS, TAXLINES, PAYMENTS WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND PAYMENTS.PAYMENT <> 'free' AND RECEIPTS.ID = TAXLINES.RECEIPT AND RECEIPTS.MONEY = ?",
            new SerializerWriteBasic(new Datas[] {Datas.INT, Datas.STRING}),
            new SerializerReadBasic(new Datas[] {Datas.INT, Datas.DOUBLE}))
            .find(new Object[] {currencyDecimals, cashIndex});
        if (recsales == null) {
            p.m_iSales = null;
            p.m_dSalesBase = null;
        } else {
            p.m_iSales = (Integer) recsales[0];
            p.m_dSalesBase = (Double) recsales[1];
        }  
        
     // Sales Free
        Object[] recsalesfree = (Object []) new StaticSentence(app.getSession(),
    		"SELECT COUNT(DISTINCT RECEIPTS.ID), SUM(ROUND(TAXLINES.BASE,?)) " +
        	"FROM RECEIPTS, TAXLINES, PAYMENTS WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND PAYMENTS.PAYMENT = 'free' AND RECEIPTS.ID = TAXLINES.RECEIPT AND RECEIPTS.MONEY = ?",
            new SerializerWriteBasic(new Datas[] {Datas.INT, Datas.STRING}),
            new SerializerReadBasic(new Datas[] {Datas.INT, Datas.DOUBLE}))
        	.find(new Object[] {currencyDecimals, cashIndex});
        if (recsalesfree == null) {
            p.m_iSalesFree = null;
            p.m_dSalesBaseFree = null;
        } else {
            p.m_iSalesFree = (Integer) recsalesfree[0];
            p.m_dSalesBaseFree = (Double) recsalesfree[1];
        }  
        
        // Taxes
        Object[] rectaxes = (Object []) new StaticSentence(app.getSession(),
            "SELECT SUM(ROUND(TAXLINES.AMOUNT,?)) " +
//            "FROM RECEIPTS, TAXLINES WHERE RECEIPTS.ID = TAXLINES.RECEIPT AND RECEIPTS.MONEY = ?"
			"FROM RECEIPTS, TAXLINES, PAYMENTS WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND PAYMENTS.PAYMENT <> 'free' AND RECEIPTS.ID = TAXLINES.RECEIPT AND RECEIPTS.MONEY = ?"
			, new SerializerWriteBasic(new Datas[] {Datas.INT, Datas.STRING})
            , new SerializerReadBasic(new Datas[] {Datas.DOUBLE}))
        	.find(new Object[] {currencyDecimals, cashIndex});            
        if (rectaxes == null) {
            p.m_dSalesTaxes = null;
        } else {
            p.m_dSalesTaxes = (Double) rectaxes[0];
        } 
        
     // Taxes free
        Object[] rectaxesfree = (Object []) new StaticSentence(app.getSession(),
            "SELECT SUM(ROUND(TAXLINES.AMOUNT, ?)) " +
//            "FROM RECEIPTS, TAXLINES WHERE RECEIPTS.ID = TAXLINES.RECEIPT AND RECEIPTS.MONEY = ?"
			"FROM RECEIPTS, TAXLINES, PAYMENTS WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND PAYMENTS.PAYMENT = 'free' AND RECEIPTS.ID = TAXLINES.RECEIPT AND RECEIPTS.MONEY = ?"
			, new SerializerWriteBasic(new Datas[] {Datas.INT, Datas.STRING})
            , new SerializerReadBasic(new Datas[] {Datas.DOUBLE}))
        	.find(new Object[] {currencyDecimals, cashIndex});            
        if (rectaxesfree == null) {
            p.m_dSalesTaxesFree = null;
        } else {
            p.m_dSalesTaxesFree = (Double) rectaxesfree[0];
        } 
                
        List<SalesLine> asales = new StaticSentence(app.getSession(),
                "SELECT TAXCATEGORIES.NAME, SUM(ROUND(TAXLINES.AMOUNT, ?)),  SUM(ROUND(TAXLINES.BASE,?)) " +
//                "FROM RECEIPTS, TAXLINES, TAXES, TAXCATEGORIES WHERE RECEIPTS.ID = TAXLINES.RECEIPT AND TAXLINES.TAXID = TAXES.ID AND TAXES.CATEGORY = TAXCATEGORIES.ID " +
                "FROM RECEIPTS, TAXLINES, TAXES, TAXCATEGORIES, PAYMENTS WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND PAYMENTS.PAYMENT <> 'free' AND RECEIPTS.ID = TAXLINES.RECEIPT AND TAXLINES.TAXID = TAXES.ID AND TAXES.CATEGORY = TAXCATEGORIES.ID " +
                "AND RECEIPTS.MONEY = ? " +
                "GROUP BY TAXCATEGORIES.NAME"
                , new SerializerWriteBasic(new Datas[] {Datas.INT, Datas.INT, Datas.STRING})
                , new SerializerReadClass(PaymentsModel.SalesLine.class))
        		.list(new Object[] {currencyDecimals, currencyDecimals, cashIndex});
        if (asales == null) {
            p.m_lsales = new ArrayList<SalesLine>();
        } else {
            p.m_lsales = asales;
        }
        
        List<SalesLine> asalesfree = new StaticSentence(app.getSession(),
                "SELECT TAXCATEGORIES.NAME, SUM(ROUND(TAXLINES.AMOUNT, ?)),  SUM(ROUND(TAXLINES.BASE, ?)) " +
//                "FROM RECEIPTS, TAXLINES, TAXES, TAXCATEGORIES WHERE RECEIPTS.ID = TAXLINES.RECEIPT AND TAXLINES.TAXID = TAXES.ID AND TAXES.CATEGORY = TAXCATEGORIES.ID " +
                "FROM RECEIPTS, TAXLINES, TAXES, TAXCATEGORIES, PAYMENTS WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND PAYMENTS.PAYMENT = 'free' AND RECEIPTS.ID = TAXLINES.RECEIPT AND TAXLINES.TAXID = TAXES.ID AND TAXES.CATEGORY = TAXCATEGORIES.ID " +
                "AND RECEIPTS.MONEY = ? " +
                "GROUP BY TAXCATEGORIES.NAME"
                , new SerializerWriteBasic(new Datas[] {Datas.INT, Datas.INT, Datas.STRING})
                , new SerializerReadClass(PaymentsModel.SalesLine.class))
        		.list(new Object[] {currencyDecimals, currencyDecimals, cashIndex});
        if (asalesfree == null) {
            p.m_lsalesFree = new ArrayList<SalesLine>();
        } else {
            p.m_lsalesFree = asalesfree;
        }
         
        return p;
    }
    
    
    public static PaymentsModel loadInstance(AppView app) throws BasicException {
        
        return loadInstance(app, app.getActiveCashIndex(false, false));
    }

    public int getPayments() {
        return m_iPayments.intValue();
    }
    public double getTotal() {
        return m_dPaymentsTotal.doubleValue();
    }
    public String getHost() {
        return m_sHost;
    }

    public String printHost() {
        return StringUtils.encodeXML(m_sHost);
    }
    public String printDateStart() {
    	try {
    		return Formats.TIMESTAMP.formatValue(m_cashStartDate);
    	} catch(Exception ex)
    	{
    		return "";
    	}
    }
    
    public void setDateEnd(Date dateEnd)
    {
    	m_cashEndDate = dateEnd;
    }
    
    public void setDateStart(Date dateStart)
    {
    	m_cashStartDate = dateStart;
    }
    
    public void setSequence(int sequence)
    {
    	m_sequence = sequence;
    }
    
    public String printDateEnd() {
        try {
    		return Formats.TIMESTAMP.formatValue(m_cashEndDate);
    	} catch(Exception ex)
    	{
    		return "";
    	}
    }  
    public String printSequence() {
    	try {
    		return Formats.INT.formatValue(m_sequence);
    	} catch(Exception ex)
    	{
    		return "";
    	}
    }
    
    
    public String printPayments() {
        return Formats.INT.formatValue(m_iPayments);
    }

    public String printPaymentsTotal() {
        return Formats.CURRENCY.formatValue(m_dPaymentsTotal);
    }     
    
    public String printPaymentsCashTotal() {
        return Formats.CURRENCY.formatValue(m_dPaymentsCashTotal);
    }
    
    public List<PaymentsLine> getPaymentLines() {
        return m_lpayments;
    }
    
    public int getSales() {
        return m_iSales == null ? 0 : m_iSales.intValue();
    }    
    public String printSales() {
        return Formats.INT.formatValue(m_iSales);
    }
    public String printSalesFree() {
        return Formats.INT.formatValue(m_iSalesFree);
    }
    public String printSalesBase() {
        return Formats.CURRENCY.formatValue(m_dSalesBase);
    } 
    public String printSalesBaseFree() {
        return Formats.CURRENCY.formatValue(m_dSalesBaseFree);
    } 
    public String printSalesTaxes() {
        return Formats.CURRENCY.formatValue(m_dSalesTaxes);
    }
    public String printSalesTaxesFree() {
        return Formats.CURRENCY.formatValue(m_dSalesTaxesFree);
    }
    public String printSalesTotal() {            
        return Formats.CURRENCY.formatValue((m_dSalesBase == null || m_dSalesTaxes == null)
                ? null
                : m_dSalesBase + m_dSalesTaxes);
    }
    public String printSalesTotalFree() {            
        return Formats.CURRENCY.formatValue((m_dSalesBaseFree == null || m_dSalesTaxesFree == null)
                ? null
                : m_dSalesBaseFree + m_dSalesTaxesFree);
    }  
    public List<SalesLine> getSaleLines() {
        return m_lsales;
    }
    
    public AbstractTableModel getPaymentsModel() {
        return new AbstractTableModel() {
            public String getColumnName(int column) {
                return AppLocal.getIntString(PAYMENTHEADERS[column]);
            }
            public int getRowCount() {
                return m_lpayments.size();
            }
            public int getColumnCount() {
                return PAYMENTHEADERS.length;
            }
            public Object getValueAt(int row, int column) {
                PaymentsLine l = m_lpayments.get(row);
                switch (column) {
                case 0: return l.getType();
                case 1:
                	if(l.getDescription() == null)
                		return "";
                	else
                		return l.getDescription();
                case 2: return l.getValue();
                default: return null;
                }
            }  
        };
    }
    
    public static class SalesLine implements SerializableRead {
        
        private String m_SalesTaxName;
        private Double m_SalesTaxes;
        private Double m_SalesNet;
        
        public void readValues(DataRead dr) throws BasicException {
            m_SalesTaxName = dr.getString(1);
            m_SalesTaxes = dr.getDouble(2);
            m_SalesNet = dr.getDouble(3);
        }
        public String printTaxName() {
            return m_SalesTaxName;
        }      
        public String printTaxes() {
            return Formats.CURRENCY.formatValue(m_SalesTaxes);
        }
        public String getTaxName() {
            return m_SalesTaxName;
        }
        public Double getTaxes() {
            return m_SalesTaxes;
        }
        
        public Double getSalesNet() {
            return m_SalesNet;
        }
        public Double getSales() {
            return m_SalesNet + m_SalesTaxes;
        }
        public String printNet() {
            return Formats.CURRENCY.formatValue(m_SalesNet);
        }
        public String printSales() {
            return Formats.CURRENCY.formatValue(m_SalesNet + m_SalesTaxes);
        }
    }

    public AbstractTableModel getSalesModel() {
        return new AbstractTableModel() {
            public String getColumnName(int column) {
                return AppLocal.getIntString(SALEHEADERS[column]);
            }
            public int getRowCount() {
                return m_lsales.size();
            }
            public int getColumnCount() {
                return SALEHEADERS.length;
            }
            public Object getValueAt(int row, int column) {
                SalesLine l = m_lsales.get(row);
                switch (column) {
                case 0: return l.getTaxName();
                case 1: return l.getTaxes();
                default: return null;
                }
            }  
        };
    }
    
    public AbstractTableModel getSalesModelFree() {
        return new AbstractTableModel() {
            public String getColumnName(int column) {
                return AppLocal.getIntString(SALEHEADERS[column]);
            }
            public int getRowCount() {
                return m_lsalesFree.size();
            }
            public int getColumnCount() {
                return SALEHEADERS.length;
            }
            public Object getValueAt(int row, int column) {
                SalesLine l = m_lsalesFree.get(row);
                switch (column) {
                case 0: return l.getTaxName();
                case 1: return l.getTaxes();
                default: return null;
                }
            }  
        };
    }
    
    public static class PaymentsLine implements SerializableRead {
        
        private String m_PaymentType;
        private String m_PaymentDescription;
        private Double m_PaymentValue;
        
        public void readValues(DataRead dr) throws BasicException {
            m_PaymentType = dr.getString(1);
            m_PaymentValue = dr.getDouble(2);
            m_PaymentDescription = dr.getString(3);
        }
        
        public String printType() {
            return AppLocal.getIntString("transpayment." + m_PaymentType);
        }
        public String printDescription() {
            return m_PaymentDescription == null ? "" : m_PaymentDescription;
        }
        public String getType() {
            return m_PaymentType;
        }
        public String getDescription() {
            return m_PaymentDescription;
        }
        public String printValue() {
            return Formats.CURRENCY.formatValue(m_PaymentValue);
        }
        public Double getValue() {
            return m_PaymentValue;
        }        
    }
}    