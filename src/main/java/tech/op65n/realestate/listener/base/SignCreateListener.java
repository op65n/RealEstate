package tech.op65n.realestate.listener.base;

import tech.op65n.realestate.listener.event.RealEstateSignCreateEvent;
import tech.op65n.realestate.logger.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.PluginManager;

public final class SignCreateListener implements Listener {

    private final FileConfiguration configuration;
    private final PluginManager pluginManager;

    public SignCreateListener(final FileConfiguration configuration) {
        this.configuration = configuration;
        this.pluginManager = Bukkit.getPluginManager();
    }

    @EventHandler
    public void onSignChange(final SignChangeEvent event) {
        if (!isRealEstateSign(event.getLines())) return;

        pluginManager.callEvent(new RealEstateSignCreateEvent(
                event
        ));
    }

    /**
     * Checks if the involved sign is a Real Estate sign
     *
     * @param lines Sign contents
     * @return status on whether a sign is a Real Estate sign or not
     */
    private boolean isRealEstateSign(final String[] lines) {
        if (lines.length == 0) return false;

        final String line = lines[0];
        final ConfigurationSection signSection = configuration.getConfigurationSection("settings.sign");
        if (signSection == null) {
            Logger.logInfo(
                    "Configuration Section 'settings.sign' is missing, failed to check sign contents!"
            );
            return false;
        }

        return line.equalsIgnoreCase(signSection.getString("short")) || line.equalsIgnoreCase(signSection.getString("long"));
    }

}
