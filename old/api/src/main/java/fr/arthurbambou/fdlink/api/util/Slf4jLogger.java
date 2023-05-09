package fr.arthurbambou.fdlink.api.util;

import org.slf4j.LoggerFactory;

public class Slf4jLogger implements Logger {

    private final org.slf4j.Logger logger;

    protected Slf4jLogger(String name) {
        this.logger = LoggerFactory.getLogger(name);
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
