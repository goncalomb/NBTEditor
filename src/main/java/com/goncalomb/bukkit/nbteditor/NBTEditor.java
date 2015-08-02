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

package com.goncalomb.bukkit.nbteditor;

import java.io.File;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import com.goncalomb.bukkit.customitems.api.CustomItemManager;
import com.goncalomb.bukkit.customitems.commands.CommandCustomItems;
import com.goncalomb.bukkit.customitems.items.AntiMatterBomb;
import com.goncalomb.bukkit.customitems.items.BatBomb;
import com.goncalomb.bukkit.customitems.items.EnderBow;
import com.goncalomb.bukkit.customitems.items.EscapePlan;
import com.goncalomb.bukkit.customitems.items.FireBomb;
import com.goncalomb.bukkit.customitems.items.GravitationalAxe;
import com.goncalomb.bukkit.customitems.items.KingsCrown;
import com.goncalomb.bukkit.customitems.items.LightningRod;
import com.goncalomb.bukkit.customitems.items.MoonStick;
import com.goncalomb.bukkit.customitems.items.RepulsionBomb;
import com.goncalomb.bukkit.customitems.items.SimpleMine;
import com.goncalomb.bukkit.customitems.items.SunStick;
import com.goncalomb.bukkit.customitems.items.TorchBow;
import com.goncalomb.bukkit.customitems.items.TreeVaporizer;
import com.goncalomb.bukkit.customitems.items.WitherBow;
import com.goncalomb.bukkit.mylib.Lang;
import com.goncalomb.bukkit.mylib.command.MyCommandManager;
import com.goncalomb.bukkit.mylib.reflect.BukkitReflect;
import com.goncalomb.bukkit.mylib.reflect.NBTBase;
import com.goncalomb.bukkit.nbteditor.bos.BookOfSouls;
import com.goncalomb.bukkit.nbteditor.commands.CommandBOS;
import com.goncalomb.bukkit.nbteditor.commands.CommandItemStorage;
import com.goncalomb.bukkit.nbteditor.commands.CommandNBTArmor;
import com.goncalomb.bukkit.nbteditor.commands.CommandNBTBook;
import com.goncalomb.bukkit.nbteditor.commands.CommandNBTEnchant;
import com.goncalomb.bukkit.nbteditor.commands.CommandNBTHead;
import com.goncalomb.bukkit.nbteditor.commands.CommandNBTItem;
import com.goncalomb.bukkit.nbteditor.commands.CommandNBTPotion;
import com.goncalomb.bukkit.nbteditor.commands.CommandNBTSpawner;
import com.goncalomb.bukkit.nbteditor.commands.CommandNBTTile;
import com.goncalomb.bukkit.nbteditor.tools.EntityInspectorTool;
import com.goncalomb.bukkit.nbteditor.tools.EntityRemoverTool;
import com.goncalomb.bukkit.nbteditor.tools.SuperLeadTool;

public final class NBTEditor extends JavaPlugin {
	
	private static NBTEditor _instance;
	
	@Override
	public void onEnable() {
		try {
			BukkitReflect.prepareReflection();
			NBTBase.prepareReflection();
		} catch (Throwable e) {
			getLogger().log(Level.SEVERE, "Error preparing reflection objects. This means that this version of NBTEditor is not compatible with this version of Bukkit.", e);
			getLogger().warning("NBTEditor version not compatible with this version of Bukkit. Please install the apropriated version.");
			return;
		}
		
		Lang.load(this);
		
		MyCommandManager.register(new CommandBOS(), this);
		MyCommandManager.register(new CommandNBTSpawner(), this);
		MyCommandManager.register(new CommandNBTItem(), this);
		MyCommandManager.register(new CommandNBTEnchant(), this);
		MyCommandManager.register(new CommandNBTBook(), this);
		MyCommandManager.register(new CommandNBTPotion(), this);
		MyCommandManager.register(new CommandNBTArmor(), this);
		MyCommandManager.register(new CommandNBTHead(), this);
		MyCommandManager.register(new CommandNBTTile(), this);
		
		ItemStorage.setDataFolder(new File(getDataFolder(), "ItemStorage"));
		MyCommandManager.register(new CommandItemStorage(), this);
		
		BookOfSouls.initialize(this);
		CustomItemManager.register(new EntityInspectorTool(), this, "nbteditor");
		CustomItemManager.register(new EntityRemoverTool(), this, "nbteditor");
		CustomItemManager.register(new SuperLeadTool(), this, "nbteditor");
		
		this.saveDefaultConfig();
		
		if (getConfig().getBoolean("enable-customitems", false)) {
			CustomItemManager.register(new BatBomb(), this, "bombs");
			CustomItemManager.register(new FireBomb(), this, "bombs");
			CustomItemManager.register(new RepulsionBomb(), this, "bombs");
			CustomItemManager.register(new LightningRod(), this, "bombs");
			CustomItemManager.register(new EnderBow(), this, "bows");
			CustomItemManager.register(new WitherBow(), this, "bows");
			CustomItemManager.register(new SunStick(), this, "fireworks");
			CustomItemManager.register(new MoonStick(), this, "fireworks");
			CustomItemManager.register(new EscapePlan(), this, "fireworks");
			CustomItemManager.register(new KingsCrown(), this, "misc");
			CustomItemManager.register(new SimpleMine(), this, "bombs");
			CustomItemManager.register(new TorchBow(), this, "bows");
			CustomItemManager.register(new AntiMatterBomb(), this, "bombs");
			CustomItemManager.register(new GravitationalAxe(), this, "axes");
			CustomItemManager.register(new TreeVaporizer(), this, "axes");
			
			MyCommandManager.register(new CommandCustomItems(), this);
			getLogger().info("CustomItems enabled.");
		}
		
		_instance = this;
		getLogger().info("NBTEditor has been enabled.");
	}
	
	@Override
	public void onDisable() {
		Lang.unload(this);
		_instance = null;
	}
	
	public static NBTEditor getInstance() {
		return _instance;
	}
	
}
