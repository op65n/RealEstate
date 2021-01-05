package com.github.frcsty.realestate;

import com.github.frcsty.frozenactions.wrapper.ActionHandler;
import com.github.frcsty.realestate.listener.registerable.ListenerRegisterable;
import com.github.frcsty.realestate.logger.registerable.LoggerRegisterable;
import com.github.frcsty.realestate.queue.registerable.QueueRegisterable;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class RealEstatePlugin extends JavaPlugin {

    private final ActionHandler actionHandler = new ActionHandler(this);

    private final Set<Registerable> registerables = new HashSet<>(Arrays.asList(
            new QueueRegisterable(),
            new LoggerRegisterable(),
            new ListenerRegisterable()
    ));

    @Override
    public void onEnable() {
        saveDefaultConfig();

        actionHandler.loadDefaults(true);
        registerables.forEach(it -> it.register(this));
    }

    @Override
    public void onDisable() {
        reloadConfig();

        registerables.forEach(it -> it.unregister(this));
    }

    public ActionHandler getActionHandler() {
        return this.actionHandler;
    }
}
