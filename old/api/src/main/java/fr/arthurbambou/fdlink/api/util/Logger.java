package fr.arthurbambou.fdlink.api.util;

public interface Logger {

    void info(String string);

    void warn(String string);

    void error(String string);

    void error(String format, Object e);
    void error(String format, Object e, Object e2);
}
