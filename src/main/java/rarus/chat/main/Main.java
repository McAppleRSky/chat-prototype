package rarus.chat.main;

import org.apache.logging.log4j.Logger;
import rarus.chat.component.ConfigComponent;
import rarus.chat.component.ServerComponent;
import rarus.chat.component.ServerComponentImpl;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.net.ServerSocket;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class Main extends MainConfiguration{

    private static final Logger LOGGER = configureLogger(Main.class);

    public static final Map context = new HashMap();

    static {
        /*{String publicHtml = Main.class.getClassLoader().getResource("public_html").getPath();
            publicHtml = publicHtml.substring(1, publicHtml.length()); // windows path fix
            context.put("public_html", publicHtml);}*/
        context.put(ConfigComponent.class, configureContext());
        context.put(DateTimeFormatter.class, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        context.put(JedisPool.class, configureJedis());
    }

    public static void main(String[] args) {
        LOGGER.info("Chat starting");
        new ServerComponentImpl().running();
        LOGGER.info("Chat finished");
//        ClientService server = new ClientServiceImpl();
/*        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder( new ChatServlet() ),ChatServlet.URL);
        ResourceHandler resourceHandler = new ResourceHandler();
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, servletContextHandler});
        Config config = (Config)context.get(Config.class);
        int port = Integer.parseInt(config.getProperty("server-port"));
        Server server = new Server(port);
        server.setHandler(handlers);
        System.out.println("Server started");
        server.start();
        server.join();*/
    }

}
