package server.com.hsbc.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Formatter;

public class Log {
    public Logger logger;

    // This constructor is for non-main classes
    public Log(Class c) {
      logger = LogManager.getLogger(c.getName());
    }

    public void debug(Object message) {
        logger.debug(message);
    }

    public void debug(String message) {
        logger.debug(message);
    }

    public void debug(Throwable throwable) {
        logger.debug("", throwable);
    }

    public void debug(String message, Throwable throwable) {
        logger.debug(message, throwable);
    }

    public void debug(String format, Object... args) {
      StringBuilder sb = new StringBuilder(format.length() + 32);
      Formatter f = new Formatter(sb);
      f.format(format, args);
      logger.debug(f.toString());
    }

    public void info(Object message) {
        logger.info(message);
    }

    public void info(String format, Object... args) {
      StringBuilder sb = new StringBuilder(format.length() + 32);
      Formatter f = new Formatter(sb);
      f.format(format, args);
      logger.info(f.toString());
    }

    public void info(String format, Throwable throwable, Object...args) {
      StringBuilder sb = new StringBuilder(format.length() + 32);
      Formatter f = new Formatter(sb);
      f.format(format, args);
      logger.info(f.toString(), throwable);
    }

    public void info(String message) {
        logger.info(message);
        System.out.println(message);
    }

    public void info(Throwable throwable) {
        logger.info("", throwable);
    }

    public void info(String message, Throwable throwable) {
        logger.info(message, throwable);
    }

    public void warn(Object message) {
        logger.warn(message);
    }

    public void warn(String message) {
        logger.warn(message);
    }

    public void warn(Throwable throwable) {
        logger.warn("", throwable);
    }

    public void warn(String message, Throwable throwable) {
        logger.warn(message, throwable);
    }

    public void warn(String format, Throwable throwable, Object...args) {
      StringBuilder sb = new StringBuilder(format.length() + 32);
      Formatter f = new Formatter(sb);
      f.format(format, args);
      logger.warn(f.toString(), throwable);
    }

    public void warn(String format, Object... args) {
      StringBuilder sb = new StringBuilder(format.length() + 32);
      Formatter f = new Formatter(sb);
      f.format(format, args);
      logger.warn(f.toString());
    }

    public void error(Object message) {
        logger.error(message);
    }

    public void error(String message) {
        logger.error(message);
    }

    public void error(Throwable throwable) {
        logger.error("", throwable);
    }

    public void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }

    public void error(Throwable throwable, String message) {
        logger.error(message, throwable);
    }

    public void error(String format, Throwable throwable, Object...args) {
      StringBuilder sb = new StringBuilder(format.length() + 32);
      Formatter f = new Formatter(sb);
      f.format(format, args);
      logger.error(f.toString(), throwable);
    }

    public void error(String format, Object...args) {
      StringBuilder sb = new StringBuilder(format.length() + 32);
      Formatter f = new Formatter(sb);
      f.format(format, args);
      logger.error(f.toString());
    }

    public void fatal(String message, Throwable throwable) {
        logger.fatal(message, throwable);
    }

    public void fatal(Throwable throwable) {
      logger.fatal("", throwable);
    }

    public void fatal(String message) {
      logger.fatal(message);
    }

    public void db(String message) {
      logger.info(message);
    }


}
