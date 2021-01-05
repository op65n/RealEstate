package com.github.frcsty.realestate.logger;

import java.util.logging.Level;

public final class Logger {

    private static java.util.logging.Logger logger;

    public static void setLogger(final java.util.logging.Logger newLogger) {
        logger = newLogger;
    }

    public static void logInfo(final String text) {
        logger.log(Level.INFO, text);
    }

}
