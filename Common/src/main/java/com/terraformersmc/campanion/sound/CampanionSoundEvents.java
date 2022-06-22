package com.terraformersmc.campanion.sound;

import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import static com.terraformersmc.campanion.Campanion.MOD_ID;

public class CampanionSoundEvents {

	private static final Map<ResourceLocation, SoundEvent> SOUNDS = new LinkedHashMap<>();

	public static final SoundEvent HOWL = add("howl");
	public static final SoundEvent SPEAR_HIT_GROUND = add("spear_hit_ground");
	public static final SoundEvent SPEAR_HIT_FLESH = add("spear_hit_flesh");
	public static final SoundEvent SPEAR_THROW = add("spear_throw");
	public static final SoundEvent FLARE_STRIKE = add("flare_strike");

	private static SoundEvent add(String id) {
		ResourceLocation identifier = new ResourceLocation(MOD_ID, id);
		SoundEvent event = new SoundEvent(identifier);
		SOUNDS.put(identifier, event);
		return event;
	}

//	public static void register() {
//		for (ResourceLocation identifier : SOUNDS.keySet()) {
//			Registry.register(Registry.SOUND_EVENT, identifier, SOUNDS.get(identifier));
//		}
//	}

	public static Map<ResourceLocation, SoundEvent> getSounds() {
		return SOUNDS;
	}
}
