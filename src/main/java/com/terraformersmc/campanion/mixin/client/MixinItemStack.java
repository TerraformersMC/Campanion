package com.terraformersmc.campanion.mixin.client;

import com.terraformersmc.campanion.client.renderer.item.FakeWorld;
import com.terraformersmc.campanion.item.CampanionRenderWorldStasher;
import com.terraformersmc.campanion.item.PlaceableTentItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemStack.class)
public abstract class MixinItemStack implements CampanionRenderWorldStasher {
	@Shadow
	public abstract Item getItem();

	private FakeWorld campanionRenderWorld = null;

	@Override
	public void setCampanionRenderWorld(FakeWorld world) {
		this.campanionRenderWorld = world;
	}

	@Override
	public FakeWorld getCampanionRenderWorld(ItemStack stack, BlockPos basePos, int lightOverride) {
		if ((this.getItem() instanceof PlaceableTentItem)) {
			if (this.campanionRenderWorld == null) {
				this.campanionRenderWorld = new FakeWorld(stack, basePos, lightOverride);
			} else {
				this.campanionRenderWorld.updatePositioning(basePos, lightOverride);
			}
		}
		return this.campanionRenderWorld;
	}
}
