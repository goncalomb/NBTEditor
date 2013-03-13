package com.goncalomb.bukkit;

import java.io.IOException;

import net.iharder.Base64;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public abstract class BookSerialize {
	
	private static String _dataPre = ChatColor.MAGIC.toString();
	
	private String _dataTitle;
	
	protected BookSerialize(String dataTitle) {
		_dataTitle = dataTitle;
	}
	
	protected byte[] loadFromBook(ItemStack book) {
		BookMeta meta = (BookMeta) book.getItemMeta();
		int pageCount = meta.getPageCount();
		
		StringBuilder dataSB = new StringBuilder();
		for (int i = 1; i <= pageCount; ++i) {
			String page = meta.getPage(i);
			if (page.startsWith(_dataTitle)) {
				dataSB.append(page.substring(_dataTitle.length() + _dataPre.length()));
				for (++i; i <= pageCount; ++i) {
					dataSB.append(meta.getPage(i).substring(_dataPre.length()));
				}
				try {
					return Base64.decode(dataSB.toString(), Base64.GZIP);
				} catch (IOException e) {
					throw new Error("Error unserializing book data.", e);
				}
			}
		}
		return null;
	}
	
	protected void saveToBook(ItemStack book, byte[] data) {
		if (book.getType() != Material.WRITTEN_BOOK) throw new IllegalArgumentException("Invalid argument book.");
		
		String dataS;
		try {
			dataS = Base64.encodeBytes(data, Base64.GZIP);
		} catch (IOException e) {
			throw new Error("Error serializing book data.", e);
		}
		
		BookMeta meta = (BookMeta) book.getItemMeta();
		int max;
		int page_max = 255 - _dataPre.length();
		for (int i = 0, l = dataS.length(); i < l; i += max) {
			max = (i == 0 ? page_max - _dataTitle.length() : page_max);
			meta.addPage((i == 0 ? _dataTitle : "") + _dataPre + dataS.substring(i, (i + max > l ? l : i + max)));
		}
		
		book.setItemMeta(meta);
	}
	
}
