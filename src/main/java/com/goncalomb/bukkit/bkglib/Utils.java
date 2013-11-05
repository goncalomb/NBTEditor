package com.goncalomb.bukkit.bkglib;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Utils {
	
	public enum SplitType {
		WHITE_SPACES("\\s+"),
		COMMAS("\\s*,\\s*");
		
		String _regex;
		
		private SplitType(String regex) {
			_regex = regex;
		}
	}
	
	private Utils() { }
	
	public static String[] split(String str, SplitType type) {
		String[] result = str.trim().split(type._regex);
		return (result.length == 1 && result[0].isEmpty() ? new String[] { } : result);
	}
	
	public static int parseInt(String str, int defaultValue) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
	public static int parseInt(String str, int max, int min, int defaultValue) {
		int value = parseInt(str, defaultValue);
		return (value > max || value < min ? defaultValue : value);
	}
	
	public static int parseTimeDuration(String str) {
		Matcher matcher = Pattern.compile("^(?:(\\d{1,4})d)?(?:(\\d{1,2})h)?(?:(\\d{1,2})m)?(?:(\\d{1,2})s)?$", Pattern.CASE_INSENSITIVE).matcher(str);
		if (matcher.find()) {
			int d = Utils.parseInt(matcher.group(1), 0);
			int h = Utils.parseInt(matcher.group(2), 0);
			int m = Utils.parseInt(matcher.group(3), 0);
			int s = Utils.parseInt(matcher.group(4), 0);
			if (h < 24 && m < 60 && s < 60) {
				return d*86400 + h*3600 + m*60 + s;
			}
		}
		return -1;
	}
	
}
