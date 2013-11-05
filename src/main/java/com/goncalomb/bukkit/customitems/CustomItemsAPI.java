package com.goncalomb.bukkit.customitems;

import com.goncalomb.bukkit.bkglib.betterplugin.BetterPlugin;
import com.goncalomb.bukkit.customitems.api.CustomItemManager;
import com.goncalomb.bukkit.customitems.commands.CommandCustomItem;
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

public final class CustomItemsAPI extends BetterPlugin {
	
	@Override
	public void onBetterEnable() {
		CustomItemManager manager = CustomItemManager.getInstance(this);
		manager.registerNew(new BatBomb(), this);
		manager.registerNew(new FireBomb(), this);
		manager.registerNew(new RepulsionBomb(), this);
		manager.registerNew(new LightningRod(), this);
		manager.registerNew(new EnderBow(), this);
		manager.registerNew(new WitherBow(), this);
		manager.registerNew(new SunStick(), this);
		manager.registerNew(new MoonStick(), this);
		manager.registerNew(new EscapePlan(), this);
		manager.registerNew(new KingsCrown(), this);
		manager.registerNew(new SimpleMine(), this);
		manager.registerNew(new TorchBow(), this);
		manager.registerNew(new AntiMatterBomb(), this);
		
		registerCommand(new CommandCustomItem(manager));
		
		getLogger().info("CustomItemsAPI has been enabled.");
	}
	
}
