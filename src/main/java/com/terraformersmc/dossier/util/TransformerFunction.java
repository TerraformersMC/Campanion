package com.terraformersmc.dossier.util;

import com.google.common.collect.Sets;

import java.util.Set;
import java.util.regex.Pattern;

public interface TransformerFunction<T, A> {

	/**
	 * Allows for replacing a part of a String with an array of values.
	 * args[0] is the key while the rest are values. The transformer replaces
	 * all instances of <key> with the specified values.
	 */
	TransformerFunction<String, String> REPLACE_TRANSFORMER = (template, args) -> {
		String key = args[0];
		Set<String> transformed = Sets.newHashSet();
		for (String value : args) {
			if (value.equals(key)) {
				continue;
			}
			transformed.add(template.replaceAll(Pattern.quote("<" + key + ">"), value));
		}
		return transformed;
	};

	Set<T> apply(T template, A[] args);
}
