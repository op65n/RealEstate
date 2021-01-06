package tech.op65n.realestate.queue.registerable;

import tech.op65n.realestate.RealEstatePlugin;
import tech.op65n.realestate.Registerable;
import tech.op65n.realestate.queue.MessageQueue;

import java.io.IOException;

public final class QueueRegisterable implements Registerable {

    @Override
    public void register(final RealEstatePlugin plugin) {
        MessageQueue.loadFromFile(plugin);
    }

    @Override
    public void unregister(final RealEstatePlugin plugin) {
        try {
            MessageQueue.saveToFile(plugin);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }
}
