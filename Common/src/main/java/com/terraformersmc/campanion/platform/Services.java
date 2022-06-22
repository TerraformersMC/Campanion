package com.terraformersmc.campanion.platform;

import com.terraformersmc.campanion.platform.services.IClientPlatformHelper;
import com.terraformersmc.campanion.platform.services.IPlatformHelper;
import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.platform.services.OmniNetwork;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ServiceLoader;

public class Services {

	public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
	@OnlyIn(Dist.CLIENT)
	public static final IClientPlatformHelper CLIENT_PLATFORM = load(IClientPlatformHelper.class);
	public static final OmniNetwork NETWORK = load(OmniNetwork.class);

    public static <T> T load(Class<T> clazz) {

        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Campanion.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
