package com.campanion.sound;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;

import static com.campanion.Campanion.MOD_ID;

public class CampanionSoundEvents {

	public static final ArrayList<SoundEvent> SOUNDS = new ArrayList<>();

	public static final SoundEvent HOWL = add("howl");
	public static final SoundEvent SPEAR_HIT_GROUND = add("spear_hit_ground");
	public static final SoundEvent SPEAR_HIT_FLESH = add("spear_hit_flesh");
	public static final SoundEvent SPEAR_THROW = add("spear_throw");

	private static SoundEvent add(String id) {
		SoundEvent event = (new SoundEvent(new Identifier(MOD_ID, id)));
		SOUNDS.add(event);
		return event;
	}

	public static void register() {
		for (SoundEvent event : SOUNDS) {
			Registry.register(Registry.SOUND_EVENT, event.getId(), event);
		}
	}
}
