package com.goncalomb.bukkit.nbteditor.nbt;

import java.util.ArrayList;
import java.util.List;

import com.goncalomb.bukkit.bkglib.reflect.NBTTagCompoundWrapper;
import com.goncalomb.bukkit.bkglib.reflect.NBTTagListWrapper;
import com.goncalomb.bukkit.nbteditor.nbt.variable.IntegerVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTGenericVariableContainer;

public class VillagerNBT extends BreedNBT {
	
	private ArrayList<VillagerNBTOffer> _offers;
	
	static {
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("Villager");
		variables.add("profession", new IntegerVariable("Profession", 0, 5));
		EntityNBTVariableManager.registerVariables(VillagerNBT.class, variables);
	}
	
	public void clearOffers() {
		_data.remove("Offers");
		_offers = null;
	}
	
	public void addOffer(VillagerNBTOffer offer) {
		NBTTagCompoundWrapper offers = _data.getCompound("Offers");
		if (offers == null) {
			offers = new NBTTagCompoundWrapper();
			_data.setCompound("Offers", offers);
		}
		NBTTagListWrapper recipes = offers.getList("Recipes");
		if (recipes == null) {
			recipes = new NBTTagListWrapper();
			offers.setList("Recipes", recipes);
		}
		recipes.add(offer.getCompound());
		if (_offers != null) {
			_offers.add(offer);
		}
	}
	
	public List<VillagerNBTOffer> getOffers() {
		if (_offers == null) {
			_offers = new ArrayList<VillagerNBTOffer>();
			if (_data.hasKey("Offers")) {
				NBTTagCompoundWrapper offers = _data.getCompound("Offers");
				if (offers.hasKey("Recipes")) {
					Object[] recipes = offers.getListAsArray("Recipes");
					for (Object recipe : recipes) {
						_offers.add(new VillagerNBTOffer((NBTTagCompoundWrapper)recipe));
					}
				}
			}
		}
		return _offers;
	}
	
}
