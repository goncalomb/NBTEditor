package com.goncalomb.bukkit.nbteditor.nbt;

import java.util.HashMap;

import org.bukkit.Material;

import com.goncalomb.bukkit.mylib.namemaps.MaterialMap;
import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.nbteditor.nbt.variables.BooleanVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.ColorVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.IntegerVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.MaterialListVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.NBTUnboundVariableContainer;
import com.goncalomb.bukkit.nbteditor.nbt.variables.RawJsonVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.StringListVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.StringVariable;

public class ItemNBT extends BaseNBT {

	private static final HashMap<String, NBTUnboundVariableContainer> ITEM_VARIABLES = new HashMap<String, NBTUnboundVariableContainer>();

	static {
		NBTUnboundVariableContainer cItem = new NBTUnboundVariableContainer("Item");
		cItem.add("Name", new RawJsonVariable("display/Name"));
		cItem.add("Lore", new StringListVariable("display/Lore"));
		cItem.add("Damage", new IntegerVariable("Damage"));
		cItem.add("Unbreakable", new BooleanVariable("Unbreakable"));
		cItem.add("CanDestroy", new MaterialListVariable("CanDestroy"));
		cItem.add("CanPlaceOn", new MaterialListVariable("CanPlaceOn"));
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

		NBTUnboundVariableContainer cPotion = new NBTUnboundVariableContainer("PotionBased", cItem);
		cPotion.add("CustomPotionColor", new ColorVariable("CustomPotionColor"));

		ITEM_VARIABLES.put("ITEM", cItem);
		ITEM_VARIABLES.put("minecraft:leather_helmet", cLeatherArmor);
		ITEM_VARIABLES.put("minecraft:leather_chestplate", cLeatherArmor);
		ITEM_VARIABLES.put("minecraft:leather_leggings", cLeatherArmor);
		ITEM_VARIABLES.put("minecraft:leather_boots", cLeatherArmor);
		ITEM_VARIABLES.put("minecraft:skull", cSkull);
		ITEM_VARIABLES.put("minecraft:writable_book", cBook);
		ITEM_VARIABLES.put("minecraft:written_book", cBook);
		ITEM_VARIABLES.put("minecraft:potion", cPotion);
		ITEM_VARIABLES.put("minecraft:lingering_potion", cPotion);
		ITEM_VARIABLES.put("minecraft:splash_potion", cPotion);
		ITEM_VARIABLES.put("minecraft:tipped_arrow", cPotion);
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
