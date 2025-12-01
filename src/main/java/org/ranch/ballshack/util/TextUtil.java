package org.ranch.ballshack.util;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.function.Supplier;

import static org.ranch.ballshack.util.Formatters.FORMATTERS;

public class TextUtil {

	public static String formatDecimal(double value) {
		DecimalFormat df = new DecimalFormat("#.##");
		return df.format(value);
	}

	public static String applyFormatting(String input) {
		String out = input;
		for (Map.Entry<String, Supplier<String>> e : FORMATTERS.entrySet()) {
			String token = e.getKey();
			String value = safeString(e.getValue().get());
			if (value == null) value = "";
			out = out.replace(token, value);
		}
		return out;
	}

	private static String safeString(Object o) {
		return o == null ? null : String.valueOf(o);
	}
}
