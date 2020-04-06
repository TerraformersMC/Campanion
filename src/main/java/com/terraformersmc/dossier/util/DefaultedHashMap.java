package com.terraformersmc.dossier.util;

import java.util.HashMap;

public class DefaultedHashMap<K, V> extends HashMap<K, V> {
	protected V defaultValue;

	public DefaultedHashMap(V defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public V get(Object key) {
		if (this.containsKey(key)) {
			return super.get(key);
		}
		return this.defaultValue;
	}
}