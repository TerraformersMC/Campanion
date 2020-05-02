package com.terraformersmc.campanion.item;

import net.minecraft.structure.Structure;

import java.util.List;

public interface AccessorStructure {
    List<Structure.PalettedBlockInfoList> getBlocks();
}
