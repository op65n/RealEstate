package tech.op65n.realestate.logger;

import tech.op65n.realestate.RealEstatePlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

public final class Logger {

    private static RealEstatePlugin plugin;
    private static java.util.logging.Logger logger;

    /**
     * Sets our logger
     *
     * @param newLogger Our plugin logger
     */
    public static void setLogger(final java.util.logging.Logger newLogger) {
        logger = newLogger;
    }

    /**
     * Sets our plugin
     *
     * @param pl Our plugin instance
     */
    public static void setPlugin(final RealEstatePlugin pl) {
        plugin = pl;
    }

    /**
     * Logs the desired input text as {@link Level#INFO}
     *
     * @param text Our desired info text
     */
    public static void logInfo(final String text) {
        logger.log(Level.INFO, text);
    }

    /**
     * Logs a RealEstate sign transaction to a file
     *
     * @param buyer The UUID of the claim buyer
     * @param text The transaction string
     */
    public static void logTransaction(final UUID buyer, final String text) {
        final File file = new File(plugin.getDataFolder(), "sign-transactions.yml");
        final FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        configuration.set("transaction." + buyer.toString(), text);

        try {
            configuration.save(file);
        } catch (final IOException ignored) { }
    }

}
