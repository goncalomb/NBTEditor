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

package com.goncalomb.bukkit.nbteditor;

import java.io.File;
import java.util.logging.Level;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
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
import com.goncalomb.bukkit.mylib.command.MyCommandManager;
import com.goncalomb.bukkit.mylib.reflect.BukkitReflect;
import com.goncalomb.bukkit.mylib.reflect.BukkitVersion;
import com.goncalomb.bukkit.mylib.reflect.NBTBase;
import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.mylib.reflect.NBTTagList;
import com.goncalomb.bukkit.mylib.reflect.NBTUtils;
import com.goncalomb.bukkit.mylib.reflect.NBTTypes;
import com.goncalomb.bukkit.nbteditor.bos.BookOfSouls;
import com.goncalomb.bukkit.nbteditor.commands.CommandBOS;
import com.goncalomb.bukkit.nbteditor.commands.CommandItemStorage;
import com.goncalomb.bukkit.nbteditor.commands.CommandNBTEnchant;
import com.goncalomb.bukkit.nbteditor.commands.CommandNBTItem;
import com.goncalomb.bukkit.nbteditor.commands.CommandNBTPotion;
import com.goncalomb.bukkit.nbteditor.commands.CommandNBTSpawner;
import com.goncalomb.bukkit.nbteditor.commands.CommandNBTTile;
import com.goncalomb.bukkit.nbteditor.nbt.SpawnerNBTWrapper;
import com.goncalomb.bukkit.nbteditor.tools.EntityInspectorTool;
import com.goncalomb.bukkit.nbteditor.tools.EntityRemoverTool;
import com.goncalomb.bukkit.nbteditor.tools.SuperLeadTool;
import com.goncalomb.bukkit.nbteditor.tools.TropicalGeneratorTool;

public final class NBTEditor extends JavaPlugin {

	private static NBTEditor _instance;

	@Override
	public void onEnable() {
		try {
			BukkitReflect.prepareReflection(this.getServer().getClass(), getLogger());
			BukkitVersion.prepareReflection(this.getServer().getClass(), getLogger());
			NBTBase.prepareReflection(this.getServer().getClass(), getLogger());
			NBTTagCompound.prepareReflection(this.getServer().getClass(), getLogger());
			NBTTagList.prepareReflection(this.getServer().getClass(), getLogger());
			NBTTypes.prepareReflection(this.getServer().getClass(), getLogger());
			NBTUtils.prepareReflection(this.getServer().getClass(), getLogger());
			SpawnerNBTWrapper.prepareReflection(this.getServer().getClass(), getLogger());
		} catch (Throwable e) {
			getLogger().log(Level.SEVERE, "Error preparing reflection objects", e);
			getLogger().severe("This version of NBTEditor is not compatible with this version of Bukkit");
			return;
		}

		try {
			new Metrics(this);
		} catch (Exception e) {
			Bukkit.getLogger().log(Level.INFO, "[bStats] " + e.getMessage());
		}

		MyCommandManager.register(new CommandBOS(), this);
		MyCommandManager.register(new CommandNBTSpawner(), this);
		MyCommandManager.register(new CommandNBTItem(), this);
		MyCommandManager.register(new CommandNBTEnchant(), this);
		MyCommandManager.register(new CommandNBTPotion(), this);
		MyCommandManager.register(new CommandNBTTile(), this);

		ItemStorage.setDataFolder(new File(getDataFolder(), "ItemStorage"));
		MyCommandManager.register(new CommandItemStorage(), this);

		BookOfSouls.initialize(this);
		CustomItemManager.register(new EntityInspectorTool(), this, this.getName());
		CustomItemManager.register(new EntityRemoverTool(), this, this.getName());
		CustomItemManager.register(new SuperLeadTool(), this, this.getName());
		CustomItemManager.register(new TropicalGeneratorTool(), this, this.getName());

		saveDefaultConfig();

		if (getConfig().getBoolean("customitems.enable-command", true)) {
			MyCommandManager.register(new CommandCustomItems(), this);
		}

		if (getConfig().getBoolean("customitems.enable-items", false)) {
			CustomItemManager.register(new BatBomb(), this, "Bombs");
			CustomItemManager.register(new FireBomb(), this, "Bombs");
			CustomItemManager.register(new RepulsionBomb(), this, "Bombs");
			CustomItemManager.register(new LightningRod(), this, "Bombs");
			CustomItemManager.register(new EnderBow(), this, "Bows");
			CustomItemManager.register(new WitherBow(), this, "Bows");
			CustomItemManager.register(new SunStick(), this, "Fireworks");
			CustomItemManager.register(new MoonStick(), this, "Fireworks");
			CustomItemManager.register(new EscapePlan(), this, "Fireworks");
			CustomItemManager.register(new KingsCrown(), this, "Misc");
			CustomItemManager.register(new SimpleMine(), this, "Bombs");
			CustomItemManager.register(new TorchBow(), this, "Bows");
			CustomItemManager.register(new AntiMatterBomb(), this, "Bombs");
			CustomItemManager.register(new GravitationalAxe(), this, "Axes");
			CustomItemManager.register(new TreeVaporizer(), this, "Axes");
		}

		_instance = this;
		getLogger().info("NBTEditor has been enabled");
	}

	@Override
	public void onDisable() {
		_instance = null;
	}

	public static NBTEditor getInstance() {
		return _instance;
	}

}
