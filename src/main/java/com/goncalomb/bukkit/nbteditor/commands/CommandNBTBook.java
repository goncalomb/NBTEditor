package com.goncalomb.bukkit.nbteditor.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.goncalomb.bukkit.bkglib.Lang;
import com.goncalomb.bukkit.bkglib.bkgcommand.BKgCommand;
import com.goncalomb.bukkit.bkglib.bkgcommand.BKgCommandException;
import com.goncalomb.bukkit.bkglib.utils.UtilsMc;
import com.goncalomb.bukkit.nbteditor.NBTEditor;
import com.goncalomb.bukkit.nbteditor.commands.HandItemWrapper.Book.BookType;

public class CommandNBTBook extends BKgCommand {
	
	public CommandNBTBook() {
		super("nbtbook", "nbtb");
	}
	
	@Command(args = "colors", type = CommandType.PLAYER_ONLY)
	public boolean colorsCommand(CommandSender sender, String[] args) throws BKgCommandException {
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
	public boolean titleCommand(CommandSender sender, String[] args) throws BKgCommandException {
		HandItemWrapper.Book item = new HandItemWrapper.Book((Player) sender, BookType.WRITTEN);
		item.meta.setTitle(UtilsMc.parseColors(UtilsMc.parseColors(StringUtils.join(args, " "))));
		item.save();
		sender.sendMessage(Lang._(NBTEditor.class, "commands.nbtbook.title"));
		return true;
	}
	
	@Command(args = "author", type = CommandType.PLAYER_ONLY, minargs = 1, maxargs = Integer.MAX_VALUE, usage = "<author>")
	public boolean authorCommand(CommandSender sender, String[] args) throws BKgCommandException {
		HandItemWrapper.Book item = new HandItemWrapper.Book((Player) sender, BookType.WRITTEN);
		item.meta.setAuthor(UtilsMc.parseColors(UtilsMc.parseColors(StringUtils.join(args, " "))));
		item.save();
		sender.sendMessage(Lang._(NBTEditor.class, "commands.nbtbook.author"));
		return true;
	}
	
	@Command(args = "unsign", type = CommandType.PLAYER_ONLY)
	public boolean unsignCommand(CommandSender sender, String[] args) throws BKgCommandException {
		HandItemWrapper.Book item = new HandItemWrapper.Book((Player) sender, BookType.WRITTEN);
		item.meta.setTitle(null);
		item.meta.setAuthor(null);
		item.item.setType(Material.BOOK_AND_QUILL);
		item.save();
		sender.sendMessage(Lang._(NBTEditor.class, "commands.nbtbook.unsign"));
		return true;
	}
	
}