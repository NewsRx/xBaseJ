package org.xBaseJ.fields.ns;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Date;

import org.xBaseJ.xBaseJException;
import org.xBaseJ.fields.DateField;

public class NsDateField extends DateField {

	public NsDateField(String iName, ByteBuffer inBuffer) throws xBaseJException {
		super(iName, inBuffer);
	}

	public NsDateField(String iName) throws IOException, xBaseJException {
		super(iName);
	}
	
	@Override
	public void put(byte[] inBytes) throws xBaseJException {
		super.put(inBytes==null?new byte[0]:inBytes);
	}
	
	@Override
	public void put(String inValue) throws xBaseJException {
		super.put(inValue==null?"":inValue);
	}
	
	@Override
	public void put(Calendar inValue) throws xBaseJException {
		if (inValue==null) {
			super.put("");
			return;
		}
		super.put(inValue);
	}
	
	@Override
	public void put(Date inValue) throws xBaseJException {
		if (inValue==null) {
			super.put("");
			return;
		}
		super.put(inValue);
	}

}
