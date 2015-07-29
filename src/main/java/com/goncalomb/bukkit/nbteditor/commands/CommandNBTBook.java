/*
 * Copyright (C) 2013-2015 Gonçalo Baltazar <me@goncalomb.com>
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

package com.goncalomb.bukkit.nbteditor.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.goncalomb.bukkit.mylib.Lang;
import com.goncalomb.bukkit.mylib.command.MyCommand;
import com.goncalomb.bukkit.mylib.command.MyCommandException;
import com.goncalomb.bukkit.mylib.utils.UtilsMc;
import com.goncalomb.bukkit.nbteditor.NBTEditor;
import com.goncalomb.bukkit.nbteditor.commands.HandItemWrapper.Book.BookType;

public class CommandNBTBook extends MyCommand {
	
	public CommandNBTBook() {
		super("nbtbook", "nbtb");
	}
	
	@Command(args = "colors", type = CommandType.PLAYER_ONLY)
	public boolean colorsCommand(CommandSender sender, String[] args) throws MyCommandException {
		HandItemWrapper.Book item = new HandItemWrapper.Book((Player) sender, BookType.BOTH);
		if (item.meta.hasTitle()) {
			item.meta.setTitle(UtilsMc.parseColors(item.meta.getTitle()));
		}
		if (item.meta.hasAuthor()) {
			item.meta.setAuthor(UtilsMc.parseColors(item.meta.getAuthor()));
		}
		for (int i = 1, l = item.meta.getPageCount(); i <= l; ++i) {
			item.meta.setPage(i, UtilsMc.parseColors(item.meta.getPage(i)));
		}
		item.save();
		sender.sendMessage(Lang._(NBTEditor.class, "commands.nbtbook.colors"));
		return true;
	}
	
	@Command(args = "title", type = CommandType.PLAYER_ONLY, minargs = 1, maxargs = Integer.MAX_VALUE, usage = "<title>")
	public boolean titleCommand(CommandSender sender, String[] args) throws MyCommandException {
		HandItemWrapper.Book item = new HandItemWrapper.Book((Player) sender, BookType.WRITTEN);
		item.meta.setTitle(UtilsMc.parseColors(UtilsMc.parseColors(StringUtils.join(args, " "))));
		item.save();
		sender.sendMessage(Lang._(NBTEditor.class, "commands.nbtbook.title"));
		return true;
	}
	
	@Command(args = "author", type = CommandType.PLAYER_ONLY, minargs = 1, maxargs = Integer.MAX_VALUE, usage = "<author>")
	public boolean authorCommand(CommandSender sender, String[] args) throws MyCommandException {
		HandItemWrapper.Book item = new HandItemWrapper.Book((Player) sender, BookType.WRITTEN);
		item.meta.setAuthor(UtilsMc.parseColors(UtilsMc.parseColors(StringUtils.join(args, " "))));
		item.save();
		sender.sendMessage(Lang._(NBTEditor.class, "commands.nbtbook.author"));
		return true;
	}
	
	@Command(args = "unsign", type = CommandType.PLAYER_ONLY)
	public boolean unsignCommand(CommandSender sender, String[] args) throws MyCommandException {
		HandItemWrapper.Book item = new HandItemWrapper.Book((Player) sender, BookType.WRITTEN);
		item.meta.setTitle(null);
		item.meta.setAuthor(null);
		item.item.setType(Material.BOOK_AND_QUILL);
		item.save();
		sender.sendMessage(Lang._(NBTEditor.class, "commands.nbtbook.unsign"));
		return true;
	}
	
}