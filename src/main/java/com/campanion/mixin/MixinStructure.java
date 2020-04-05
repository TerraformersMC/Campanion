package com.campanion.mixin;

import com.campanion.item.AccessorStructure;
import net.minecraft.structure.Structure;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(Structure.class)
public class MixinStructure implements AccessorStructure {

    @Final
    @Shadow
    private List<List<Structure.StructureBlockInfo>> blocks;

    @Override
    public List<List<Structure.StructureBlockInfo>> getBlocks() {
        return this.blocks;
    }
}
