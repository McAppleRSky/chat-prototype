package rarus.chat.server.main;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import rarus.chat.server.main.config.ConfigComponent;
import rarus.chat.server.main.config.ConfigComponentImpl;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

@SuppressWarnings("deprecation")
class MainConfiguration {

    protected static Logger configureLogger(Class c){
        ConfigurationBuilder<BuiltConfiguration> cfgBuilder = ConfigurationBuilderFactory.newConfigurationBuilder();
        cfgBuilder
                .add(cfgBuilder
                        .newAppender("Stdout", "CONSOLE")
                        .add(cfgBuilder
                                .newLayout("PatternLayout")
                                .addAttribute("pattern", "%d [%t] %-5level: %msg%n%throwable"))
                        .addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT))
                /*.add(cfgBuilder
                        .newAppender("fileAppender", "FILE")
                        .add(cfgBuilder
                                .newLayout("PatternLayout")
                                .addAttribute("pattern", "%d [%t] %-5level: %msg%n%throwable"))
                        .addAttribute("fileName", "log/server.log"))*/
        ;
        RootLoggerComponentBuilder rootLoggerBuilder = cfgBuilder
                .newRootLogger(Level.ALL)
                .add(cfgBuilder
                        .newAppenderRef("Stdout")
                        .addAttribute("level", Level.INFO))
                /*.add(cfgBuilder
                        .newAppenderRef("fileAppender")
                        .addAttribute("level", Level.INFO))*/
                ;
        LoggerContext loggerContext = Configurator
                .initialize(cfgBuilder
                        .add(rootLoggerBuilder)
                        .build());
        return loggerContext.getLogger(c.getName());
    }

    protected static ConfigComponent configureContext() {
        ConfigComponent result = null;
        Properties properties = new Properties();
        try (InputStream input = MainConfiguration.class.getClassLoader().getResourceAsStream("chat.properties")) {
            properties.load(input);
            result = new ConfigComponentImpl("server-port", properties.getProperty("server-port"));
            result.setProperty("redis-host", properties.getProperty("redis-host"));
            result.setProperty("redis-port", properties.getProperty("redis-port"));
            result.setProperty("redis-timeout", properties.getProperty("redis-timeout"));
            result.setProperty("chat-room", properties.getProperty("chat-room"));
            result.setProperty("date-time-format", properties.getProperty("date-time-format"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try (InputStream input = MainConfiguration.class.getClassLoader().getResourceAsStream("chat-hide.properties")) {
            properties.load(input);
            result.setProperty("redis-pass", properties.getProperty("redis-pass"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    protected static JedisPool configureJedis() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(128);
        jedisPoolConfig.setMaxIdle(128);
        jedisPoolConfig.setMinIdle(16);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(true);
        jedisPoolConfig.setTestWhileIdle(true);
        jedisPoolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
        jedisPoolConfig.setNumTestsPerEvictionRun(3);
        jedisPoolConfig.setBlockWhenExhausted(true);
        ConfigComponentImpl config = (ConfigComponentImpl) Main.context.get(ConfigComponentImpl.class);
        String host = config.getProperty("redis-host");
        int port = Integer.parseInt(config.getProperty("redis-port"));
        int timeout = Integer.parseInt(config.getProperty("redis-timeout"));
        String password = config.getProperty("redis-pass");
        return new JedisPool(jedisPoolConfig, host, port, timeout, password);
    }

}
