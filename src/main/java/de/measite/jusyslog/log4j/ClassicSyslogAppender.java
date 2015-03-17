package de.measite.jusyslog.log4j;

import de.measite.jusyslog.Syslog;
import org.apache.log4j.Level;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Syslog appender for the log4j 1.2 API. This appender will redirect all syslog messagess to the local syslog (if
 * available).
 */
public class ClassicSyslogAppender extends AppenderSkeleton {

    @Override
    protected void append(LoggingEvent event) {
        String message = event.getRenderedMessage();
        if (getLayout() == null) {
            message = getLayout().format(event);
        }
        switch (event.getLevel().toInt()) {
            case Level.ALL_INT:
            case Level.TRACE_INT:
            case Level.DEBUG_INT:
            default:
                Syslog.log(Syslog.LOG_DEBUG, message);
                return;
            case Level.OFF_INT:
                Syslog.log(Syslog.LOG_EMERG, message);
                return;
            case Level.INFO_INT:
                Syslog.log(Syslog.LOG_INFO, message);
                return;
            case Level.ERROR_INT:
                Syslog.log(Syslog.LOG_ERR, message);
                return;
            case Level.WARN_INT:
                Syslog.log(Syslog.LOG_WARNING, message);
                return;
        }
    }

    @Override
    public void close() {
        // Noop
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }

}
