package com.goncalomb.bukkit.nbteditor.nbt.variables;

import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.mylib.reflect.NBTTagList;
import com.goncalomb.bukkit.mylib.reflect.NBTUtils;

public class EffectsVariable extends PotionVariable {

	public EffectsVariable(String key) {
		super(key);
	}

	@Override
	public String getFormat() {
		return "Effects from a potion.";
	}

	@Override
	public void setItem(ItemStack item) {
		if (item == null) {
			clear();
		} else {
			NBTTagList effects = NBTUtils.potionToNBTEffectsList(item);
			if (effects != null) {
				data().setList(_key, effects);
			}
		}
	}

	@Override
	public ItemStack getItem() {
		NBTTagCompound data = data();
		if (data.hasKey(_key)) {
			return NBTUtils.potionFromNBTEffectsList(data.getList(_key));
		}
		return null;
	}

}
