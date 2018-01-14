package com.goncalomb.bukkit.nbteditor.nbt;

import java.util.HashMap;

import org.bukkit.Material;

import com.goncalomb.bukkit.mylib.namemaps.MaterialMap;
import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.nbteditor.nbt.variables.BooleanVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.NBTUnboundVariableContainer;

public class ItemNBT extends BaseNBT {

	private static final HashMap<String, NBTUnboundVariableContainer> ITEM_VARIABLES = new HashMap<String, NBTUnboundVariableContainer>();

	static {
		NBTUnboundVariableContainer cItem = new NBTUnboundVariableContainer("Item");
		cItem.add("Unbreakable", new BooleanVariable("Unbreakable"));
		// CanDestroy

		NBTUnboundVariableContainer cBlock = new NBTUnboundVariableContainer("Block", cItem);
		// CanPlaceOn

		ITEM_VARIABLES.put("ITEM", cItem);
		ITEM_VARIABLES.put("BLOCK", cBlock);
	}

	public ItemNBT(Material type, NBTTagCompound data) {
		super(data, MaterialMap.getName(type));
	}

	@Override
	protected NBTUnboundVariableContainer getVariableContainer(String id) {
		NBTUnboundVariableContainer container = ITEM_VARIABLES.get(id);
		if (container == null) {
			Material material = MaterialMap.getByName(id);
			if (material != null) {
				if (material.isBlock()) {
					return ITEM_VARIABLES.get("BLOCK");
				} else {
					return ITEM_VARIABLES.get("ITEM");
				}
			}
		}
		return null;
	}

}
