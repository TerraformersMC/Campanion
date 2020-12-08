package com.terraformersmc.campanion.mixin;

import net.minecraft.block.CampfireBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

/**
 * Mixes into the Campfire block to allow for us to recolor the particles for colored signals
 * Original author Kyrptonaught from their mod (https://www.curseforge.com/minecraft/mc-mods/colorful-campfire-smoke)
 * modified for the use cases in Campanion with their permission.
 */
@Mixin(CampfireBlock.class)
public class MixinCampfire {

	@Inject(method = "spawnSmokeParticle(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;ZZ)V", at = @At(value = "HEAD"), cancellable = true)
	private static void spawnColoredParticle(World world, BlockPos pos, boolean isSignal, boolean lotsOfSmoke, CallbackInfo cbi) {
		if (world.isReceivingRedstonePower(pos)) {
			Random random = world.getRandom();
			world.addImportantParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, true, (double) pos.getX() + 0.5D + random.nextDouble() / 3.0D * (double) (random.nextBoolean() ? 1 : -1), (double) pos.getY() + random.nextDouble() + random.nextDouble(), (double) pos.getZ() + 0.5D + random.nextDouble() / 3.0D * (double) (random.nextBoolean() ? 1 : -1), pos.getX(), pos.getY(), pos.getZ());
			cbi.cancel();
		}
	}
}
