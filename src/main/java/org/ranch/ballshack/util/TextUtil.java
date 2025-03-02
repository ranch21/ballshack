package org.ranch.ballshack.util;

import java.text.DecimalFormat;

public class TextUtil {
	public static String formatDecimal(double value) {
		DecimalFormat df = new DecimalFormat("#.##");
		return df.format(value);
	}
}
