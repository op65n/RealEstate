package tech.op65n.realestate;

import com.github.frcsty.frozenactions.wrapper.ActionHandler;
import org.bukkit.plugin.java.JavaPlugin;
import tech.op65n.realestate.listener.registerable.ListenerRegisterable;
import tech.op65n.realestate.logger.registerable.LoggerRegisterable;
import tech.op65n.realestate.queue.registerable.QueueRegisterable;

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

    /**
     * Retrieves our loaded {@link com.github.frcsty.frozenactions.wrapper.ActionHandler} instance
     *
     * @return loaded {@link ActionHandler}
     */
    public ActionHandler getActionHandler() {
        return this.actionHandler;
    }
}
