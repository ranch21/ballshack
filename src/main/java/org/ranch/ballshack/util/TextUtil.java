package org.ranch.ballshack.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static org.ranch.ballshack.util.Formatters.FORMATTERS;

public class TextUtil {

	public static String formatDecimal(double value) {
		return formatDecimal(value, "#.##");
	}

	public static String formatDecimal(double value, String format) {
		DecimalFormat df = new DecimalFormat(format);
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

	public static List<String> wrapTextKeep(String input, int maxLength) {
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

	public static List<String> wrapText(String text, int maxPixelWidth) {
		TextRenderer tr = MinecraftClient.getInstance().textRenderer;
		List<String> out = new ArrayList<>();
		if (text == null || text.isEmpty()) {
			out.add("");
			return out;
		}

		String[] hardLines = text.split("\\n", -1);
		for (String hard : hardLines) {
			if (tr.getWidth(hard) <= maxPixelWidth) {
				out.add(hard);
				continue;
			}
			int start = 0;
			int len = hard.length();
			while (start < len) {
				int end = findWrapPoint(hard, start, maxPixelWidth, tr);
				out.add(hard.substring(start, end));
				start = end;
			}
		}
		return out;
	}

	public static int findWrapPoint(String s, int start, int maxPixelWidth, TextRenderer tr) {
		int end = start;
		int lastSpace = -1;
		while (end < s.length()) {
			char c = s.charAt(end);
			if (Character.isWhitespace(c)) {
				lastSpace = end;
			}
			String candidate = s.substring(start, end + 1);
			if (tr.getWidth(candidate) > maxPixelWidth) {
				if (lastSpace >= start) {
					return Math.max(lastSpace + 1, start + 1);
				}
				return Math.max(end, start + 1);
			}
			end++;
		}
		return end;
	}

	private static String safeString(Object o) {
		return o == null ? null : String.valueOf(o);
	}
}
