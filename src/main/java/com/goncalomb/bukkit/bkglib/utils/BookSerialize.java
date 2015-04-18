/*
 * Copyright (C) 2013 - Gonçalo Baltazar <http://goncalomb.com>
 *
 * This file is part of BKgLib.
 *
 * BKgLib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BKgLib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BKgLib.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.goncalomb.bukkit.bkglib.utils;

import org.bukkit.ChatColor;
import org.bukkit.inventory.meta.BookMeta;

public abstract class BookSerialize {
	
	private static final String _dataPre = ChatColor.MAGIC.toString();
	
	private BookSerialize() { }
	
	public static String loadData(BookMeta meta, String dataTitle) {
		int pageCount = meta.getPageCount();
		if (pageCount == 0) {
			return null;
		}
		
		StringBuilder dataSB = new StringBuilder();
		for (int i = 1; i <= pageCount; ++i) {
			String page = meta.getPage(i);
			if (page.startsWith(dataTitle)) {
				dataSB.append(page.substring(dataTitle.length() + _dataPre.length()));
				for (++i; i <= pageCount; ++i) {
					page = meta.getPage(i);
					if (page.startsWith(_dataPre)) {
						dataSB.append(page.substring(_dataPre.length()));
					} else {
						break;
					}
				}
				return dataSB.toString();
			}
		}
		return null;
	}
	
	public static void saveToBook(BookMeta meta, String data, String dataTitle) {
		int max;
		int pageMax = 255 - _dataPre.length();
		for (int i = 0, l = data.length(); i < l; i += max) {
			max = (i == 0 ? pageMax - dataTitle.length() : pageMax);
			meta.addPage((i == 0 ? dataTitle : "") + _dataPre + data.substring(i, (i + max > l ? l : i + max)));
		}
	}
	
}
