package tech.op65n.realestate.listener.base;

import tech.op65n.realestate.listener.event.RealEstateSignInteractEvent;
import tech.op65n.realestate.namespace.PluginKey;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.PluginManager;

public final class SignInteractListener implements Listener {

    private final PluginManager pluginManager;

    public SignInteractListener() {
        this.pluginManager = Bukkit.getPluginManager();
    }

    @EventHandler
    public void onSignInteract(final PlayerInteractEvent event) {
        final Block block = event.getClickedBlock();
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (isNotRealEstateSign(block)) return;

        pluginManager.callEvent(new RealEstateSignInteractEvent(
                event.getPlayer(), block
        ));
    }

    /**
     * Checks if a clicked block is a Real Estate sign
     *
     * @param block Clicked block
     * @return status on whether a block is a RealEstate sign
     */
    private boolean isNotRealEstateSign(final Block block) {
        if (block == null) return true;
        if (!(block.getState() instanceof Sign)) return true;

        final Sign sign = (Sign) block.getState();
        final PersistentDataContainer container = sign.getPersistentDataContainer();
        final String propertyOwner = container.get(PluginKey.getKey("RealEstate_PropertyOwner"), PersistentDataType.STRING);

        return propertyOwner == null || propertyOwner.isEmpty();
    }

}
