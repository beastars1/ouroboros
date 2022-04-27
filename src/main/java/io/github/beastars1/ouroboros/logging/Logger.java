package io.github.beastars1.ouroboros.logging;

public interface Logger {
    void info(String s);

    void debug(String s);

    void warn(String s);

    void error(String s);

    void error(String s, Throwable t);
}
