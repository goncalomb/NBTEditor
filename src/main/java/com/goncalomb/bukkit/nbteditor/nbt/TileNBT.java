package com.goncalomb.bukkit.nbteditor.nbt;

import java.util.HashMap;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.nbteditor.nbt.variables.BooleanVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.ByteVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.IntegerVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.ItemsVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.LongVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.NBTUnboundVariableContainer;
import com.goncalomb.bukkit.nbteditor.nbt.variables.ShortVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.SingleItemVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.StringVariable;

public class TileNBT extends BaseNBT {

	private static final HashMap<String, NBTUnboundVariableContainer> TILE_VARIABLES = new HashMap<String, NBTUnboundVariableContainer>();

	static {
		NBTUnboundVariableContainer cNameable = new NBTUnboundVariableContainer("Nameable");
		cNameable.add("Name", new StringVariable("CustomName"));

		NBTUnboundVariableContainer cLockable = new NBTUnboundVariableContainer("Lockable", cNameable);
		cLockable.add("Lock", new StringVariable("Lock"));

		NBTUnboundVariableContainer cLootable = new NBTUnboundVariableContainer("Lootable", cLockable);
		cLootable.add("LootTable", new StringVariable("LootTable"));
		cLootable.add("LootTableSeed", new LongVariable("LootTableSeed"));

		//

		NBTUnboundVariableContainer cBanner = new NBTUnboundVariableContainer("Banner", cNameable);
		cBanner.add("BaseColor", new IntegerVariable("Base", 0, 15));
		// Patterns

		NBTUnboundVariableContainer cBeacon = new NBTUnboundVariableContainer("Beacon");
		cBeacon.add("Lock", new StringVariable("Lock"));
		cBeacon.add("Levels", new IntegerVariable("Levels", 0));
		cBeacon.add("Primary", new IntegerVariable("Primary", 0));
		cBeacon.add("Secondary", new IntegerVariable("Secondary", 0));

		NBTUnboundVariableContainer cBed = new NBTUnboundVariableContainer("Bed");
		cBed.add("Color", new IntegerVariable("color", 0, 15));

		NBTUnboundVariableContainer cBrewingStand = new NBTUnboundVariableContainer("BrewingStand", cLockable);
		cBrewingStand.add("Items", new ItemsVariable("Items", new String[] { "Left Item", "Middle Item", "Right Item", "Potion Ingredient", "Fuel" }, true));
		cBrewingStand.add("BrewTime", new IntegerVariable("BrewTime"));
		cBrewingStand.add("Fuel", new ByteVariable("Fuel"));

		NBTUnboundVariableContainer cCommandBlock = new NBTUnboundVariableContainer("CommandBlock", cNameable);
		cCommandBlock.add("Command", new StringVariable("Command"));

		NBTUnboundVariableContainer cJukebox = new NBTUnboundVariableContainer("Jukebox");
		cJukebox.add("RecordItem", new SingleItemVariable("RecordItem"));

		NBTUnboundVariableContainer cSpawner = new NBTUnboundVariableContainer("Spawner");
		cSpawner.add("Count", new ShortVariable("SpawnCount", (short) 0));
		cSpawner.add("Range", new ShortVariable("SpawnRange", (short) 0));
		cSpawner.add("Delay", new ShortVariable("Delay", (short) 0));
		cSpawner.add("MinDelay", new ShortVariable("MinSpawnDelay", (short) 0));
		cSpawner.add("MaxDelay", new ShortVariable("MaxSpawnDelay", (short) 0));
		cSpawner.add("MaxEntities", new ShortVariable("MaxNearbyEntities", (short) 0));
		cSpawner.add("PlayerRange", new ShortVariable("RequiredPlayerRange", (short) 0));

		NBTUnboundVariableContainer cNoteBlock = new NBTUnboundVariableContainer("NoteBlock");
		cNoteBlock.add("Note", new ByteVariable("note", (byte) 0, (byte) 24));
		cNoteBlock.add("Powered", new BooleanVariable("powered"));

		NBTUnboundVariableContainer cSign = new NBTUnboundVariableContainer("Sign");
		cSign.add("Text1", new StringVariable("Text1"));
		cSign.add("Text2", new StringVariable("Text2"));
		cSign.add("Text3", new StringVariable("Text3"));
		cSign.add("Text4", new StringVariable("Text4"));

		NBTUnboundVariableContainer cSkull = new NBTUnboundVariableContainer("Skull");
		cSkull.add("SkullType", new ByteVariable("SkullType"));
		cSkull.add("Rotation", new ByteVariable("Rot"));
		// cSkull.add("OwnerName", new StringVariable("Owner/Name")); // changing this causes network error?

		//

		TILE_VARIABLES.put("minecraft:banner", cBanner);
		TILE_VARIABLES.put("minecraft:beacon", cBeacon);
		TILE_VARIABLES.put("minecraft:bed", cBed);
		TILE_VARIABLES.put("minecraft:brewing_stand", cBrewingStand);
		// TILE_VARIABLES.put("minecraft:cauldron", null);
		TILE_VARIABLES.put("minecraft:chest", cLootable);
		// TILE_VARIABLES.put("minecraft:comparator", null);
		TILE_VARIABLES.put("minecraft:command_block", cCommandBlock);
		// TILE_VARIABLES.put("minecraft:daylight_detector", null);
		TILE_VARIABLES.put("minecraft:dispenser", cLootable);
		TILE_VARIABLES.put("minecraft:dropper", cLootable);
		TILE_VARIABLES.put("minecraft:enchanting_table", cNameable);
		// TILE_VARIABLES.put("minecraft:ender_chest", null);
		// TILE_VARIABLES.put("minecraft:end_gateway", null);
		// TILE_VARIABLES.put("minecraft:end_portal", null);
		// TILE_VARIABLES.put("minecraft:flower_pot", null);
		TILE_VARIABLES.put("minecraft:furnace", cLockable);
		TILE_VARIABLES.put("minecraft:hopper", cLootable);
		TILE_VARIABLES.put("minecraft:jukebox", cJukebox);
		TILE_VARIABLES.put("minecraft:mob_spawner", cSpawner);
		TILE_VARIABLES.put("minecraft:noteblock", cNoteBlock);
		// TILE_VARIABLES.put("minecraft:piston", null);
		TILE_VARIABLES.put("minecraft:sign", cSign);
		// TILE_VARIABLES.put("minecraft:skull", cSkull); // XXX: needs more testing
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
