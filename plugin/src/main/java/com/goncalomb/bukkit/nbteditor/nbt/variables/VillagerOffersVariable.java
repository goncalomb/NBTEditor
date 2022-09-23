package com.goncalomb.bukkit.nbteditor.nbt.variables;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.mylib.reflect.NBTTagList;
import com.goncalomb.bukkit.mylib.reflect.NBTUtils;

public class VillagerOffersVariable extends NBTVariable implements SpecialVariable {

	public static class Offer {

		public ItemStack buyA = null;
		public ItemStack buyB = null;
		public ItemStack sell = null;
		public boolean rewardExp = false;
		public int maxUses = Integer.MAX_VALUE;
		public int uses = 0;

		public Offer() { }

		Offer(NBTTagCompound offer) {
			buyA = NBTUtils.itemStackFromNBTData(offer.getCompound("buy"));
			if (offer.hasKey("buyB")) {
				buyB = NBTUtils.itemStackFromNBTData(offer.getCompound("buyB"));
			} else {
				buyB = null;
			}
			sell = NBTUtils.itemStackFromNBTData(offer.getCompound("sell"));
			rewardExp = offer.getByte("rewardExp") != 0;
			maxUses = offer.getInt("maxUses");
			uses = offer.getInt("uses");
		}

		NBTTagCompound getCompound() {
			NBTTagCompound offer = new NBTTagCompound();
			offer.setCompound("buy", NBTUtils.itemStackToNBTData(buyA));
			if (buyB != null && !buyB.getType().equals(Material.AIR)) {
				offer.setCompound("buyB", NBTUtils.itemStackToNBTData(buyB));
			}
			offer.setCompound("sell", NBTUtils.itemStackToNBTData(sell));
			offer.setByte("rewardExp", (byte) (rewardExp ? 1 : 0));
			offer.setInt("maxUses", maxUses);
			offer.setInt("uses", uses);
			return offer;
		}

	}


	public VillagerOffersVariable() {
		super("Offers");
	}

	@Override
	public boolean set(String value, Player player) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String get() {
		NBTTagCompound data = data();
		if (data.hasKey(_key)) {
			NBTTagCompound offers = data.getCompound(_key);
			if (offers.hasKey("Recipes")) {
				return offers.getList("Recipes").size() + " offer(s)";
			}
		}
		return null;
	}

	@Override
	public String getFormat() {
		return "Villager offers.";
	}

	public List<Offer> getOffers() {
		NBTTagCompound data = data();
		ArrayList<Offer> list = new ArrayList<Offer>();
		if (data.hasKey(_key)) {
			NBTTagCompound offers = data.getCompound(_key);
			if (offers.hasKey("Recipes")) {
				Object[] recipes = offers.getListAsArray("Recipes");
				for (Object recipe : recipes) {
					list.add(new Offer((NBTTagCompound) recipe));
				}
			}
		}
		return list;
	}

	public void setOffers(List<Offer> list) {
		NBTTagCompound data = data();
		NBTTagCompound offers = data.getCompound("Offers");
		if (offers == null) {
			offers = new NBTTagCompound();
			data.setCompound("Offers", offers);
		}
		NBTTagList recipes = new NBTTagList();
		for (Offer offer : list) {
			if (offer != null) {
				recipes.add(offer.getCompound());
			}
		}
		offers.setList("Recipes", recipes);
	}

}
