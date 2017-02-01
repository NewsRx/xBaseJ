package org.xBaseJ.fields.ns;

import java.math.BigDecimal;
import java.nio.ByteBuffer;

import org.xBaseJ.xBaseJException;
import org.xBaseJ.fields.CurrencyField;

public class NsCurrencyField extends CurrencyField {

	public NsCurrencyField(String iName, ByteBuffer inBuffer) throws xBaseJException {
		super(iName, inBuffer);
	}

	public NsCurrencyField(String iName) throws xBaseJException {
		super(iName);
	}

	@Override
	public void put(byte[] inBytes) throws xBaseJException {
		super.put(inBytes == null ? new byte[0] : inBytes);
	}

	@Override
	public void put(String inValue) throws xBaseJException {
		super.put(inValue == null ? "" : inValue);
	}

	@Override
	public void put(BigDecimal value) throws xBaseJException {
		if (value == null) {
			super.put("");
			return;
		}
		super.put(value);
	}
}
