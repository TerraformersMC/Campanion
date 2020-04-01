package com.campanion.tags;

import com.campanion.Campanion;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class CampanionBlockTags {
    public static final Tag<Block> LAWN_CHAIR = TagRegistry.block(new Identifier(Campanion.MOD_ID, "lawn_chairs"));
}
