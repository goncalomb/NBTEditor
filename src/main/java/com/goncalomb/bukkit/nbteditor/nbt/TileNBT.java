package com.goncalomb.bukkit.nbteditor.nbt;

import java.util.HashMap;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.nbteditor.nbt.variables.NBTUnboundVariableContainer;
import com.goncalomb.bukkit.nbteditor.nbt.variables.StringVariable;

public class TileNBT extends BaseNBT {

	private static final HashMap<String, NBTUnboundVariableContainer> TILE_VARIABLES = new HashMap<String, NBTUnboundVariableContainer>();

	static {
		NBTUnboundVariableContainer cLockable = new NBTUnboundVariableContainer("Lockable");
		cLockable.add("Lock", new StringVariable("Lock"));

		NBTUnboundVariableContainer cNameable = new NBTUnboundVariableContainer("Nameable", cLockable);
		cNameable.add("Name", new StringVariable("CustomName"));

		//

		NBTUnboundVariableContainer cBeacon = new NBTUnboundVariableContainer("Beacon", cLockable);
		// Levels
		// Primary
		// Secundary

		NBTUnboundVariableContainer cCommandBlock = new NBTUnboundVariableContainer("CommandBlock");
		cCommandBlock.add("Name", new StringVariable("CustomName"));

		//

		TILE_VARIABLES.put("minecraft:banner", cNameable);
		TILE_VARIABLES.put("minecraft:beacon", cBeacon);
		// TILE_VARIABLES.put("minecraft:bed", null);
		TILE_VARIABLES.put("minecraft:brewing_stand", cNameable);
		// TILE_VARIABLES.put("minecraft:cauldron", null);
		TILE_VARIABLES.put("minecraft:chest", cNameable);
		// TILE_VARIABLES.put("minecraft:comparator", null);
		TILE_VARIABLES.put("minecraft:command_block", cCommandBlock);
		// TILE_VARIABLES.put("minecraft:daylight_detector", null);
		TILE_VARIABLES.put("minecraft:dispenser", cNameable);
		TILE_VARIABLES.put("minecraft:dropper", cNameable);
		TILE_VARIABLES.put("minecraft:enchanting_table", cNameable);
		// TILE_VARIABLES.put("minecraft:ender_chest", null);
		// TILE_VARIABLES.put("minecraft:end_gateway", null);
		// TILE_VARIABLES.put("minecraft:end_portal", null);
		// TILE_VARIABLES.put("minecraft:flower_pot", null);
		TILE_VARIABLES.put("minecraft:furnace", cNameable);
		TILE_VARIABLES.put("minecraft:hopper", cNameable);
		// TILE_VARIABLES.put("minecraft:jukebox", null);
		// TILE_VARIABLES.put("minecraft:mob_spawner", null);
		// TILE_VARIABLES.put("minecraft:noteblock", null);
		// TILE_VARIABLES.put("minecraft:piston", null);
		// TILE_VARIABLES.put("minecraft:sign", null);
		// TILE_VARIABLES.put("minecraft:skull", null);
		// TILE_VARIABLES.put("minecraft:structure_block", null);
	}

	public TileNBT(NBTTagCompound data) {
		super(data);
	}

	@Override
	protected NBTUnboundVariableContainer getVariableContainer(String id) {
		return TILE_VARIABLES.get(id);
	}

}
