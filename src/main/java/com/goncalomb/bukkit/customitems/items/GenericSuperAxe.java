package com.goncalomb.bukkit.customitems.items;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.goncalomb.bukkit.customitems.api.CustomItem;
import com.goncalomb.bukkit.customitems.api.PlayerDetails;

public abstract class GenericSuperAxe extends CustomItem {

	protected GenericSuperAxe(String slug, String name) {
		super(slug, name, new MaterialData(Material.DIAMOND_AXE));
	}
	
	@Override
	public void onLeftClick(PlayerInteractEvent event, PlayerDetails details) {
		if (event.getAction() == org.bukkit.event.block.Action.LEFT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.LOG) {
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 5*20, 3), true);
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
						if (block.getType() == Material.LOG) {
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
			} if (!isGroundBlock(start)) {
				return false;
			}
			HashSet<Block> result = new HashSet<Block>();
			HashSet<Block> blocks = new HashSet<Block>();
			blocks.add(start);
			while (blocks.size() > 0) {
				HashSet<Block> lastBlocks = blocks;
				blocks = new HashSet<Block>();
				for (Block block : lastBlocks) {
					if (isGroundBlock(block) && result.add(block)) {
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
		
		private boolean isGroundBlock(Block block) {
			Material mat = block.getType();
			return (mat != Material.LOG && mat != Material.LEAVES && mat.isSolid());
		}
		
		private void findConnectedLeaves(Block block, int depth) throws BlockLimitException {
			if (depth == 0 || block.getType() == Material.LEAVES) {
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
			} else if (block.getType() == Material.LOG) {
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
						if (block.getType() == Material.LOG) {
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
