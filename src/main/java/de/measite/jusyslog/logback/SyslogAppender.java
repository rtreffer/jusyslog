package de.measite.jusyslog.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import de.measite.jusyslog.Syslog;

public class SyslogAppender extends AppenderBase<ILoggingEvent> {

    public void setSyslogName(String name) {
        Syslog.setIdent(name);
    }

    public void setFacility(String facility) {
        Syslog.setFacility(facility);
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        Level level = eventObject.getLevel();
        if (level.equals(Level.OFF)) {
            Syslog.log(Syslog.LOG_EMERG, eventObject.getMessage());
            return;
        }
        if (level.equals(Level.INFO)) {
            Syslog.log(Syslog.LOG_INFO, eventObject.getMessage());
            return;
        }
        if (level.equals(Level.WARN)) {
            Syslog.log(Syslog.LOG_WARNING, eventObject.getMessage());
            return;
        }
        if (level.equals(Level.ERROR)) {
            Syslog.log(Syslog.LOG_ERR, eventObject.getMessage());
            return;
        }
        Syslog.log(Syslog.LOG_DEBUG, eventObject.getMessage());
    }

}
