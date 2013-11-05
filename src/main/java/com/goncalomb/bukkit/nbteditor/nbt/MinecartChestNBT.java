package com.goncalomb.bukkit.nbteditor.nbt;

import org.bukkit.block.Block;

public class MinecartChestNBT extends MinecartContainerNBT {

	@Override
	public void copyFromChest(Block block) {
		internalCopyFromChest(block, 27);
	}

}
