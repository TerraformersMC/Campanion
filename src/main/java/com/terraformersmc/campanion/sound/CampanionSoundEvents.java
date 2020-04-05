package com.terraformersmc.campanion.sound;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.terraformersmc.campanion.Campanion.MOD_ID;

public class CampanionSoundEvents {

	private static final Map<Identifier, SoundEvent> SOUNDS = new LinkedHashMap<>();

	public static final SoundEvent HOWL = add("howl");
	public static final SoundEvent SPEAR_HIT_GROUND = add("spear_hit_ground");
	public static final SoundEvent SPEAR_HIT_FLESH = add("spear_hit_flesh");
	public static final SoundEvent SPEAR_THROW = add("spear_throw");

	private static SoundEvent add(String id) {
		Identifier identifier = new Identifier(MOD_ID, id);
		SoundEvent event = new SoundEvent(identifier);
		SOUNDS.put(identifier, event);
		return event;
	}

	public static void register() {
		for (Identifier identifier : SOUNDS.keySet()) {
			Registry.register(Registry.SOUND_EVENT, identifier, SOUNDS.get(identifier));
		}
	}
}
