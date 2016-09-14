package org.xBaseJ.fields.ns;

import org.xBaseJ.xBaseJException;
import org.xBaseJ.fields.Field;

public class NsField extends Field {

	public NsField() {
	}

	@Override
	public char getType() {
		return 0;
	}
	
	@Override
	public void put(byte[] inBytes) throws xBaseJException {
		super.put(inBytes==null?new byte[0]:inBytes);
	}
	
	@Override
	public void put(String inValue) throws xBaseJException {
		super.put(inValue==null?"":inValue);
	}
}
