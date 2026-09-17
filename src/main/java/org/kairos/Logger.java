package org.kairos;

import java.io.*;
import java.sql.Timestamp;
import org.slf4j.LoggerFactory;

public class Logger {
    private static final org.slf4j.Logger LOG =
            LoggerFactory.getLogger(Logger.class);

    public static void info(String message) {
        LOG.info(message);
    }

    public static void warn(String message) { LOG.warn(message); }

    public static void error(String message, Throwable cause) { LOG.error(message, cause); }

    private Logger() {}
}
