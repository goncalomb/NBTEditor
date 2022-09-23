/*
 * Copyright (C) 2013-2018 Gonçalo Baltazar <me@goncalomb.com>
 *
 * This file is part of NBTEditor.
 *
 * NBTEditor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NBTEditor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NBTEditor.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.goncalomb.bukkit.customitems.items;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.goncalomb.bukkit.customitems.api.CustomItem;
import com.goncalomb.bukkit.customitems.api.PlayerDetails;

public abstract class GenericSuperAxe extends CustomItem {

	protected GenericSuperAxe(String slug, String name) {
		super(slug, name, Material.DIAMOND_AXE);
	}

	protected boolean isLog(Material mat) {
		return (mat == Material.OAK_LOG || mat == Material.SPRUCE_LOG || mat == Material.BIRCH_LOG
				|| mat == Material.JUNGLE_LOG || mat == Material.ACACIA_LOG || mat == Material.DARK_OAK_LOG
				|| mat == Material.STRIPPED_OAK_LOG || mat == Material.STRIPPED_SPRUCE_LOG
				|| mat == Material.STRIPPED_BIRCH_LOG || mat == Material.STRIPPED_JUNGLE_LOG
				|| mat == Material.STRIPPED_ACACIA_LOG || mat == Material.STRIPPED_DARK_OAK_LOG);
	}

	protected boolean isLeaves(Material mat) {
		return (mat == Material.OAK_LEAVES || mat == Material.SPRUCE_LEAVES || mat == Material.BIRCH_LEAVES
				|| mat == Material.JUNGLE_LEAVES || mat == Material.ACACIA_LEAVES || mat == Material.DARK_OAK_LEAVES);
	}

	@Override
	public void onLeftClick(PlayerInteractEvent event, PlayerDetails details) {
		if (event.getAction() == org.bukkit.event.block.Action.LEFT_CLICK_BLOCK && isLog(event.getClickedBlock().getType())) {
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 5*20, 1), true);
		}
	}

	protected Set<Block> getTreeBlocks(Block root) {
		return new BlockFinder(root).getBlocks();
	}

	private final class BlockFinder {

		@SuppressWarnings("serial")
		private final class BlockLimitException extends Exception { }

		private Block _root;
		private HashSet<Block> _finalSet;
		private HashSet<Block> _leaves;
		private HashSet<Block> _groundedBlocks;

		public BlockFinder(Block root) {
			_root = root;
		}

		private void checkBlockLimit(int more) throws BlockLimitException {
			if (_finalSet.size() + more > 1000) {
				throw new BlockLimitException();
			}
		}

		private boolean isGround(Material mat) {
			return (!isLog(mat) && !isLeaves(mat) && mat.isSolid());
		}

		public Set<Block> getBlocks() {
			if (_finalSet == null) {
				try {
					_finalSet = new HashSet<Block>();
					_groundedBlocks = new HashSet<Block>();
					for (Block block : getNeighbourBlocks(_root)) {
						if (!_finalSet.contains(block)) {
							findConnectedLogs(block);
						}
					}
					_leaves = new HashSet<Block>();
					for (Block block : _finalSet) {
						findConnectedLeaves(block, 0);
					}
					_finalSet.addAll(_leaves);
				} catch (BlockLimitException e) {
					// Block limit reached return nothing.
					_finalSet = new HashSet<Block>();
				}
				_leaves = null;
				_groundedBlocks = null;
			}
			return _finalSet;
		}

		private void findConnectedLogs(Block start) throws BlockLimitException {
			HashSet<Block> result = new HashSet<Block>();
			HashSet<Block> blocks = new HashSet<Block>();
			blocks.add(start);
			while (blocks.size() > 0) {
				HashSet<Block> lastBlocks = blocks;
				blocks = new HashSet<Block>();
				for (Block block : lastBlocks) {
					if (!block.equals(_root)) {
						if (isLog(block.getType())) {
							if (result.add(block)) {
								blocks.addAll(getNeighbourBlocks(block));
								checkBlockLimit(result.size());
							}
						} else if (findGround(block)) {
							// Found ground, discard all and mark as grounded.
							_groundedBlocks.addAll(result);
							return;
						}
					}
				}
			}
			_finalSet.addAll(result);
		}

		private List<Block> getNeighbourBlocks(Block block) {
			ArrayList<Block> blocks = new ArrayList<Block>();
			for (int dx = -1; dx <= 1; ++dx) {
				for (int dy = -1; dy <= 1; ++dy) {
					for (int dz = -1; dz <= 1; ++dz) {
						if (dx != 0 || dy != 0 || dz != 0) {
							blocks.add(block.getRelative(dx, dy, dz));
						}
					}
				}
			}
			return blocks;
		}

		private boolean findGround(Block start) {
			if (_groundedBlocks.contains(start)) {
				return true;
			} if (!isGround(start.getType())) {
				return false;
			}
			HashSet<Block> result = new HashSet<Block>();
			HashSet<Block> blocks = new HashSet<Block>();
			blocks.add(start);
			while (blocks.size() > 0) {
				HashSet<Block> lastBlocks = blocks;
				blocks = new HashSet<Block>();
				for (Block block : lastBlocks) {
					if (isGround(block.getType()) && result.add(block)) {
						if (result.size() >= 5) {
							// Found 5 connected ground blocks.
							// Mark them as ground and return true.
							_groundedBlocks.addAll(result);
							return true;
						}
						blocks.add(block.getRelative(0, -1, 0));
						blocks.add(block.getRelative(0, 1, 0));
						blocks.add(block.getRelative(-1, 0, 0));
						blocks.add(block.getRelative(1, 0, 0));
						blocks.add(block.getRelative(0, 0, -1));
						blocks.add(block.getRelative(0, 0, 1));
					}
				}
			}
			return false;
		}

		private void findConnectedLeaves(Block block, int depth) throws BlockLimitException {
			if (depth == 0 || isLeaves(block.getType())) {
				if (depth != 0) {
					_leaves.add(block);
					checkBlockLimit(_leaves.size());
				}
				if (depth < 7) {
					findConnectedLeaves(block.getRelative(0, -1, 0), depth + 1);
					findConnectedLeaves(block.getRelative(0, 1, 0), depth + 1);
					findConnectedLeaves(block.getRelative(-1, 0, 0), depth + 1);
					findConnectedLeaves(block.getRelative(1, 0, 0), depth + 1);
					findConnectedLeaves(block.getRelative(0, 0, -1), depth + 1);
					findConnectedLeaves(block.getRelative(0, 0, 1), depth + 1);
				}
			} else if (isLog(block.getType())) {
				// Found log is it a small disconnected log patch?
				if (!_finalSet.contains(block) && !_groundedBlocks.contains(block) && !_leaves.contains(block)) {
					HashSet<Block> patch = findSmallLogPatch(block);
					_leaves.addAll(patch);
					checkBlockLimit(_leaves.size());
					for (Block b : patch) {
						findConnectedLeaves(b, 2);
					}
				}
			}
		}

		private HashSet<Block> findSmallLogPatch(Block start) {
			HashSet<Block> result = new HashSet<Block>();
			HashSet<Block> blocks = new HashSet<Block>();
			blocks.add(start);
			while (blocks.size() > 0) {
				HashSet<Block> lastBlocks = blocks;
				blocks = new HashSet<Block>();
				for (Block block : lastBlocks) {
					if (!block.equals(_root)) {
						if (isLog(block.getType())) {
							if (result.add(block)) {
								if (result.size() > 3) {
									// Found more than 3 connected log blocks.
									// This is not a small patch.
									// Here _groundedBlocks acts as a blacklist for patch blocks.
									_groundedBlocks.addAll(result);
									return new HashSet<Block>();
								}
								blocks.add(block.getRelative(0, -1, 0));
								blocks.add(block.getRelative(0, 1, 0));
								blocks.add(block.getRelative(-1, 0, 0));
								blocks.add(block.getRelative(1, 0, 0));
								blocks.add(block.getRelative(0, 0, -1));
								blocks.add(block.getRelative(0, 0, 1));
							}
						} else if (findGround(block)) {
							// Found ground, this is not a disconnected patch.
							return new HashSet<Block>();
						}
					}
				}
			}
			return result;
		}

	}

}
