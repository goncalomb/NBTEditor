package com.goncalomb.bukkit.customitems;

import org.bukkit.plugin.java.JavaPlugin;

import com.goncalomb.bukkit.bkglib.BKgLib;
import com.goncalomb.bukkit.customitems.api.CustomItemManager;
import com.goncalomb.bukkit.customitems.commands.CommandCustomItems;
import com.goncalomb.bukkit.customitems.items.AntiMatterBomb;
import com.goncalomb.bukkit.customitems.items.BatBomb;
import com.goncalomb.bukkit.customitems.items.EnderBow;
import com.goncalomb.bukkit.customitems.items.EscapePlan;
import com.goncalomb.bukkit.customitems.items.FireBomb;
import com.goncalomb.bukkit.customitems.items.KingsCrown;
import com.goncalomb.bukkit.customitems.items.LightningRod;
import com.goncalomb.bukkit.customitems.items.MoonStick;
import com.goncalomb.bukkit.customitems.items.RepulsionBomb;
import com.goncalomb.bukkit.customitems.items.SimpleMine;
import com.goncalomb.bukkit.customitems.items.SunStick;
import com.goncalomb.bukkit.customitems.items.TorchBow;
import com.goncalomb.bukkit.customitems.items.WitherBow;

public final class CustomItemsAPI extends JavaPlugin {
	
	@Override
	public void onEnable() {
		BKgLib.bind(this);
		
		CustomItemManager.register(new BatBomb(), this);
		CustomItemManager.register(new FireBomb(), this);
		CustomItemManager.register(new RepulsionBomb(), this);
		CustomItemManager.register(new LightningRod(), this);
		CustomItemManager.register(new EnderBow(), this);
		CustomItemManager.register(new WitherBow(), this);
		CustomItemManager.register(new SunStick(), this);
		CustomItemManager.register(new MoonStick(), this);
		CustomItemManager.register(new EscapePlan(), this);
		CustomItemManager.register(new KingsCrown(), this);
		CustomItemManager.register(new SimpleMine(), this);
		CustomItemManager.register(new TorchBow(), this);
		CustomItemManager.register(new AntiMatterBomb(), this);
		
		BKgLib.registerCommand(new CommandCustomItems(), this);
		
		getLogger().info("CustomItemsAPI has been enabled.");
	}
	
	@Override
	public void onDisable() {
		BKgLib.unbind(this);
	}
	
}
