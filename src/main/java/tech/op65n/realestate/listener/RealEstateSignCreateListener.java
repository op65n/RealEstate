package tech.op65n.realestate.listener;

import com.github.frcsty.frozenactions.util.Color;
import com.github.frcsty.frozenactions.util.Replace;
import com.github.frcsty.frozenactions.wrapper.ActionHandler;
import com.google.common.primitives.Doubles;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import tech.op65n.realestate.RealEstatePlugin;
import tech.op65n.realestate.listener.event.RealEstateSignCreateEvent;
import tech.op65n.realestate.logger.Logger;
import tech.op65n.realestate.namespace.PluginKey;

import java.util.List;

public final class RealEstateSignCreateListener implements Listener {

    private final ActionHandler actionHandler;
    private final GriefPrevention griefPrevention = GriefPrevention.instance;
    private final RealEstatePlugin plugin;
    private final FileConfiguration configuration;

    public RealEstateSignCreateListener(final RealEstatePlugin plugin) {
        this.plugin = plugin;
        this.actionHandler = plugin.getActionHandler();
        this.configuration = plugin.getConfig();
    }

    /**
     * Manages the event call
     *
     * @param event {@link RealEstateSignCreateEvent}
     */
    @EventHandler
    @SuppressWarnings("UnstableApiUsage")
    public void onSignCreate(final RealEstateSignCreateEvent event) {
        final SignChangeEvent baseEvent = event.getBaseEvent();
        final Player player = event.getPlayer();
        final List<String> signLines = event.getLines();
        final Location location = baseEvent.getBlock().getLocation();

        final Claim claim = griefPrevention.dataStore.getClaimAt(location, false, null);
        if (claim == null) {
            actionHandler.execute(player, Color.translate(Replace.replaceList(
                    configuration.getStringList("message.notInAClaim")
            )));
            return;
        }

        if (!claim.ownerID.toString().equalsIgnoreCase(player.getUniqueId().toString())) {
            actionHandler.execute(player, Color.translate(Replace.replaceList(
                    configuration.getStringList("message.notClaimOwner")
            )));
            return;
        }

        final String priceLine = signLines.size() >= 2 ? signLines.get(1) : null;
        Double claimPrice = Doubles.tryParse(priceLine == null ? "" : priceLine.trim());
        if (claimPrice == null) {
            final double defaultClaimBlockPrice = getDefaultPrice();
            final double claimAreaSize = claim.getArea();

            claimPrice = defaultClaimBlockPrice * claimAreaSize;
        }

        setSignProperties(baseEvent, claimPrice, player);
        actionHandler.execute(player, Color.translate(Replace.replaceList(
                configuration.getStringList("message.sellingClaim"),
                "{price}", claimPrice
        )));
    }

    /**
     * Set's the sign's properties (contents)
     *
     * @param event  Base event
     * @param price  Claim price
     * @param seller Property seller
     */
    private void setSignProperties(final SignChangeEvent event, final double price, final Player seller) {
        Sign block = (Sign) event.getBlock().getState();
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            int index = 0;
            for (final String line : configuration.getStringList("settings.sign.lines")) {
                block.setLine(index, Color.translate(Replace.replaceString(
                        line,
                        "{price}", price,
                        "{seller}", seller.getName()
                )));
                index++;
            }

            final PersistentDataContainer container = block.getPersistentDataContainer();
            container.set(PluginKey.getKey("RealEstate_PropertyOwner"), PersistentDataType.STRING, seller.getUniqueId().toString());
            container.set(PluginKey.getKey("RealEstate_PropertyPrice"), PersistentDataType.DOUBLE, price);

            block.update(true);
        }, 2L);
    }

    /**
     * Returns the default claim block price if not price is specified
     *
     * @return default price
     */
    private double getDefaultPrice() {
        final ConfigurationSection claimSection = configuration.getConfigurationSection("settings.claim");
        if (claimSection == null) {
            Logger.logInfo(
                    "Configuration Section 'settings.claim' is missing, failed to retrieve default claim price!"
            );
            return 5;
        }

        return claimSection.getDouble("default-price");
    }

}
