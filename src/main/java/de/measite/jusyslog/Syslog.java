package de.measite.jusyslog;

import com.sun.jna.Memory;
import com.sun.jna.Native;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Static class to handle syslog access.
 */
public final class Syslog {

    public final static int LOG_EMERG   = 0;
    public final static int LOG_ALERT   = 1;
    public final static int LOG_CRIT    = 2;
    public final static int LOG_ERR     = 3;
    public final static int LOG_WARNING = 4;
    public final static int LOG_NOTICE  = 5;
    public final static int LOG_INFO    = 6;
    public final static int LOG_DEBUG   = 7;

    public final static int LOG_KERN     = (0<<3);
    public final static int LOG_USER     = (1<<3);
    public final static int LOG_MAIL     = (2<<3);
    public final static int LOG_DAEMON   = (3<<3);
    public final static int LOG_AUTH     = (4<<3);
    public final static int LOG_SYSLOG   = (5<<3);
    public final static int LOG_LPR      = (6<<3);
    public final static int LOG_NEWS     = (7<<3);
    public final static int LOG_UUCP     = (8<<3);
    public final static int LOG_CRON     = (9<<3);
    public final static int LOG_AUTHPRIV = (10<<3);
    public final static int LOG_FTP      = (11<<3);
    public final static int LOG_LOCAL0   = (16<<3);
    public final static int LOG_LOCAL1   = (17<<3);
    public final static int LOG_LOCAL2   = (18<<3);
    public final static int LOG_LOCAL3   = (19<<3);
    public final static int LOG_LOCAL4   = (20<<3);
    public final static int LOG_LOCAL5   = (21<<3);
    public final static int LOG_LOCAL6   = (22<<3);
    public final static int LOG_LOCAL7   = (23<<3);

    public final static int LOG_PID    = 0x01;
    public final static int LOG_CONS   = 0x02;
    public final static int LOG_ODELAY = 0x04;
    public final static int LOG_NDELAY = 0x08;
    public final static int LOG_NOWAIT = 0x10;
    public final static int LOG_PERROR = 0x20;

    private static final SyslogLibrary library;
    static {
        SyslogLibrary lib = null;
        try {
            lib = (SyslogLibrary) Native.loadLibrary("c", SyslogLibrary.class);
        } catch (Throwable t) {
            // this should barely ever happen, and if it happens it's most likely
            // so trying to run it on a non linux/unix machine
            t.printStackTrace();
        }
        library = lib;
    }

    private static Memory ident = null;
    private static String name = null;

    private static int facility = LOG_LOCAL0;

    private static int options = LOG_NOWAIT;

    private Syslog() {
    }

    public static void setIdent(String name) {
        if (library == null) {
            return;
        }
        synchronized (library) {
            Syslog.name = name;
            if (name == null) {
                ident = null;
                library.closelog(); // reset log name
                return;
            }
            try {
                int size = name.getBytes("UTF-8").length + 1;
                ident = new Memory(size);
                ident.setString(0,name,"UTF-8");
                library.openlog(ident, options, facility);
            } catch (UnsupportedEncodingException e) {
                // should never happen
                e.printStackTrace();
            }
        }
    }

    public static void setFacility(String facility) {
        if (library == null) {
            return;
        }
        facility = facility.toUpperCase();
        if ("KERN".equals(facility)) { Syslog.setFacility(Syslog.LOG_KERN); return; }
        if ("USER".equals(facility)) { Syslog.setFacility(Syslog.LOG_USER); return; }
        if ("MAIL".equals(facility)) { Syslog.setFacility(Syslog.LOG_MAIL); return; }
        if ("DAEMON".equals(facility)) { Syslog.setFacility(Syslog.LOG_DAEMON); return; }
        if ("AUTH".equals(facility)) { Syslog.setFacility(Syslog.LOG_AUTH); return; }
        if ("SYSLOG".equals(facility)) { Syslog.setFacility(Syslog.LOG_SYSLOG); return; }
        if ("LPR".equals(facility)) { Syslog.setFacility(Syslog.LOG_LPR); return; }
        if ("NEWS".equals(facility)) { Syslog.setFacility(Syslog.LOG_NEWS); return; }
        if ("UUCP".equals(facility)) { Syslog.setFacility(Syslog.LOG_UUCP); return; }
        if ("CRON".equals(facility)) { Syslog.setFacility(Syslog.LOG_CRON); return; }
        if ("AUTHPRIV".equals(facility)) { Syslog.setFacility(Syslog.LOG_AUTHPRIV); return; }
        if ("FTP".equals(facility)) { Syslog.setFacility(Syslog.LOG_FTP); return; }
        if ("LOCAL0".equals(facility)) { Syslog.setFacility(Syslog.LOG_LOCAL0); return; }
        if ("LOCAL1".equals(facility)) { Syslog.setFacility(Syslog.LOG_LOCAL1); return; }
        if ("LOCAL2".equals(facility)) { Syslog.setFacility(Syslog.LOG_LOCAL2); return; }
        if ("LOCAL3".equals(facility)) { Syslog.setFacility(Syslog.LOG_LOCAL3); return; }
        if ("LOCAL4".equals(facility)) { Syslog.setFacility(Syslog.LOG_LOCAL4); return; }
        if ("LOCAL5".equals(facility)) { Syslog.setFacility(Syslog.LOG_LOCAL5); return; }
        if ("LOCAL6".equals(facility)) { Syslog.setFacility(Syslog.LOG_LOCAL6); return; }
        if ("LOCAL7".equals(facility)) { Syslog.setFacility(Syslog.LOG_LOCAL7); return; }
    }

    public static void setFacility(int facility) {
        if (library == null) {
            return;
        }
        synchronized (library) {
            Syslog.facility = facility;
            if (name != null) {
                setIdent(name);
            }
        }
    }

    public static void setOptions(int options) {
        if (library == null) {
            return;
        }
        synchronized (library) {
            Syslog.options = options;
            if (name != null) {
                setIdent(name);
            }
        }
    }

    public static void log(int priority, String message) {
        if (library == null) {
            return;
        }
        synchronized (library) {
            library.syslog(priority, "%s", message);
        }
    }
}
