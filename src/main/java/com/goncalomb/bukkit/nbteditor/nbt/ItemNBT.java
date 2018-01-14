package com.goncalomb.bukkit.nbteditor.nbt;

import java.util.HashMap;

import org.bukkit.Material;

import com.goncalomb.bukkit.mylib.namemaps.MaterialMap;
import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.nbteditor.nbt.variables.BooleanVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.ColorVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.IntegerVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.NBTUnboundVariableContainer;
import com.goncalomb.bukkit.nbteditor.nbt.variables.StringListVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.StringVariable;

public class ItemNBT extends BaseNBT {

	private static final HashMap<String, NBTUnboundVariableContainer> ITEM_VARIABLES = new HashMap<String, NBTUnboundVariableContainer>();

	static {
		NBTUnboundVariableContainer cItem = new NBTUnboundVariableContainer("Item");
		cItem.add("Name", new StringVariable("display/Name"));
		cItem.add("LocalizedName", new StringVariable("display/LocName"));
		cItem.add("Lore", new StringListVariable("display/Lore"));
		cItem.add("Unbreakable", new BooleanVariable("Unbreakable"));
		cItem.add("CanDestroy", new StringListVariable("CanDestroy"));
		cItem.add("CanPlaceOn", new StringListVariable("CanPlaceOn"));
		cItem.add("HideFlags", new IntegerVariable("HideFlags"));

		NBTUnboundVariableContainer cLeatherArmor = new NBTUnboundVariableContainer("LeatherArmor", cItem);
		cLeatherArmor.add("Color", new ColorVariable("display/color"));

		NBTUnboundVariableContainer cSkull = new NBTUnboundVariableContainer("Skull", cItem);
		cSkull.add("Owner", new StringVariable("SkullOwner/Name"));

		NBTUnboundVariableContainer cBook = new NBTUnboundVariableContainer("Book", cItem);
		cBook.add("Title", new StringVariable("title"));
		cBook.add("Author", new StringVariable("author"));
		cBook.add("Resolved", new BooleanVariable("resolved"));
		cBook.add("Generation", new IntegerVariable("generation"));

		ITEM_VARIABLES.put("ITEM", cItem);
		ITEM_VARIABLES.put("minecraft:leather_helmet", cLeatherArmor);
		ITEM_VARIABLES.put("minecraft:leather_chestplate", cLeatherArmor);
		ITEM_VARIABLES.put("minecraft:leather_leggings", cLeatherArmor);
		ITEM_VARIABLES.put("minecraft:leather_boots", cLeatherArmor);
		ITEM_VARIABLES.put("minecraft:skull", cSkull);
		ITEM_VARIABLES.put("minecraft:writable_book", cBook);
		ITEM_VARIABLES.put("minecraft:written_book", cBook);
	}

	public ItemNBT(Material type, NBTTagCompound data) {
		super(data, MaterialMap.getName(type));
	}

	@Override
	protected NBTUnboundVariableContainer getVariableContainer(String id) {
		NBTUnboundVariableContainer container = ITEM_VARIABLES.get(id);
		if (container == null) {
			return ITEM_VARIABLES.get("ITEM");
		}
		return container;
	}

}
