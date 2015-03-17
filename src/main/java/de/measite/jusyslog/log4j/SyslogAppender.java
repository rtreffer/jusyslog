package de.measite.jusyslog.log4j;

import de.measite.jusyslog.Syslog;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;

import java.io.UnsupportedEncodingException;

/**
 * Syslog appender for log4j 2.X, writes all log messages to the local syslog (if available). The program name and
 * facility can be set via setSyslogName and setFacility (or via the corresponding config properties).
 */
public class SyslogAppender extends AbstractAppender {

    public SyslogAppender(String name, Filter filter, Layout<? extends String> layout) {
        super(name, filter, layout);
    }

    public SyslogAppender(String name, Filter filter, Layout<? extends String> layout, boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
    }

    public void setSyslogName(String name) {
        Syslog.setIdent(name);
    }

    public void setFacility(String facility) {
        Syslog.setFacility(facility);
    }

    @Override
    public void append(LogEvent event) {
        if (isFiltered(event)) {
            return;
        }
        try {
            String message = new String(getLayout().toByteArray(event), "UTF-8");
            if (event.getLevel() == null) {
                Syslog.log(Syslog.LOG_DEBUG, message);
                return;
            }
            if (Level.OFF.equals(event.getLevel())) {
                Syslog.log(Syslog.LOG_EMERG, message);
                return;
            }
            if (Level.FATAL.equals(event.getLevel())) {
                Syslog.log(Syslog.LOG_CRIT, message);
                return;
            }
            if (Level.ERROR.equals(event.getLevel())) {
                Syslog.log(Syslog.LOG_ERR, message);
                return;
            }
            if (Level.WARN.equals(event.getLevel())) {
                Syslog.log(Syslog.LOG_WARNING, message);
                return;
            }
            if (Level.INFO.equals(event.getLevel())) {
                Syslog.log(Syslog.LOG_INFO, message);
                return;
            }
            Syslog.log(Syslog.LOG_DEBUG, message);
        } catch (UnsupportedEncodingException e) {
            // if you add a mad layout....
            e.printStackTrace();
        }
    }

}
