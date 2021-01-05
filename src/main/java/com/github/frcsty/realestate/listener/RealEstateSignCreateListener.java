package com.github.frcsty.realestate.listener;

import com.github.frcsty.frozenactions.util.ReplaceUtils;
import com.github.frcsty.frozenactions.wrapper.ActionHandler;
import com.github.frcsty.realestate.RealEstatePlugin;
import com.github.frcsty.realestate.listener.event.RealEstateSignCreateEvent;
import com.github.frcsty.realestate.logger.Logger;
import com.github.frcsty.realestate.namespace.PluginKey;
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

    @EventHandler
    @SuppressWarnings("UnstableApiUsage")
    public void onSignCreate(final RealEstateSignCreateEvent event) {
        final SignChangeEvent baseEvent = event.getBaseEvent();
        final Player player = event.getPlayer();
        final List<String> signLines = event.getLines();
        final Location location = baseEvent.getBlock().getLocation();

        final Claim claim = griefPrevention.dataStore.getClaimAt(location, false, null);
        if (claim == null) {
            actionHandler.execute(player, ReplaceUtils.replaceList(true,
                    configuration.getStringList("message.notInAClaim")
            ));
            return;
        }

        if (!claim.ownerID.toString().equalsIgnoreCase(player.getUniqueId().toString())) {
            actionHandler.execute(player, ReplaceUtils.replaceList(true,
                    configuration.getStringList("message.notClaimOwner")
            ));
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
        actionHandler.execute(player, ReplaceUtils.replaceList(true,
                configuration.getStringList("message.sellingClaim"),
                "{price}", claimPrice
        ));
    }

    //@SuppressWarnings("ConstantConditions")
    private void setSignProperties(final SignChangeEvent event, final double price, final Player seller) {
        /*
        if (configuration.get("settings.sign.material") != null) {
            Material material = Material.matchMaterial(configuration.getString("settings.sign.material"));

            if (material == null) material = Material.DARK_OAK_SIGN;
            event.getBlock().setType(material);
        }
        */

        Sign block = (Sign) event.getBlock().getState();
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            int index = 0;
            for (final String line : configuration.getStringList("settings.sign.lines")) {
                block.setLine(index, ReplaceUtils.replaceString(true,
                        line,
                        "{price}", price,
                        "{seller}", seller.getName()
                ));
                index++;
            }

            final PersistentDataContainer container = block.getPersistentDataContainer();
            container.set(PluginKey.getKey("RealEstate_PropertyOwner"), PersistentDataType.STRING, seller.getUniqueId().toString());
            container.set(PluginKey.getKey("RealEstate_PropertyPrice"), PersistentDataType.DOUBLE, price);

            block.update(true);
        }, 2L);
    }

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
