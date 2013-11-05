package com.goncalomb.bukkit.bkglib.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

public final class NBTUtils {
	
	// Minecraft's ItemStack Class;
	private static Method _createStack;
	private static Method _save;
	private static Method _getTag;
	private static Method _setTag;
	
	// CraftItemStack Class;
	private static Method _asBukkitCopy;
	private static Method _asNMSCopy;
	private static Field _handle;
	
	// Minecraft's Entity Class;
	private static Method _e0; // Save data to NBTTagCompound.
	private static Method _f0; // Load data from NBTTagCompound (generic data).
	private static Method _a0; // Load data from NBTTagCompound (entity specific data), this is a protected method.
	
	// CraftEntity Class
	private static Method _getHandle;
	
	// Minecraft's TileEntity
	private static Method _b1; // Save data to NBTTagCompound.
	private static Method _a1; // Load data from NBTTagCompound.
	
	// CraftWorld
	private static Method _getTileEntity;
	
	static void prepareReflection() throws SecurityException, NoSuchMethodException, NoSuchFieldException {
		Class<?> nbtTagCompoundClass = BukkitReflect.getMinecraftClass("NBTTagCompound");
		
		Class<?> minecraftItemStackClass = BukkitReflect.getMinecraftClass("ItemStack");
		_createStack = minecraftItemStackClass.getMethod("createStack", nbtTagCompoundClass);
		_save = minecraftItemStackClass.getMethod("save", nbtTagCompoundClass);
		_getTag = minecraftItemStackClass.getMethod("getTag");
		_setTag = minecraftItemStackClass.getMethod("setTag", nbtTagCompoundClass);
		
		Class<?> craftItemStackClass = BukkitReflect.getCraftBukkitClass("inventory.CraftItemStack");
		_asBukkitCopy = craftItemStackClass.getMethod("asBukkitCopy", minecraftItemStackClass);
		_asNMSCopy = craftItemStackClass.getMethod("asNMSCopy", ItemStack.class);
		_handle = craftItemStackClass.getDeclaredField("handle");
		_handle.setAccessible(true);
		
		Class<?> minecraftEntityClass = BukkitReflect.getMinecraftClass("Entity");
		_e0 = minecraftEntityClass.getMethod("e", nbtTagCompoundClass);
		_f0 = minecraftEntityClass.getMethod("f", nbtTagCompoundClass);
		_a0 = minecraftEntityClass.getDeclaredMethod("a", nbtTagCompoundClass);
		_a0.setAccessible(true);
		
		Class<?> craftEntityClass = BukkitReflect.getCraftBukkitClass("entity.CraftEntity");
		_getHandle = craftEntityClass.getMethod("getHandle");
		
		Class<?> minecraftTileEntityClass = BukkitReflect.getMinecraftClass("TileEntity");
		_b1 = minecraftTileEntityClass.getMethod("b", nbtTagCompoundClass);
		_a1 = minecraftTileEntityClass.getMethod("a", nbtTagCompoundClass);
		
		Class<?> craftWorldClass = BukkitReflect.getCraftBukkitClass("CraftWorld");
		_getTileEntity = craftWorldClass.getMethod("getTileEntityAt", int.class, int.class, int.class);
	}
	
	private NBTUtils() { }
	
	public static ItemStack itemStackFromNBTTagCompound(NBTTagCompoundWrapper data) {
		NBTBaseWrapper.prepareReflection();
		return (ItemStack) BukkitReflect.invokeMethod(null, _asBukkitCopy, BukkitReflect.invokeMethod(null, _createStack, data._nbtBaseObject));
	}
	
	public static NBTTagCompoundWrapper nbtTagCompoundFromItemStack(ItemStack stack) {
		NBTBaseWrapper.prepareReflection();
		NBTTagCompoundWrapper data = new NBTTagCompoundWrapper();
		BukkitReflect.invokeMethod(BukkitReflect.invokeMethod(null, _asNMSCopy, stack), _save, data._nbtBaseObject);
		return data;
	}
	
	public static NBTTagCompoundWrapper getMineEntityNBTTagCompound(Object minecraftEntity) {
		NBTBaseWrapper.prepareReflection();
		NBTTagCompoundWrapper data = new NBTTagCompoundWrapper();
		BukkitReflect.invokeMethod(minecraftEntity, _e0, data._nbtBaseObject);
		return data;
	}
	
