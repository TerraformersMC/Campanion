package com.terraformersmc.campanion.mixin.client;

import net.minecraft.block.CampfireBlock;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.client.particle.CampfireSmokeParticle;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixes into the Campfire block to allow for us to recolor the particles for colored signals
 * Original author Kyrptonaught from their mod (https://www.curseforge.com/minecraft/mc-mods/colorful-campfire-smoke)
 * modified for the use cases in Campanion with their permission.
 */
@Mixin(CampfireSmokeParticle.class)
public abstract class MixinCampfireSmokeParticle extends SpriteBillboardParticle {

	protected MixinCampfireSmokeParticle(ClientWorld clientWorld, double d, double e, double f) {
		super(clientWorld, d, e, f);
	}

	@Inject(method = "<init>", at = @At(value = "RETURN"))
	private void campanion$setColor(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, boolean bl, CallbackInfo ci) {
		BlockPos pos = new BlockPos(x, y, z);
		float[] currColor;
		Vector3f color = new Vector3f(0, 0, 0);
		boolean recolor = false;
		if (world.getBlockState(pos).getBlock() instanceof CampfireBlock && world.getBlockEntity(pos) instanceof CampfireBlockEntity) {
			CampfireBlockEntity campfireBlockEntity = (CampfireBlockEntity) world.getBlockEntity(pos);
			for (ItemStack stack : campfireBlockEntity.getItemsBeingCooked()) {
				if (stack.getItem() instanceof DyeItem) {
					recolor = true;
					currColor = ((DyeItem) stack.getItem()).getColor().getColorComponents();
					color.add(currColor[0], currColor[1], currColor[2]);
				}
			}
			if (recolor) {
				color.normalize();
				setColor(color.getX(), color.getY(), color.getZ());
			}
		}
	}
}
