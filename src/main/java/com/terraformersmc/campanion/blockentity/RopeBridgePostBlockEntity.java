package com.terraformersmc.campanion.blockentity;

import com.terraformersmc.campanion.ropebridge.RopeBridgePlank;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

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
        return this.getPlanks().stream().noneMatch(RopeBridgePlank::isStopper)
            && this.ghostPlanks.values().stream().flatMap(Collection::stream).map(Pair::getRight).flatMap(Collection::stream).anyMatch(RopeBridgePlank::isStopper);
    }

    @Override
    public void fromClientTag(NbtCompound tag) {
        super.fromClientTag(tag);
        this.ghostPlanks.clear();
        for (NbtElement nbtRaw : tag.getList("GhostPlankMap", 10)) {
            BlockPos pos = BlockPos.fromLong(((NbtCompound) nbtRaw).getLong("Position"));
            List<Pair<BlockPos, List<RopeBridgePlank>>> list = new LinkedList<>();
            for (NbtElement nbtRawGhostPlanks : ((NbtCompound) nbtRaw).getList("GhostPlanks", 10)) {
                NbtCompound nbt = (NbtCompound) nbtRawGhostPlanks;
                list.add(Pair.of(BlockPos.fromLong(nbt.getLong("Pos")), this.getFrom(nbt.getList("Planks", 10))));
            }
            this.ghostPlanks.put(pos, list);
        }

        this.linkedPositions.clear();
        Arrays.stream(tag.getLongArray("LinkedPositions")).mapToObj(BlockPos::fromLong).forEach(this.linkedPositions::add);
    }

    @Override
    public void toClientTag(NbtCompound tag) {
        super.toClientTag(tag);
        NbtList list = new NbtList();
        this.ghostPlanks.forEach((plankPos, pairs) -> {
            NbtCompound nbt = new NbtCompound();
            nbt.putLong("Position", plankPos.asLong());

            NbtList ghostPlanksTags = new NbtList();
            for (Pair<BlockPos, List<RopeBridgePlank>> plank : pairs) {
                NbtCompound t = new NbtCompound();
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
