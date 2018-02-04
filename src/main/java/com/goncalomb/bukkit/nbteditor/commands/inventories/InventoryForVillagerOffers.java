package com.goncalomb.bukkit.nbteditor.commands.inventories;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.goncalomb.bukkit.mylib.utils.UtilsMc;
import com.goncalomb.bukkit.nbteditor.nbt.BaseNBT;
import com.goncalomb.bukkit.nbteditor.nbt.variables.VillagerOffersVariable;

public class InventoryForVillagerOffers extends InventoryForSpecialVariable<VillagerOffersVariable> {

	private ItemStack _placeHolderA = createPlaceholder(Material.PAPER, "§6Buy item 1");
	private ItemStack _placeHolderB = createPlaceholder(Material.PAPER, "§6Buy item 2", "§bThis is optional.");
	private ItemStack _placeHolderS = createPlaceholder(Material.PAPER, "§6Sell item");

	private static void setItemStackName(ItemStack item, String name) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
	}

	List<VillagerOffersVariable.Offer> _offers;
	private int _index = 0;
	private int[] _uses = new int[9];
	private int[] _maxUses = new int[9];
	private boolean[] _rewardExp = new boolean[9];
	private int _selected = 0;

	public InventoryForVillagerOffers(Player owner, BaseNBT wrapper, VillagerOffersVariable variable) {
		super(owner, wrapper, variable, 54);
		_offers = _variable.getOffers();
		loadOffers();
		createWoolButtons();
		createPaperButtons(36, "uses");
		createPaperButtons(45, "max uses");
		updatePaperButtons();
		setItem(43, new ItemStack(Material.GLASS_BOTTLE));
		updateBottleButton();
		setItem(44, UtilsMc.newSingleItemStack(Material.ARROW, "[0] Previous Page"));
		setItem(53, UtilsMc.newSingleItemStack(Material.ARROW, "[0] Next Page"));
	}

	private void loadOffers() {
		int l = _offers.size();
		for (int i = 0, k = _index; i < 9; i++, k++) {
			if (k < l) {
				VillagerOffersVariable.Offer offer = _offers.get(i);
				if (offer != null) {
					setItem(i, offer.buyA);
					setItem(9 + i, offer.buyB);
					setItem(18 + i, offer.sell);
					_uses[i] = offer.uses;
					_maxUses[i] = offer.maxUses;
					_rewardExp[i] = offer.rewardExp;
					continue;
				}
			}
			if (i == 8) {
				setPlaceholder(8, _placeHolderA);
				setPlaceholder(17, _placeHolderB);
				setPlaceholder(26, _placeHolderS);
			} else {
				setItem(i, null);
				setItem(9 + i, null);
				setItem(18 + i, null);
			}
			_uses[i] = 0;
			_maxUses[i] = Integer.MAX_VALUE;
			_rewardExp[i] = false;
		}
	}

	private boolean saveOffers(boolean abortOnInvalidOffers) {
		ArrayList<VillagerOffersVariable.Offer> backup = null;
		if (abortOnInvalidOffers) {
			backup = new ArrayList<>(_offers);
		}
		int l = _offers.size();
		for (int i = 0, k = _index; i < 9; i++, k++) {
			ItemStack buyA = getItem(i);
			ItemStack buyB = getItem(9 + i);
			ItemStack sell = getItem(18 + i);
			if (buyA != null && sell != null) {
				VillagerOffersVariable.Offer offer;
				if (k < l) {
					offer = _offers.get(k);
					if (offer == null) {
						offer = new VillagerOffersVariable.Offer();
						_offers.set(k, offer);
					}
				} else {
					offer = new VillagerOffersVariable.Offer();
					_offers.add(offer);
				}
				offer.buyA = buyA;
				offer.buyB = buyB;
				offer.sell = sell;
				offer.uses = _uses[i];
				offer.maxUses = _maxUses[i];
				offer.rewardExp = _rewardExp[i];
			} else if ((buyA != null || buyB != null || sell != null) && abortOnInvalidOffers) {
				_offers = backup;
				return false;
			} else if (k < l) {
				_offers.set(k, null);
			}
		}
		_variable.setOffers(_offers);
		return true;
	}

	private void changeIndex(HumanEntity player, int offset) {
		if (_index + offset >= 0) {
			if (saveOffers(true)) {
				_index += offset;
				loadOffers();
				createWoolButtons();
				updatePaperButtons();
				updateBottleButton();
				int p = _index/9;
				setItemStackName(getItem(44), "[" + p + "] Previous Page");
				setItemStackName(getItem(53), "[" + p + "] Next Page");
			} else {
				player.sendMessage("§eSome offers are invalid. Fix them.");
			}
		}
	}

	private ArrayList<String> createUsesLore(int index) {
		ArrayList<String> lore = new ArrayList<String>();
		lore.add((_uses[index] == Integer.MAX_VALUE ? "\"infinite\"" : _uses[index]) + " uses");
		lore.add((_maxUses[index] == Integer.MAX_VALUE ? "\"infinite\"" : _maxUses[index]) + " max uses");
		return lore;
	}

	private ArrayList<String> createWoolButtonLore(int index) {
		ArrayList<String> lore = createUsesLore(index);
		lore.add("XP reward is " + (_rewardExp[index] ? "ON" : "OFF") + ".");
		return lore;
	}

	private void createWoolButtons() {
		for (int i = 0; i < 9; i++) {
			ItemStack item = new ItemStack(Material.WOOL);
			if (i == _selected) {
				item.setDurability(DyeColor.LIGHT_BLUE.getWoolData());
			}
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("Select offer #" + i + ".");
			meta.setLore(createWoolButtonLore(i));
			item.setItemMeta(meta);
			setItem(27 + i, item);
		}
	}

	private void updateWoolButton() {
		ItemStack item = getItem(27 + _selected);
		ItemMeta meta = item.getItemMeta();
		meta.setLore(createWoolButtonLore(_selected));
		item.setItemMeta(meta);
	}

	private void changeSelected(int selected) {
		getItem(27 + _selected).setDurability(DyeColor.WHITE.getWoolData());
		_selected = selected;
		getItem(27 + _selected).setDurability(DyeColor.LIGHT_BLUE.getWoolData());
		updatePaperButtons();
		updateBottleButton();
	}

	private void createPaperButtons(int firstSlot, String token) {
		for (int i = 0; i < 4; i++) {
			int x = (int) Math.pow(10, i);
			setItem(firstSlot + i, UtilsMc.newSingleItemStack(Material.PAPER, "+" + x + " " + token));
		}
		setItem(firstSlot + 4, UtilsMc.newSingleItemStack(Material.EMPTY_MAP, "x10 " + token));
		setItem(firstSlot + 5, UtilsMc.newSingleItemStack(Material.EMPTY_MAP, "set 0 " + token));
		setItem(firstSlot + 6, UtilsMc.newSingleItemStack(Material.EMPTY_MAP, "set \"infinite\" " + token));
	}

	private void updatePaperButtons() {
		ArrayList<String> lore = createUsesLore(_selected);
		lore.add("Editing offer #" + _selected + ".");
		for (int i = 0; i < 7; i++) {
			ItemStack item = getItem(36 + i);
			ItemMeta meta = item.getItemMeta();
			meta.setLore(lore);
			item.setItemMeta(meta);
		}
		for (int i = 0; i < 7; i++) {
			ItemStack item = getItem(45 + i);
			ItemMeta meta = item.getItemMeta();
			meta.setLore(lore);
			item.setItemMeta(meta);
		}
	}

	public void updateBottleButton() {
		ItemStack item = getItem(43);
		ItemMeta meta = item.getItemMeta();
		if (_rewardExp[_selected]) {
			item.setType(Material.EXP_BOTTLE);
			meta.setDisplayName("XP reward is ON.");
		} else {
			item.setType(Material.GLASS_BOTTLE);
			meta.setDisplayName("XP reward is OFF.");
		}
		item.setItemMeta(meta);
	}

	private void handlePaperButtons(int[] data, int firstSlot, int clickSlot) {
		int value = data[_selected];
		if (clickSlot >= firstSlot && clickSlot < firstSlot + 4) {
			value += (int) Math.pow(10, clickSlot - firstSlot);
		} else if (clickSlot == firstSlot + 4) {
			value *= 10;
		} else if (clickSlot == firstSlot + 5) {
			value = 0;
		} else if (clickSlot == firstSlot + 6) {
			value = Integer.MAX_VALUE;
		}
		if (value < 0) {
			value = Integer.MAX_VALUE;
		}
		if (data[_selected] != value) {
			data[_selected] = value;
			updatePaperButtons();
			updateWoolButton();
		}
	}

	@Override
	protected void inventoryClick(InventoryClickEvent event) {
		super.inventoryClick(event);
		int slot = event.getRawSlot();
		if (slot >= 27 && slot < getInventory().getSize()) {
			event.setCancelled(true);
		}
		if (slot >= 27 && slot < 36) {
			changeSelected(slot - 27);
		} else if (slot == 43) {
			// xp reward
			_rewardExp[_selected] = !_rewardExp[_selected];
			updateBottleButton();
			updateWoolButton();
		} else if (slot == 44) {
			// previous page
			changeIndex(event.getWhoClicked(), -9);
		} else if (slot == 53) {
			// next page
			changeIndex(event.getWhoClicked(), +9);
		} else {
			handlePaperButtons(_uses, 36, slot);
			handlePaperButtons(_maxUses, 45, slot);
		}
	}

	@Override
	protected void inventoryClose(InventoryCloseEvent event) {
		saveOffers(false);
		super.inventoryClose(event);
	}

}
