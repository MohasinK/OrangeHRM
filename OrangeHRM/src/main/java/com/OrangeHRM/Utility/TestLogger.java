package com.OrangeHRM.Utility;

import org.testng.log4testng.Logger;

public class TestLogger {
    private final Logger logger;

    public TestLogger(Class<?> clazz) {
        this.logger = Logger.getLogger(clazz);
    }

    public void start(String message) {
        logger.info("START: " + message);
    }

    public void end(String message) {
        logger.info("END: " + message);
    }

    public void info(String message) {
        logger.info(message);
    }

    public void error(String message) {
        logger.error(message);
    }

    public void error(String message, Throwable t) {
        logger.error(message, t);
    }
}
