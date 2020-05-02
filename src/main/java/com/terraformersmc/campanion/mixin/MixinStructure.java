package com.terraformersmc.campanion.mixin;

import com.terraformersmc.campanion.item.AccessorStructure;
import net.minecraft.structure.Structure;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(Structure.class)
public class MixinStructure implements AccessorStructure {

    @Final
    @Shadow
    private List<Structure.PalettedBlockInfoList> blockInfoLists;

    @Override
    public List<Structure.PalettedBlockInfoList> getBlocks() {
        return this.blockInfoLists;
    }
}
