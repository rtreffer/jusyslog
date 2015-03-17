package de.measite.jusyslog.logging;

import de.measite.jusyslog.Syslog;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Syslog handler for java.util.logging. This class will redirect all messages to the local syslog (if available).
 */
public class SyslogHandler extends Handler {

    @Override
    public void publish(LogRecord logRecord) {
        if (logRecord == null) {
            return;
        }
        Level level = logRecord.getLevel();
        String message = (getFormatter() == null) ? logRecord.getMessage() : getFormatter().format(logRecord);
        if (Level.OFF.equals(level)) {
            Syslog.log(Syslog.LOG_EMERG, message);
            return;
        }
        if (Level.SEVERE.equals(level)) {
            Syslog.log(Syslog.LOG_CRIT, message);
            return;
        }
        if (Level.WARNING.equals(level)) {
            Syslog.log(Syslog.LOG_WARNING, message);
            return;
        }
        if (Level.INFO.equals(level)) {
            Syslog.log(Syslog.LOG_INFO, message);
            return;
        }
        if (Level.CONFIG.equals(level) || Level.FINE.equals(level)) {
            Syslog.log(Syslog.LOG_NOTICE, message);
            return;
        }
        Syslog.log(Syslog.LOG_DEBUG, message);
    }

    @Override
    public void flush() {
        // noop
    }

    @Override
    public void close() throws SecurityException {
        // noop
    }

    public void setSyslogName(String name) {
        Syslog.setIdent(name);
    }
    public void setFacility(String facility) {
        Syslog.setFacility(facility);
    }
}
