package tech.op65n.realestate.queue.registerable;

import tech.op65n.realestate.RealEstatePlugin;
import tech.op65n.realestate.Registerable;
import tech.op65n.realestate.queue.MessageQueue;

import java.io.IOException;

public final class QueueRegisterable implements Registerable {

    /**
     * Loads our stored queued messages
     * {@link MessageQueue#loadFromFile(RealEstatePlugin)}
     *
     * @param plugin Our plugin instance
     */
    @Override
    public void register(final RealEstatePlugin plugin) {
        MessageQueue.loadFromFile(plugin);
    }

    /**
     * Saves our queued message
     * {@link MessageQueue#saveToFile(RealEstatePlugin)}
     *
     * @param plugin Our plugin instance
     */
    @Override
    public void unregister(final RealEstatePlugin plugin) {
        try {
            MessageQueue.saveToFile(plugin);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }
}
