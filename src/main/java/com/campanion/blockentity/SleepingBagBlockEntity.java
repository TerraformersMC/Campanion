package com.campanion.blockentity;

import com.campanion.block.SleepingBagBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.DyeColor;

public class SleepingBagBlockEntity extends BlockEntity  {

	private DyeColor color;

	public SleepingBagBlockEntity() {
		super(CampanionBlockEntities.SLEEPING_BAG);
	}

	public SleepingBagBlockEntity(DyeColor dyeColor) {
		this();
		this.setColor(dyeColor);
	}

	public BlockEntityUpdateS2CPacket toUpdatePacket() {
		return new BlockEntityUpdateS2CPacket(this.pos, 11, this.toInitialChunkDataTag());
	}

	@Environment(EnvType.CLIENT)
	public DyeColor getColor() {
		if (this.color == null) {
			this.color = ((SleepingBagBlock)this.getCachedState().getBlock()).getColor();
		}

		return this.color;
	}

	public void setColor(DyeColor color) {
		this.color = color;
	}
}
