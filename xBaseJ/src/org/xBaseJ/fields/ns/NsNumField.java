package org.xBaseJ.fields.ns;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.xBaseJ.xBaseJException;
import org.xBaseJ.fields.NumField;

public class NsNumField extends NumField {

	public NsNumField() {
		// TODO Auto-generated constructor stub
	}

	public NsNumField(String iName, int iLength, int idecPosition, ByteBuffer inBuffer) throws xBaseJException {
		super(iName, iLength, idecPosition, inBuffer);
	}

	public NsNumField(String iName, int iLength, int inDecPosition) throws xBaseJException, IOException {
		super(iName, iLength, inDecPosition);
	}
	
	@Override
	public void put(byte[] inValue) throws xBaseJException {
		super.put(inValue==null?new byte[0]:inValue);
	}
	
	@Override
	public void put(String inValue) throws xBaseJException {
		super.put(inValue==null?"":inValue);
	}

}
