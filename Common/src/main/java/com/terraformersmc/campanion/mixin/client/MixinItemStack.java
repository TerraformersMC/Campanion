package com.terraformersmc.campanion.mixin.client;

import com.terraformersmc.campanion.client.renderer.item.FakeWorld;
import com.terraformersmc.campanion.item.CampanionRenderWorldStasher;
import com.terraformersmc.campanion.item.PlaceableTentItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemStack.class)
public abstract class MixinItemStack implements CampanionRenderWorldStasher {
	private FakeWorld campanionRenderWorld = null;

	@Override
	public void setCampanionRenderWorld(FakeWorld world) {
		this.campanionRenderWorld = world;
	}

	@Override
	public FakeWorld getCampanionRenderWorld(ItemStack stack, BlockPos basePos, int lightOverride) {
		ItemStack self = (ItemStack) (Object) this;
		if ((self.getItem() instanceof PlaceableTentItem)) {
			if (this.campanionRenderWorld == null) {
				this.campanionRenderWorld = new FakeWorld(stack, basePos, lightOverride);
			} else {
				this.campanionRenderWorld.updatePositioning(basePos, lightOverride);
			}
		}
		return this.campanionRenderWorld;
	}
}
