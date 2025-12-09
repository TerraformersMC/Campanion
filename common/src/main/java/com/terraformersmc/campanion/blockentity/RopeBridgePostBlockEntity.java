package com.terraformersmc.campanion.blockentity;

import com.terraformersmc.campanion.ropebridge.RopeBridgePlank;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.state.BlockState;

public class RopeBridgePostBlockEntity extends RopeBridgePlanksBlockEntity {

    private final Map<BlockPos, List<Pair<BlockPos, List<RopeBridgePlank>>>> ghostPlanks = new HashMap<>();
    private final List<BlockPos> linkedPositions = new ArrayList<>();

    public RopeBridgePostBlockEntity(BlockPos pos, BlockState state) {
        super(CampanionBlockEntities.ROPE_BRIDGE_POST, pos, state);
    }

    public Map<BlockPos, List<Pair<BlockPos, List<RopeBridgePlank>>>> getGhostPlanks() {
        return ghostPlanks;
    }

    public List<BlockPos> getLinkedPositions() {
        return linkedPositions;
    }

    @Override
    public boolean forceRenderStopper() {
        return this.getPlanks().stream().noneMatch(RopeBridgePlank::stopper)
            && this.ghostPlanks.values().stream().flatMap(Collection::stream).map(Pair::getRight).flatMap(Collection::stream).anyMatch(RopeBridgePlank::stopper);
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        super.fromClientTag(tag);
        this.ghostPlanks.clear();
        for (Tag nbtRaw : tag.getList("GhostPlankMap", 10)) {
            BlockPos pos = BlockPos.of(((CompoundTag) nbtRaw).getLong("Position"));
            List<Pair<BlockPos, List<RopeBridgePlank>>> list = new LinkedList<>();
            for (Tag nbtRawGhostPlanks : ((CompoundTag) nbtRaw).getList("GhostPlanks", 10)) {
                CompoundTag nbt = (CompoundTag) nbtRawGhostPlanks;
                list.add(Pair.of(BlockPos.of(nbt.getLong("Pos")), this.getFrom(nbt.getList("Planks", 10))));
            }
            this.ghostPlanks.put(pos, list);
        }

        this.linkedPositions.clear();
        Arrays.stream(tag.getLongArray("LinkedPositions")).mapToObj(BlockPos::of).forEach(this.linkedPositions::add);
    }

    @Override
    public void toClientTag(CompoundTag tag) {
        super.toClientTag(tag);
        ListTag list = new ListTag();
        this.ghostPlanks.forEach((plankPos, pairs) -> {
            CompoundTag nbt = new CompoundTag();
            nbt.putLong("Position", plankPos.asLong());

            ListTag ghostPlanksTags = new ListTag();
            for (Pair<BlockPos, List<RopeBridgePlank>> plank : pairs) {
                CompoundTag t = new CompoundTag();
                t.putLong("Pos", plank.getLeft().asLong());
                t.put("Planks", this.writeTo(plank.getRight()));
                ghostPlanksTags.add(t);
            }
            nbt.put("GhostPlanks", ghostPlanksTags);
            list.add(nbt);
        });
        tag.put("GhostPlankMap", list);

        tag.putLongArray("LinkedPositions", this.linkedPositions.stream().mapToLong(BlockPos::asLong).toArray());
    }
}
