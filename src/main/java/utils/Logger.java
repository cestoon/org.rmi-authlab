package utils;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class Logger {

    public static java.util.logging.Logger getLogger(Class<?> clazz) {
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(clazz.getName());

        // Create a console handler
        ConsoleHandler handler = new ConsoleHandler();

        // Create a formatter that includes a timestamp and other details
        handler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                return String.format("%1$tF %1$tT %2$s %3$s %n",
                        record.getMillis(),
                        record.getLevel().getLocalizedName(),
                        formatMessage(record));
            }
        });

        // Add the handler to the logger
        logger.addHandler(handler);

        // Set the log level to INFO
        logger.setLevel(Level.INFO);

        return logger;
    }
}
