package tech.op65n.realestate.listener.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.SignChangeEvent;

import java.util.Arrays;
import java.util.List;

public final class RealEstateSignCreateEvent extends Event {

    private final SignChangeEvent event;
    private final Player player;
    private final List<String> lines;

    public RealEstateSignCreateEvent(final SignChangeEvent event) {
        this.event = event;
        this.player = event.getPlayer();
        this.lines = Arrays.asList(event.getLines());
    }

    /**
     * Returns the player which has created a RealEstate sign
     *
     * @return RealEstate sign creator
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Returns the sign's contents
     *
     * @return Sign contents
     */
    public List<String> getLines() {
        return this.lines;
    }

    /**
     * Returns the base event
     *
     * @return {@link SignChangeEvent}
     */
    public SignChangeEvent getBaseEvent() {
        return this.event;
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
