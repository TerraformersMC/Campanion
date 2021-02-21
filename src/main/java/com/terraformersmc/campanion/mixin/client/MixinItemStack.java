package com.terraformersmc.campanion.mixin.client;

import com.terraformersmc.campanion.client.renderer.item.FakeWorld;
import com.terraformersmc.campanion.item.CampanionRenderWorldStasher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemStack.class)
public class MixinItemStack implements CampanionRenderWorldStasher {
	private FakeWorld campanionRenderWorld = null;

	@Override
	public void setCampanionRenderWorld(FakeWorld world) {
		this.campanionRenderWorld = world;
	}

	@Override
	public FakeWorld getCampanionRenderWorld(BlockPos basePos, int lightOverride) {
		if (this.campanionRenderWorld == null) {
			this.campanionRenderWorld = new FakeWorld(basePos, lightOverride);
		} else {
			this.campanionRenderWorld.updatePositioning(basePos, lightOverride);
		}
		return this.campanionRenderWorld;
	}
}
