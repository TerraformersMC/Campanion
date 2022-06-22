package com.terraformersmc.campanion.item;

import java.util.List;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public interface AccessorStructure {
    List<StructureTemplate.Palette> getBlocks();
}
