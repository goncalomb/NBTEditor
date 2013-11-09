package com.goncalomb.bukkit.bkglib.namemaps;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.bukkit.enchantments.Enchantment;

public final class EnchantmentsMap {
	
	private final static HashMap<String, Enchantment> _enchantmentNames = new HashMap<String, Enchantment>();
	private final static String _enchantmentStringList;
	
	static {
		_enchantmentNames.put("protection", Enchantment.PROTECTION_ENVIRONMENTAL);
		_enchantmentNames.put("fire-protection", Enchantment.PROTECTION_FIRE);
		_enchantmentNames.put("feather-falling", Enchantment.PROTECTION_FALL);
		_enchantmentNames.put("blast-protection", Enchantment.PROTECTION_EXPLOSIONS);
		_enchantmentNames.put("projectile-protection", Enchantment.PROTECTION_PROJECTILE);
		_enchantmentNames.put("respiration", Enchantment.OXYGEN);
		_enchantmentNames.put("aqua-affinity", Enchantment.WATER_WORKER);
		_enchantmentNames.put("thorns", Enchantment.THORNS);
		_enchantmentNames.put("unbreaking", Enchantment.DURABILITY);
		_enchantmentNames.put("sharpness", Enchantment.DAMAGE_ALL);
		_enchantmentNames.put("smite", Enchantment.DAMAGE_UNDEAD);
		_enchantmentNames.put("bane-of-arthropods", Enchantment.DAMAGE_ARTHROPODS);
		_enchantmentNames.put("knockback", Enchantment.KNOCKBACK);
		_enchantmentNames.put("fire-aspect", Enchantment.FIRE_ASPECT);
		_enchantmentNames.put("looting", Enchantment.LOOT_BONUS_MOBS);
		_enchantmentNames.put("efficiency", Enchantment.DIG_SPEED);
		_enchantmentNames.put("silk-touch", Enchantment.SILK_TOUCH);
		_enchantmentNames.put("fortune", Enchantment.LOOT_BONUS_BLOCKS);
		_enchantmentNames.put("power", Enchantment.ARROW_DAMAGE);
		_enchantmentNames.put("punch", Enchantment.ARROW_KNOCKBACK);
		_enchantmentNames.put("flame", Enchantment.ARROW_FIRE);
		_enchantmentNames.put("infinity", Enchantment.ARROW_INFINITE);
		
		_enchantmentStringList = StringUtils.join(_enchantmentNames.keySet(), ", ");
	}
	
	private EnchantmentsMap() { }
	
	public static Enchantment getByName(String name) {
		return _enchantmentNames.get(name.toLowerCase());
	}
	
	public static String getStringList() {
		return _enchantmentStringList;
	}
	
}
