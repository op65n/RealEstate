package tech.op65n.realestate.listener.registerable;

import tech.op65n.realestate.RealEstatePlugin;
import tech.op65n.realestate.Registerable;
import tech.op65n.realestate.listener.RealEstateSignCreateListener;
import tech.op65n.realestate.listener.RealEstateSignInteractListener;
import tech.op65n.realestate.listener.base.SignCreateListener;
import tech.op65n.realestate.listener.base.SignInteractListener;
import tech.op65n.realestate.listener.queue.PlayerJoinListener;
import tech.op65n.realestate.namespace.PluginKey;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.Arrays;

public final class ListenerRegisterable implements Registerable {

    @Override
    public void register(final RealEstatePlugin plugin) {
        PluginKey.setPlugin(plugin);

        registerListeners(plugin,
                new SignCreateListener(plugin.getConfig()),
                new SignInteractListener(),

                new RealEstateSignCreateListener(plugin),
                new RealEstateSignInteractListener(plugin),

                new PlayerJoinListener(plugin)
        );
    }

    private void registerListeners(final RealEstatePlugin plugin, final Listener... listeners) {
        Arrays.stream(listeners).forEach(it -> Bukkit.getPluginManager().registerEvents(it, plugin));
    }

}
