package at.w4cash.signature;

import java.nio.charset.StandardCharsets;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.SerializableRead;

public class DEPExportTicket implements SerializableRead {
	private String clearText;
	private byte[] sigantureValueBase64;
	private String signatureId;
	private String posId;
	private Integer cashTicketId;
	
	
	@Override
	public void readValues(DataRead dr) throws BasicException {
		clearText = dr.getString(1);
		sigantureValueBase64 = dr.getBytes(2);
        signatureId = dr.getString(3);
        posId = dr.getString(4);
        cashTicketId = dr.getInt(5);
	}

	public String getClearText()
	{
		return clearText;
	}
	
	public String getSignatureValueBase64()
	{
		if(sigantureValueBase64 == null)
			return null;
		
		return new String(sigantureValueBase64, StandardCharsets.UTF_8);
	}
	
	public String getSignatureId()
	{
		return signatureId;
	}
	
	public String getPosId()
	{
		return posId;
	}
	
	public Integer getCashTicketId()
	{
		return cashTicketId;
	}
	
}
