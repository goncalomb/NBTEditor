package com.goncalomb.bukkit.nbteditor.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.goncalomb.bukkit.nbteditor.commands.HandItemWrapper.Book.BookType;
import com.goncalomb.bukkit.UtilsMc;
import com.goncalomb.bukkit.betterplugin.BetterCommand;
import com.goncalomb.bukkit.betterplugin.BetterCommandException;
import com.goncalomb.bukkit.betterplugin.BetterSubCommandType;
import com.goncalomb.bukkit.lang.Lang;

public class CommandNBTBook extends BetterCommand {
	
	public CommandNBTBook() {
		super("nbtbook", "nbteditor.book");
		setAlises("nbtb");
		setDescription(Lang._("nbt.cmds.nbtb.description"));
	}
	
	@SubCommand(args = "colors", type = BetterSubCommandType.PLAYER_ONLY)
	public boolean colorsCommand(CommandSender sender, String[] args) throws BetterCommandException {
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
		sender.sendMessage(Lang._("nbt.cmd.nbtb.colors"));
		return true;
	}
	
	@SubCommand(args = "title", type = BetterSubCommandType.PLAYER_ONLY, minargs = 1, maxargs = Integer.MAX_VALUE, usage = "<title>")
	public boolean titleCommand(CommandSender sender, String[] args) throws BetterCommandException {
		HandItemWrapper.Book item = new HandItemWrapper.Book((Player) sender, BookType.WRITTEN);
		item.meta.setTitle(UtilsMc.parseColors(UtilsMc.parseColors(StringUtils.join(args, " "))));
		item.save();
		sender.sendMessage(Lang._("nbt.cmd.nbtb.title"));
		return true;
	}
	
	@SubCommand(args = "author", type = BetterSubCommandType.PLAYER_ONLY, minargs = 1, maxargs = Integer.MAX_VALUE, usage = "<author>")
	public boolean authorCommand(CommandSender sender, String[] args) throws BetterCommandException {
		HandItemWrapper.Book item = new HandItemWrapper.Book((Player) sender, BookType.WRITTEN);
		item.meta.setAuthor(UtilsMc.parseColors(UtilsMc.parseColors(StringUtils.join(args, " "))));
		item.save();
		sender.sendMessage(Lang._("nbt.cmd.nbtb.author"));
		return true;
	}
	
	@SubCommand(args = "unsign", type = BetterSubCommandType.PLAYER_ONLY)
	public boolean unsignCommand(CommandSender sender, String[] args) throws BetterCommandException {
		HandItemWrapper.Book item = new HandItemWrapper.Book((Player) sender, BookType.WRITTEN);
		item.meta.setTitle(null);
		item.meta.setAuthor(null);
		item.item.setType(Material.BOOK_AND_QUILL);
		item.save();
		sender.sendMessage(Lang._("nbt.cmd.nbtb.unsign"));
		return true;
	}
	
}