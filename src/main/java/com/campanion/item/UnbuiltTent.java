package com.campanion.item;

import com.campanion.Campanion;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class UnbuiltTent extends Item {

    private final Identifier structure;

    public UnbuiltTent(Settings settings, String structureName) {
        super(settings);
        this.structure = new Identifier(Campanion.MOD_ID, "tents/" + structureName + "_tent");
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            Structure structure = ((ServerWorld) world).getStructureManager().getStructure(this.structure);
            if(structure != null) {
                BlockPos size = structure.getSize();
                ItemStack out = new ItemStack(CampanionItems.TENT_BAG);
                ListTag list = new ListTag();
                for (Structure.StructureBlockInfo info : ((AccessorStructure) structure).getBlocks().get(0)) {
                    CompoundTag tag = new CompoundTag();
                    tag.put("Pos", NbtHelper.fromBlockPos(info.pos.add(-size.getX()/2, 0, -size.getZ()/2)));
                    tag.put("BlockState", NbtHelper.fromBlockState(info.state));
                    if(info.tag != null && !info.tag.isEmpty()) {
                        tag.put("BlockEntityData", info.tag);
                    }
                    list.add(tag);
                }

                out.getOrCreateTag().put("Blocks", list);
                out.getOrCreateTag().put("TentSize", NbtHelper.fromBlockPos(size));

                return new TypedActionResult<>(ActionResult.CONSUME, out);
            }
        }

        return super.use(world, user, hand);
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
