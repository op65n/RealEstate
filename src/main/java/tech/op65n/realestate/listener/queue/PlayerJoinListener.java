package tech.op65n.realestate.listener.queue;

import com.github.frcsty.frozenactions.wrapper.ActionHandler;
import tech.op65n.realestate.RealEstatePlugin;
import tech.op65n.realestate.queue.MessageQueue;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerJoinListener implements Listener {

    private final ActionHandler actionHandler;

    public PlayerJoinListener(final RealEstatePlugin plugin) {
        this.actionHandler = plugin.getActionHandler();
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        MessageQueue.executeForReceiver(
                actionHandler,
                player
        );
    }
}
