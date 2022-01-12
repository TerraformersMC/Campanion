package com.terraformersmc.campanion.blockentity;

import com.google.common.base.Preconditions;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

// based on https://github.com/Technici4n/Modern-Dynamics/blob/master/src/main/java/dev/technici4n/moderndynamics/MdBlockEntity.java
/// by shartte & Technici4n
public abstract class SerializableBlockEntity extends BlockEntity {
	private boolean shouldClientRemesh = true;

	public SerializableBlockEntity(BlockEntityType<?> bet, BlockPos pos, BlockState state) {
		super(bet, pos, state);
	}

	public void sync(boolean shouldRemesh) {
		Preconditions.checkNotNull(world); // Maintain distinct failure case from below
		if (!(world instanceof ServerWorld))
			throw new IllegalStateException("Cannot call sync() on the logical client! Did you check world.isClient first?");

		shouldClientRemesh = shouldRemesh | shouldClientRemesh;
		((ServerWorld) world).getChunkManager().markForUpdate(getPos());
	}

	public void sync() {
		sync(true);
	}

	public abstract void toTag(NbtCompound tag);

	public abstract void fromTag(NbtCompound tag);

	public abstract void toClientTag(NbtCompound tag);

	public abstract void fromClientTag(NbtCompound tag);

	@Override
	public final BlockEntityUpdateS2CPacket toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public final NbtCompound toInitialChunkDataNbt() {
		NbtCompound nbt = super.toInitialChunkDataNbt();
		toClientTag(nbt);
		nbt.putBoolean("#c", shouldClientRemesh); // mark client tag
		shouldClientRemesh = false;
		return nbt;
	}

	@Override
	protected final void writeNbt(NbtCompound nbt) {
		toTag(nbt);
	}

	@Override
	public final void readNbt(NbtCompound nbt) {
		if (nbt.contains("#c")) {
			fromClientTag(nbt);
			if (nbt.getBoolean("#c")) {
				remesh();
			}
		} else {
			fromTag(nbt);
		}
	}

	public final void remesh() {
		Preconditions.checkNotNull(world);
		if (!(world instanceof ClientWorld))
			throw new IllegalStateException("Cannot call remesh() on the server!");

		world.updateListeners(pos, null, null, 0);
	}
}
