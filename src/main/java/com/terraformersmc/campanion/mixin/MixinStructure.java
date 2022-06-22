package com.terraformersmc.campanion.mixin;

import com.terraformersmc.campanion.item.AccessorStructure;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

@Mixin(StructureTemplate.class)
public class MixinStructure implements AccessorStructure {

    @Final
    @Shadow
    private List<StructureTemplate.Palette> blockInfoLists;

    @Override
    public List<StructureTemplate.Palette> getBlocks() {
        return this.blockInfoLists;
    }
}
