/*
 * Copyright (C) 2013-2018 Gonçalo Baltazar <me@goncalomb.com>
 *
 * This file is part of NBTEditor.
 *
 * NBTEditor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NBTEditor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NBTEditor.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.goncalomb.bukkit.mylib.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.StringUtil;

public final class Utils {

	private Utils() { }

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

	public static List<String> getElementsWithPrefix(Collection<String> values, String prefix) {
		return getElementsWithPrefix(values, prefix, null, false);
	}

	public static List<String> getElementsWithPrefix(Collection<String> values, String prefix, String ignorePrefix, boolean sort) {
		List<String> result;
		if (prefix == null || prefix.isEmpty()) {
			result = new ArrayList<String>(values);
		} else {
			result = new ArrayList<String>();
			for (String string : values) {
				if (StringUtil.startsWithIgnoreCase(string, prefix)) {
					result.add(string);
				} else if (ignorePrefix != null && !ignorePrefix.isEmpty() && StringUtil.startsWithIgnoreCase(string, ignorePrefix)) {
					if (string.regionMatches(ignorePrefix.length(), prefix, 0, prefix.length())) {
						result.add(string);
					}
				}
			}
		}
		if (sort) {
			Collections.sort(result, String.CASE_INSENSITIVE_ORDER);
		}
		return Collections.unmodifiableList(result);
	}

	public static List<String> getElementsWithPrefixGeneric(Collection<?> values, String prefix) {
		return getElementsWithPrefixGeneric(values, prefix, false);
	}

	public static List<String> getElementsWithPrefixGeneric(Collection<?> values, String prefix, boolean sort) {
		List<String> result = new ArrayList<String>(values.size());
		if (prefix == null || prefix.isEmpty()) {
			for (Object obj : values) {
				result.add(obj.toString());
			}
		} else {
			for (Object obj : values) {
				String str = obj.toString();
				if (StringUtil.startsWithIgnoreCase(str, prefix)) {
					result.add(str);
				}
			}
		}
		if (sort) {
			Collections.sort(result, String.CASE_INSENSITIVE_ORDER);
		}
		return Collections.unmodifiableList(result);
	}

	/* This method can be used to wrap code that should only execute once per tick
	 * for a given metadata object (block, entity, etc.).
	 *
	 * If this function returns true, the program should proceed - this is the
	 * first time it has been invoked on this particular tick
	 *
	 * If this function returns false, then it has been called already this tick
	 * for this entity / metakey pair. When returning false, the code should not
	 * be executed.
	 */
	public static boolean checkOnceThisTick(Plugin plugin, Entity entity, String metakey) {
		if (entity.hasMetadata(metakey)
			&& entity.getMetadata(metakey).get(0).asInt() == entity.getTicksLived()) {
			return false;
		}

		entity.setMetadata(metakey, new FixedMetadataValue(plugin, entity.getTicksLived()));
		return true;
	}
}
