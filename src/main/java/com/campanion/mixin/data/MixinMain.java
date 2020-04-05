package com.campanion.mixin.data;

import com.campanion.data.CampanionData;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.file.Path;
import java.util.Collection;

@Mixin(Main.class)
public class MixinMain {
	@Inject(method = "create", at = @At("TAIL"), cancellable = true)
	private static void onCreate(Path output, Collection<Path> inputs, boolean includeClient, boolean includeServer, boolean includeDev, boolean includeReports, boolean validate, CallbackInfoReturnable<DataGenerator> info) {
		if (CampanionData.ENABLED) {
			info.setReturnValue(CampanionData.create(output, inputs, includeClient, includeServer, includeDev, includeReports, validate));
		}
	}
}