	static void setMineEntityNBTTagCompound(Object minecraftEntity, NBTTagCompoundWrapper data) {
		NBTTagCompoundWrapper entityData = getMineEntityNBTTagCompound(minecraftEntity);
		NBTTagListWrapper pos = entityData.getList("Pos"); // Save the position.
		entityData.merge(data);
		entityData.setList("Pos", pos);
		BukkitReflect.invokeMethod(minecraftEntity, _f0, entityData._nbtBaseObject);
		BukkitReflect.invokeMethod(minecraftEntity, _a0, entityData._nbtBaseObject);
	}
	
	public static NBTTagCompoundWrapper getEntityNBTTagCompound(Entity entity) {
		return getMineEntityNBTTagCompound(BukkitReflect.invokeMethod(entity, _getHandle));
	}
	
	public static void setEntityNBTTagCompound(Entity entity, NBTTagCompoundWrapper data) {
		setMineEntityNBTTagCompound(BukkitReflect.invokeMethod(entity, _getHandle), data);
	}
	
	public static NBTTagListWrapper effectsListFromPotion(ItemStack potion) {
		NBTBaseWrapper.prepareReflection();
		Object tagObject = BukkitReflect.invokeMethod(BukkitReflect.invokeMethod(null, _asNMSCopy, potion), _getTag);
		if (tagObject != null) {
			NBTTagCompoundWrapper tag = new NBTTagCompoundWrapper(tagObject);
			if (tag != null && tag.hasKey("CustomPotionEffects")) {
				return tag.getList("CustomPotionEffects").clone();
			}
		}
		// Fallback to default potion effect.
		Collection<PotionEffect> effects = Potion.fromItemStack(potion).getEffects();
		NBTTagListWrapper effectList = new NBTTagListWrapper();
		for (PotionEffect effect : effects) {
			NBTTagCompoundWrapper effectTag = new NBTTagCompoundWrapper();
			effectTag.setByte("Id", (byte)effect.getType().getId());
			effectTag.setByte("Amplifier", (byte)effect.getAmplifier());
			effectTag.setInt("Duration", effect.getDuration());
			effectList.add(effectTag);
		}
		return effectList;
	}
	
	public static ItemStack getGenericPotionFromEffectList(NBTTagListWrapper effectList) {
		NBTBaseWrapper.prepareReflection();
		NBTTagCompoundWrapper tag = new NBTTagCompoundWrapper();
		tag.setList("CustomPotionEffects", effectList.clone());
		NBTTagCompoundWrapper data = new NBTTagCompoundWrapper();
		data.setShort("id", (short) Material.POTION.getId());
		data.setByte("Count", (byte) 1);
		data.setShort("Damage", (short) (new Potion(PotionType.SPEED, 1)).toDamageValue());
		data.setCompound("tag", tag);
		return itemStackFromNBTTagCompound(data);
	}
	
	private static Object getTileEntity(Block block) {
		return BukkitReflect.invokeMethod(block.getWorld(), _getTileEntity, block.getX(), block.getY(), block.getZ());
	}
	
	public static NBTTagCompoundWrapper getTileEntityNBTTagCompound(Block block) {
		NBTBaseWrapper.prepareReflection();
		Object tileEntity = getTileEntity(block);
		if (tileEntity != null) {
			NBTTagCompoundWrapper data = new NBTTagCompoundWrapper();
			BukkitReflect.invokeMethod(tileEntity, _b1, data._nbtBaseObject);
			return data;
		}
		return null;
	}
	
	public static void setTileEntityNBTTagCompound(Block block, NBTTagCompoundWrapper data) {
		NBTBaseWrapper.prepareReflection();
		Object tileEntity = getTileEntity(block);
		if (tileEntity != null) {
			BukkitReflect.invokeMethod(tileEntity, _a1, data._nbtBaseObject);
		}
	}
	
	public static NBTTagCompoundWrapper getItemStackTag(ItemStack item) {
		try {
			Object handle = _handle.get(item);
			Object tag = BukkitReflect.invokeMethod(handle, _getTag);
			return (tag == null ? new NBTTagCompoundWrapper() : new NBTTagCompoundWrapper(tag));
		} catch (Exception e) {
			throw new Error("Error while getting item tag.", e);
		}
	}
	
	public static void setItemStackTag(ItemStack item, NBTTagCompoundWrapper tag) {
		try {
			Object handle = _handle.get(item);
			BukkitReflect.invokeMethod(handle, _setTag, tag._nbtBaseObject);
		} catch (Exception e) {
			throw new Error("Error while setting item tag.", e);
		}
	}
	
	public static void setItemStackFakeEnchantment(ItemStack item) {
		NBTTagCompoundWrapper tag = getItemStackTag(item);
		tag.setList("ench", new NBTTagListWrapper());
		setItemStackTag(item, tag);
	}
}
