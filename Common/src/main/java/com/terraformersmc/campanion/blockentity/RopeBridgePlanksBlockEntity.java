package com.terraformersmc.campanion.blockentity;

import com.terraformersmc.campanion.ropebridge.RopeBridge;
import com.terraformersmc.campanion.ropebridge.RopeBridgePlank;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RopeBridgePlanksBlockEntity extends SerializableBlockEntity {

	private final List<RopeBridgePlank> planks = new ArrayList<>();

	private VoxelShape fullPlankShape;
	private VoxelShape cutPlankShape;
	private VoxelShape slimPlankShape;

	private Mesh mesh;

	public RopeBridgePlanksBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public RopeBridgePlanksBlockEntity(BlockPos pos, BlockState state) {
		super(CampanionBlockEntities.ROPE_BRIDGE_PLANK, pos, state);
	}

	public List<RopeBridgePlank> getPlanks() {
		return Collections.unmodifiableList(this.planks);
	}

	private void clearPlankCache() {
		this.mesh = null;
		this.fullPlankShape = null;
		this.cutPlankShape = null;
		this.slimPlankShape = null;
	}

	public Mesh getMesh() {
		if(this.mesh != null) {
			return this.mesh;
		}
		Renderer renderer = RendererAccess.INSTANCE.getRenderer();
		MeshBuilder builder = renderer.meshBuilder();
		QuadEmitter emitter = builder.getEmitter();

		if(this.planks.isEmpty() || this.forceRenderStopper()) {
			RopeBridgePlank.generateDefaultStoppers(emitter);
		}
		for (RopeBridgePlank plank : this.planks) {
			plank.generateMesh(false, emitter);
		}
		return this.mesh = builder.build();
	}

	public void addPlank(RopeBridgePlank plank) {
		this.planks.add(plank);
		this.setChanged();
		this.clearPlankCache();
	}

	public boolean removeBroken() {
		boolean ret = this.planks.removeIf(RopeBridgePlank::isBroken);
		this.clearPlankCache();
		this.setChanged();
		if (this.level != null && !this.level.isClientSide) {
			this.sync();
			this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 11);
		}
		return ret;
	}

	public VoxelShape getOutlineShape() {
		if (this.fullPlankShape != null) {
			return this.fullPlankShape;
		}
		return this.fullPlankShape = this.generateShape(true);
	}

	public VoxelShape getCollisionShape() {
		if (this.slimPlankShape != null) {
			return this.slimPlankShape;
		}
		return this.slimPlankShape = Shapes.joinUnoptimized(this.generateShape(false), Shapes.block(), BooleanOp.AND);
	}

	public VoxelShape getRaytraceShape() {
		if (this.cutPlankShape != null) {
			return this.cutPlankShape;
		}
		return this.cutPlankShape = Shapes.joinUnoptimized(this.generateShape(true), Shapes.block(), BooleanOp.AND);
	}

	private VoxelShape generateStopperShape(boolean full, float x, float y, float z, double sin, double cos) {
		float size = (RopeBridge.STOPPER_WIDTH + 1) / 32F * (full ? 1F : 0.25F);
		return Shapes.box(-size, 0, -size, size, (RopeBridge.STOPPER_HEIGHT + 0.5) / 16F, size).move(x + sin, y, z - cos);
	}

	public VoxelShape generateShape(boolean full) {
		VoxelShape shape = Shapes.empty();
		if (this.planks.isEmpty() || this.forceRenderStopper()) {
			shape = Shapes.or(shape, this.generateStopperShape(full, 0.5F, 0, 0.5F, 0, -RopeBridge.PLANK_LENGTH / 2));
			shape = Shapes.or(shape, this.generateStopperShape(full, 0.5F, 0, 0.5F, 0, RopeBridge.PLANK_LENGTH / 2));
		}
		for (RopeBridgePlank plank : this.planks) {
			double sin = RopeBridge.PLANK_LENGTH / 2 * Math.sin(plank.getyAngle());
			double cos = RopeBridge.PLANK_LENGTH / 2 * Math.cos(plank.getyAngle());

			double xRange = 1.5 / 16F + Math.abs(sin);
			double yRange = Math.abs(Math.sin(plank.getTiltAngle())) * RopeBridge.PLANK_WIDTH / 2 + 1.5 / 16F;
			double zRange = 1.5 / 16F + Math.abs(cos);

			double minY = plank.getDeltaPosition().y - yRange;
			double maxY = plank.getDeltaPosition().y + yRange;

			double minX = plank.getDeltaPosition().x - xRange;
			double maxX = plank.getDeltaPosition().x + xRange;

			double minZ = plank.getDeltaPosition().z - zRange;
			double maxZ = plank.getDeltaPosition().z + zRange;

			shape = Shapes.or(shape, Shapes.box(minX, minY, minZ, maxX, maxY, maxZ));

			if (plank.isStopper()) {
				shape = Shapes.or(shape, this.generateStopperShape(full, (float) plank.getDeltaPosition().x, (float) plank.getDeltaPosition().y, (float) plank.getDeltaPosition().z, -sin, -cos));
				shape = Shapes.or(shape, this.generateStopperShape(full, (float) plank.getDeltaPosition().x, (float) plank.getDeltaPosition().y, (float) plank.getDeltaPosition().z, sin, cos));
			}
		}
		return shape;
	}

	public boolean forceRenderStopper() {
		return false;
	}


	public void fromClientTag(CompoundTag tag) {
		fromTag(tag);

		assert this.level != null;
		this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 11);
	}

	public void toClientTag(CompoundTag tag) {
		toTag(tag);
	}

	@Override
	public void toTag(CompoundTag tag) {
		tag.put("Planks", writeTo(this.planks));
	}

	@Override
	public void fromTag(CompoundTag tag) {
		this.planks.clear();
		this.planks.addAll(this.getFrom(tag.getList("Planks", Tag.TAG_COMPOUND)));
		this.clearPlankCache();
	}

	protected List<RopeBridgePlank> getFrom(ListTag list) {
		List<RopeBridgePlank> out = new ArrayList<>();
		for (Tag nbt : list) {
			out.add(RopeBridgePlank.deserialize((CompoundTag) nbt));
		}
		return out;
	}

	protected ListTag writeTo(List<RopeBridgePlank> planks) {
		ListTag list = new ListTag();
		for (RopeBridgePlank plank : planks) {
			list.add(RopeBridgePlank.serialize(plank));
		}
		return list;
	}
}
