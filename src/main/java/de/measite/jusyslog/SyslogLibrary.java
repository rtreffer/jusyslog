package de.measite.jusyslog;

import com.sun.jna.Library;
import com.sun.jna.Memory;

public interface SyslogLibrary extends Library {

    void openlog(Memory ident, int option, int facility);

    void syslog(int priority, String format, String message);

    void closelog();

}
