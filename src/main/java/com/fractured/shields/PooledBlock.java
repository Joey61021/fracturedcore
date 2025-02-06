package com.fractured.shields;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

public class PooledBlock {

    private final Block block;
    private final Material originalType;
    private final BlockData originalBlockData;

    public PooledBlock(Block block) {
        this.block = block;
        this.originalType = block.getType();
        this.originalBlockData = block.getBlockData();
    }

    public Block getBlock() {
        return block;
    }

    public Material getOriginalType() {
        return originalType;
    }

    public BlockData getOriginalBlockData() {
        return originalBlockData;
    }
}
