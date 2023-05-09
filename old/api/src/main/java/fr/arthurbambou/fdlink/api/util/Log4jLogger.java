package fr.arthurbambou.fdlink.api.util;

import org.apache.logging.log4j.LogManager;

public class Log4jLogger implements Logger {

    private final org.apache.logging.log4j.Logger logger;

    protected Log4jLogger(String name) {
        this.logger = LogManager.getLogger(name);
    }

    @Override
    public void info(String string) {
        this.logger.info(string);
    }

    @Override
    public void warn(String string) {
        this.logger.warn(string);
    }

    @Override
    public void error(String string) {
        this.logger.error(string);
    }

    @Override
    public void error(String format, Object e) {
        this.logger.error(format, e);
    }
    @Override
    public void error(String format, Object e, Object e2) {
        this.logger.error(format, e, e2);
    }
}
