package com.fractured.shields;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class PooledBlock {

    private Block block;
    private Material originalType;

    public PooledBlock(Block block, Material originalType) {
        this.block = block;
        this.originalType = originalType;
    }

    public Block getBlock() {
        return block;
    }

    public Material getOriginalType() {
        return originalType;
    }
}
