package com.github.frcsty.realestate.namespace;

import com.github.frcsty.realestate.RealEstatePlugin;
import org.bukkit.NamespacedKey;

public final class PluginKey {

    private static RealEstatePlugin plugin;

    public static void setPlugin(final RealEstatePlugin pl) {
        plugin = pl;
    }

    public static NamespacedKey getKey(final String identifier) {
        return new NamespacedKey(plugin, identifier);
    }

}
