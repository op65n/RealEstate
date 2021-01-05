package com.github.frcsty.realestate.logger;

import com.github.frcsty.realestate.RealEstatePlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

public final class Logger {

    private static RealEstatePlugin plugin;
    private static java.util.logging.Logger logger;

    public static void setLogger(final java.util.logging.Logger newLogger) {
        logger = newLogger;
    }

    public static void setPlugin(final RealEstatePlugin pl) {
        plugin = pl;
    }

    public static void logInfo(final String text) {
        logger.log(Level.INFO, text);
    }

    public static void logTransaction(final UUID buyer, final String text) {
        final File file = new File(plugin.getDataFolder(), "sign-transactions.yml");
        final FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        configuration.set("transaction." + buyer.toString(), text);

        try {
            configuration.save(file);
        } catch (final IOException ignored) { }
    }

}
