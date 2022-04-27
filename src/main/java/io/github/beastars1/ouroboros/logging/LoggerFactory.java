package io.github.beastars1.ouroboros.logging;

public class LoggerFactory {
    public static Logger getLogger(String name) {
        return new LoggerImpl(name);
    }

    public static Logger getLogger(Class<?> c) {
        return new LoggerImpl(c.getName());
    }
}
