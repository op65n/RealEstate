package tech.op65n.realestate.namespace;

import tech.op65n.realestate.RealEstatePlugin;
import org.bukkit.NamespacedKey;

public final class PluginKey {

    private static RealEstatePlugin plugin;

    /**
     * Provides us with our plugin instance
     *
     * @param pl Our plugin instance
     */
    public static void setPlugin(final RealEstatePlugin pl) {
        plugin = pl;
    }

    /**
     * Returns a new {@link NamespacedKey} with the desired identifier key
     *
     * @param identifier Desired key identifier
     * @return {@link NamespacedKey} containing our key
     */
    public static NamespacedKey getKey(final String identifier) {
        return new NamespacedKey(plugin, identifier);
    }

}
