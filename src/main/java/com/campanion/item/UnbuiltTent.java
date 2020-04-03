package com.campanion.item;

import com.campanion.Campanion;
import com.campanion.block.BaseTentBlock;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

public class UnbuiltTent extends Item {

    public UnbuiltTent(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (!context.getWorld().isClient) {
            Structure structure = ((ServerWorld) context.getWorld()).getStructureManager().getStructure(new Identifier(Campanion.MOD_ID, "tents/small_tent"));
            BlockPos size = structure.getSize();
            structure.place(
                context.getWorld(),
                context.getBlockPos().add(-size.getX()/2, 1, -size.getZ()/2),
                new StructurePlacementData()
                    .setRotation(BlockRotation.values()[context.getPlayer().getHorizontalFacing().getHorizontal()])
                    .setPosition(new BlockPos(size.getX()/2, 0, size.getZ()/2))
                    .setIgnoreEntities(true)
                    .addProcessor(new TentProcessor(context.getBlockPos().up(), size))
            );
        }
        return ActionResult.CONSUME;
    }

    private class TentProcessor extends StructureProcessor {

        private final BlockPos pos;
        private final BlockPos size;

        private TentProcessor(BlockPos pos, BlockPos size) {
            this.pos = pos;
            this.size = size;
        }

        @Override
        public Structure.StructureBlockInfo process(WorldView worldView, BlockPos pos, Structure.StructureBlockInfo structureBlockInfo, Structure.StructureBlockInfo structureBlockInfo2, StructurePlacementData placementData) {
            if(structureBlockInfo2.state.getBlock() instanceof BaseTentBlock) {
                CompoundTag tag = new CompoundTag();
                tag.put("LinkedPos", NbtHelper.fromBlockPos(this.pos));
                tag.put("Size", NbtHelper.fromBlockPos(this.size));
                return new Structure.StructureBlockInfo(structureBlockInfo2.pos, structureBlockInfo2.state, tag);
            }
            return structureBlockInfo2;
        }

        @Override
        protected StructureProcessorType getType() {
            return StructureProcessorType.NOP;
        }

        @Override
        protected <T> Dynamic<T> method_16666(DynamicOps<T> dynamicOps) {
            return new Dynamic<>(dynamicOps, dynamicOps.emptyMap());
        }
    }
}
