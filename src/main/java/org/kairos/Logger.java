package org.kairos;

import static java.util.logging.Level.INFO;

public class Logger {
    private static final Logger instance = new Logger();

    public static final String ANSI_WHITE = "\u001B[0m";
    public static final String ANSI_GREEN = "\033[0;32m";
    public static final String ANSI_YELLOW = "\033[0;33m";
    public static final String ANSI_RED = "\u001B[31m";

    public enum Level {
        SUCCESS,
        INFO,
        DEBUG,
        WARNING,
        ERROR,
    }

    public static Logger getInstance() {
        return instance;
    }

    public void success(String msg) {}

    public void log(String msg) {
        log(msg, Level.INFO);
    }

    public void log(String msg, Level level) {
        msg = "[" + level + "] " + msg;

        switch(level) {
            case SUCCESS, INFO -> System.out.println(ANSI_GREEN + msg + ANSI_WHITE);
            case DEBUG, WARNING -> System.out.println(ANSI_YELLOW + msg + ANSI_WHITE);
            default -> System.out.println(ANSI_RED + msg + ANSI_WHITE);
        }
    }
}
