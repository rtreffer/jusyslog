JUSyslog
========

A syslog client for java, using unix domain sockets via common c library
calls.

Why
===

Syslog has been the true logging mechanism on unix/linux since about forever.
Java still lacks a good interface.

Good means in this context
- Uses only the libc calls (no need for udp/tcp/different RFCs)
- Handles most logging frameworks well
- Has a fatJar mode (drop a single jar into existing projects as an "upgrade")
- Has a friendly license (JUSyslog and all dependencies are LGPL/ASL)

How
===

JNA is used to load the c library of the system. It then wraps the functions
openlog, syslog and closelog.
Any ident is wrapped as malloc allocated memory, thus removing the key problem
of temporary strings.

Based upon a core syslog class (which is a static class) are log
appenders for some of the most common logging frameworks - namely
- log4j (1.2 and 2.X)
- java.util.logging
- logback
- tinylog

Note that those libraries are "provided" dependencies, meaning jusyslog
expects them on your classpath and never pulls the dependency into the
system.

Corolla: If you have a tool that checks all symbols in you project then
         jusyslog will fail.

The library will stop to log anything if the initialization through jna fails.
This is intentional (it should not hard-fail windows users for instance).

Building
========

```
gradle assemble
gradle fatJar
```

TODO: There is one bug with the javadoc build right now. It can't find the
      provided libraries.

Install
=======

I want to use syslog with an existing java daemon
-------------------------------------------------

Download the fat jar and drop it into the libs folder of the application,
reconfiger the system as discribed.

I want to integrate it into my java daemon
------------------------------------------

Great! Just add the dependency to your application.
JUSyslog won't add a logging framework as a dependency, you still have to
choose one (or limit yourself to syslog).

Configuration
=============

Configuration depends on your favorite logging tool.

Logback
-------

```
    <appender name="SYSLOG" class="de.measite.jusyslog.logback.SyslogAppender">
        <encoder>
            <pattern>%logger{15} - %message%n%xException{5}</pattern>
        </encoder>
        <syslogName>awesome-app</syslogName>
    </appender>

    <root level="INFO">
        <appender-ref ref="SYSLOG" />
    </root>

```
