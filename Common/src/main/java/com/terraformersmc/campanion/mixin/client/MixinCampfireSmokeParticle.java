package com.terraformersmc.campanion.mixin.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.CampfireSmokeParticle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import org.joml.Vector3f;
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
public abstract class MixinCampfireSmokeParticle extends TextureSheetParticle {

	protected MixinCampfireSmokeParticle(ClientLevel clientWorld, double d, double e, double f) {
		super(clientWorld, d, e, f);
	}

	@Inject(method = "<init>", at = @At(value = "RETURN"))
	private void campanion$setColor(ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, boolean bl, CallbackInfo ci) {
		BlockPos pos = BlockPos.containing(x, y, z);
		float[] currColor;
		Vector3f color = new Vector3f(0, 0, 0);
		boolean recolor = false;

		//The y position can be up to 2 blocks too high (almost never exactly two), so we do this twice just to be sure we're at the campfire.
		if (!(world.getBlockState(pos).getBlock() instanceof CampfireBlock)) {
			pos = pos.below();
		}


		if (world.getBlockState(pos).getBlock() instanceof CampfireBlock && world.getBlockEntity(pos) instanceof CampfireBlockEntity) {
			CampfireBlockEntity campfireBlockEntity = (CampfireBlockEntity) world.getBlockEntity(pos);
			for (ItemStack stack : campfireBlockEntity.getItems()) {
				if (stack.getItem() instanceof DyeItem) {
					recolor = true;
					currColor = ((DyeItem) stack.getItem()).getDyeColor().getTextureDiffuseColors();
					color.add(currColor[0], currColor[1], currColor[2]);
				}
			}
			if (recolor) {
				color.normalize();
				setColor(color.x(), color.y(), color.z());
			}
		}
	}
}
