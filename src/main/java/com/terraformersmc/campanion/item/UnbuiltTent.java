package com.terraformersmc.campanion.item;

import com.terraformersmc.campanion.Campanion;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class UnbuiltTent extends Item {

    private final Identifier structure;

    public UnbuiltTent(Settings settings, String structureName) {
        super(settings);
        this.structure = new Identifier(Campanion.MOD_ID, "tents/" + structureName + "_tent");
    }

    public Identifier getStructure() {
        return structure;
    }
}
