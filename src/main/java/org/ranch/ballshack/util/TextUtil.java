package org.ranch.ballshack.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
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

	public static List<String> splitSting(String input, int maxLength) {
		List<StringBuilder> lines = new ArrayList<>();
		int line = 0;
		for (String word : input.split(" ")) {
			if (line >= lines.size()) lines.add(new StringBuilder());
			lines.get(line).append(word).append(" ");
			if (lines.get(line).length() + word.length() > maxLength) line++;
		}
		List<String> result = new ArrayList<>();
		lines.forEach(l -> result.add(l.toString()));
		return result;
	}

	private static String safeString(Object o) {
		return o == null ? null : String.valueOf(o);
	}
}
