package com.github.frcsty.realestate.queue.registerable;

import com.github.frcsty.realestate.RealEstatePlugin;
import com.github.frcsty.realestate.Registerable;
import com.github.frcsty.realestate.queue.MessageQueue;

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
