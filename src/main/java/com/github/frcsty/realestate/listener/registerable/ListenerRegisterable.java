package com.github.frcsty.realestate.listener.registerable;

import com.github.frcsty.realestate.RealEstatePlugin;
import com.github.frcsty.realestate.Registerable;
import com.github.frcsty.realestate.listener.RealEstateSignCreateListener;
import com.github.frcsty.realestate.listener.RealEstateSignInteractListener;
import com.github.frcsty.realestate.listener.base.SignCreateListener;
import com.github.frcsty.realestate.listener.base.SignInteractListener;
import com.github.frcsty.realestate.listener.queue.PlayerJoinListener;
import com.github.frcsty.realestate.namespace.PluginKey;
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
