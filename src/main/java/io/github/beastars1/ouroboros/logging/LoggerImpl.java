package io.github.beastars1.ouroboros.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerImpl implements Logger{
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss.SSS");

    private final String name;

    public LoggerImpl(String name) {
        this.name = name;
    }

    @Override
    public void info(String s) {
        print("info", s);
    }

    @Override
    public void debug(String s) {
        print("debug", s);
    }

    @Override
    public void warn(String s) {
        print("warn", s);
    }

    @Override
    public void error(String s) {
        print("error", s);
    }

    @Override
    public void error(String s, Throwable t) {
        print("error", s);
        t.printStackTrace();
    }

    private void print(String level, String msg) {
        String threadName = Thread.currentThread().getName();
        String time = LocalDateTime.now().format(FORMAT);
        String s = String.format("%s [%s] [%s] [%s] %s", time, level, threadName, name, msg);
        System.out.println(s);
    }
}
