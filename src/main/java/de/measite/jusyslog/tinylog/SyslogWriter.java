package de.measite.jusyslog.tinylog;

import de.measite.jusyslog.Syslog;
import org.pmw.tinylog.Configuration;
import org.pmw.tinylog.LogEntry;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.writers.LogEntryValue;
import org.pmw.tinylog.writers.Writer;

import java.util.HashSet;
import java.util.Set;

/**
 * Syslog writer for tinylog.
 */
public class SyslogWriter implements Writer {
    @Override
    public Set<LogEntryValue> getRequiredLogEntryValues() {
        HashSet<LogEntryValue> set = new HashSet<LogEntryValue>();
        set.add(LogEntryValue.RENDERED_LOG_ENTRY);
        return set;
    }

    @Override
    public void init(Configuration configuration) throws Exception {
    }

    @Override
    public void write(LogEntry logEntry) throws Exception {
        String message = logEntry.getRenderedLogEntry();
        if (logEntry.getLevel() == null) {
            Syslog.log(Syslog.LOG_DEBUG, message);
            return;
        }
        if (Level.OFF.equals(logEntry.getLevel())) {
            Syslog.log(Syslog.LOG_EMERG, message);
            return;
        }
        if (Level.ERROR.equals(logEntry.getLevel())) {
            Syslog.log(Syslog.LOG_ERR, message);
            return;
        }
        if (Level.WARNING.equals(logEntry.getLevel())) {
            Syslog.log(Syslog.LOG_WARNING, message);
            return;
        }
        if (Level.INFO.equals(logEntry.getLevel())) {
            Syslog.log(Syslog.LOG_INFO, message);
            return;
        }
        Syslog.log(Syslog.LOG_DEBUG, message);
    }

    @Override
    public void flush() throws Exception {
        // noop
    }

    @Override
    public void close() throws Exception {
        // noop
    }
}
