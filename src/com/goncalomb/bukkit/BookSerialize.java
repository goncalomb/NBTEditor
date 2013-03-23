package com.goncalomb.bukkit;

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
					dataSB.append(meta.getPage(i).substring(_dataPre.length()));
				}
				return dataSB.toString();
			}
		}
		return null;
	}
	
	public static void saveToBook(BookMeta meta, String data, String dataTitle) {
		int max;
		int page_max = 255 - _dataPre.length();
		for (int i = 0, l = data.length(); i < l; i += max) {
			max = (i == 0 ? page_max - dataTitle.length() : page_max);
			meta.addPage((i == 0 ? dataTitle : "") + _dataPre + data.substring(i, (i + max > l ? l : i + max)));
		}
	}
	
}
