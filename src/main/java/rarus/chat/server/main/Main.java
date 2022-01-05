package rarus.chat.server.main;

import org.apache.logging.log4j.Logger;
import rarus.chat.server._1_component.ServerComponent;
import rarus.chat.server._2_createConnection.ConnectionFactory;
import rarus.chat.server._2_createConnection.creator.ConnectionCreator;
import rarus.chat.server.main.config.ConfigComponent;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


public class Main extends MainConfiguration{

    private static final Logger LOGGER = configureLogger(Main.class);

    public static final Map context = new HashMap();

    static {
        context.put(ConfigComponent.class, configureContext());
        context.put( DateTimeFormatter.class, DateTimeFormatter.ofPattern(
                ( (ConfigComponent)context.get(ConfigComponent.class) ).getProperty("date-time-format") )
        );
        context.put(ConnectionCreator.class, new ConnectionFactory());
//        context.put(JedisPool.class, configureJedis());
    }

    public static void main(String[] args) {
        LOGGER.info("Chat starting");
        new ServerComponent();
//        Main.context.put(ServerComponent.class, serverComponent);
//        serverComponent.execute();
        LOGGER.info("Chat finished");
    }

}
