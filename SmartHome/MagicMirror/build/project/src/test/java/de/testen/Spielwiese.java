package de.testen;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.junit.Test;

public class Spielwiese {

	@Test
	public void test() throws JSONException {
		
		String test = "\"";
		String res = test.replace("\"", "");
		System.out.println(res);
		System.out.println(res == "");
		System.out.println(StringUtils.isBlank(res));
	}
	
	public void formatTo(double d){

		String res = new DecimalFormat("#", DecimalFormatSymbols.getInstance( Locale.GERMAN )).format(d);
		
		System.out.println(d + " = " + res);
	}

}
