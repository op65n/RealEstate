package com.github.frcsty.realestate.logger.registerable;

import com.github.frcsty.realestate.RealEstatePlugin;
import com.github.frcsty.realestate.Registerable;
import com.github.frcsty.realestate.logger.Logger;

public final class LoggerRegisterable implements Registerable {

    @Override
    public void register(final RealEstatePlugin plugin) {
        Logger.setLogger(plugin.getLogger());
        Logger.setPlugin(plugin);
    }

}
