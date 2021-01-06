package tech.op65n.realestate.listener.event;

import tech.op65n.realestate.namespace.PluginKey;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;
import java.util.UUID;

public final class RealEstateSignInteractEvent extends Event {

    private final Player player;
    private final Sign sign;

    private final PersistentDataContainer container;

    public RealEstateSignInteractEvent(final Player player, final Block block) {
        this.player = player;
        this.sign = (Sign) block.getState();

        this.container = sign.getPersistentDataContainer();
    }

    /**
     * Returns the property buyer
     *
     * @return Property buyer
     */
    public Player getBuyer() {
        return this.player;
    }

    /**
     * Returns the property seller
     *
     * @return Property seller
     */
    public OfflinePlayer getSeller() {
        final String sellerUUID = container.get(PluginKey.getKey("RealEstate_PropertyOwner"), PersistentDataType.STRING);

        return Bukkit.getOfflinePlayer(UUID.fromString(Objects.requireNonNull(sellerUUID)));
    }

    /**
     * Returns the price the property was sold to
     *
     * @return Property price
     */
    public double getPropertyPrice() {
        final Double propertyPrice = container.get(PluginKey.getKey("RealEstate_PropertyPrice"), PersistentDataType.DOUBLE);

        return propertyPrice == null ? 0 : propertyPrice;
    }

    /**
     * Returns the sign used for the transaction
     *
     * @return RealEstate sign
     */
    public Sign getRealEstateSign() {
        return this.sign;
    }

    // Default Event Stuff

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

}
