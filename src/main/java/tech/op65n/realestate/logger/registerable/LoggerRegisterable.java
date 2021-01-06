package tech.op65n.realestate.logger.registerable;

import tech.op65n.realestate.RealEstatePlugin;
import tech.op65n.realestate.Registerable;
import tech.op65n.realestate.logger.Logger;

public final class LoggerRegisterable implements Registerable {

    @Override
    public void register(final RealEstatePlugin plugin) {
        Logger.setLogger(plugin.getLogger());
        Logger.setPlugin(plugin);
    }

}
