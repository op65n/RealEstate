package tech.op65n.realestate.listener;

import com.github.frcsty.frozenactions.util.ReplaceUtils;
import com.github.frcsty.frozenactions.wrapper.ActionHandler;
import tech.op65n.realestate.RealEstatePlugin;
import tech.op65n.realestate.listener.event.RealEstateSignInteractEvent;
import tech.op65n.realestate.logger.Logger;
import tech.op65n.realestate.queue.MessageQueue;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;

public final class RealEstateSignInteractListener implements Listener {

    private final ActionHandler actionHandler;
    private final GriefPrevention griefPrevention = GriefPrevention.instance;
    private final FileConfiguration configuration;

    private Economy economy;

    public RealEstateSignInteractListener(final RealEstatePlugin plugin) {
        this.actionHandler = plugin.getActionHandler();
        this.configuration = plugin.getConfig();

        final RegisteredServiceProvider<Economy> economyRegisteredServiceProvider = Bukkit.getServicesManager().getRegistration(Economy.class);

        if (economyRegisteredServiceProvider != null) {
            this.economy = economyRegisteredServiceProvider.getProvider();
            return;
        }

        throw new RuntimeException("Failed to retrieve Economy.class!");
    }

    @EventHandler
    public void onSignInteract(final RealEstateSignInteractEvent event) {
        final Sign sign = event.getRealEstateSign();
        final Player buyer = event.getBuyer();
        final OfflinePlayer seller = event.getSeller();

        if (buyer.getUniqueId().toString().equalsIgnoreCase(seller.getUniqueId().toString())) {
            actionHandler.execute(buyer, ReplaceUtils.replaceList(true,
                    configuration.getStringList("message.claimOwner")
            ));
            return;
        }

        final double claimPrice = event.getPropertyPrice();
        if (!canAffordClaim(buyer, claimPrice)) {
            actionHandler.execute(buyer, ReplaceUtils.replaceList(true,
                    configuration.getStringList("message.missingPurchaseFunds")
            ));
            return;
        }

        final Claim claim = griefPrevention.dataStore.getClaimAt(sign.getLocation(), true, null);
        final EconomyResponse economyResponse = economy.withdrawPlayer(buyer, claimPrice);

        if (economyResponse.transactionSuccess()) {
            claim.ownerID = buyer.getUniqueId();

            economy.depositPlayer(seller, claimPrice);
            actionHandler.execute(buyer, ReplaceUtils.replaceList(true,
                    configuration.getStringList("message.purchasedClaim"),
                    "{price}", claimPrice,
                    "{seller}", seller.getName()
            ));

            if (seller.isOnline()) {
                actionHandler.execute(seller.getPlayer(), ReplaceUtils.replaceList(true,
                        configuration.getStringList("message.soldClaim"),
                        "{price}", claimPrice,
                        "{buyer}", buyer.getName()
                ));
            } else {
                MessageQueue.addToQueue(seller.getUniqueId(), ReplaceUtils.replaceList(true,
                        configuration.getStringList("message.soldClaim"),
                        "{price}", claimPrice,
                        "{buyer}", buyer.getName())
                );
            }

            event.getRealEstateSign().getBlock().setType(Material.AIR);
            Logger.logTransaction(buyer.getUniqueId(), String.format(
                    "User '%s' bought a claim from '%s' for '%s' on date '%s'",
                    buyer.getUniqueId().toString(), seller.getUniqueId().toString(), claimPrice, System.currentTimeMillis()
            ));
        }
    }

    /**
     * Checks if the buyer can afford to purchase a claim
     *
     * @param buyer Claim buyer
     * @param price Claim price
     * @return true/false if the buyer can afford to purchase the claim
     */
    private boolean canAffordClaim(final Player buyer, final double price) {
        final double balance = economy.getBalance(buyer);

        return balance >= price;
    }

}
