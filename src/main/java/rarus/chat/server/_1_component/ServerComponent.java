package rarus.chat.server._1_component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import rarus.chat.server._2_createConnection.ConnectionFactory;
import rarus.chat.server._2_createConnection.ConnectionHandler;
import rarus.chat.server._2_createConnection.creator.ConnectionCreator;
import rarus.chat.server._2_createConnection.creator.FactoryConfigurator;
import rarus.chat.server._3_service.ServerClientServiceImpl;
import rarus.chat.server.main.Main;
import rarus.chat.server.main.config.ConfigComponent;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerComponent implements FactoryConfigurator {
    ConnectionFactory connectionFactory = new ConnectionFactory();
    private final Logger LOGGER = LogManager.getLogger(ServerComponent.class);
    private final ConfigComponent configComponent = (ConfigComponent) Main.context.get(ConfigComponent.class);
    private int port = 0;
//    private ConnectionCreator factory = new ConnectionFactory();
    /*public ServerComponent(ConnectionCreator factory) { this.factory = factory; }*/
    {
        try {
            port = Integer.parseInt(configComponent.getProperty("server-port"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
    private boolean waitAcceptClient = true;

    public ServerComponent() {
        if (port>0) {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
//            Main.context.put(ServerSocket.class, serverSocket);
                LOGGER.info("Server socked started");
                serverClient(serverSocket);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                LOGGER.info("Server closed");
            }
        } else {
            LOGGER.error("Host port not set");
        }
    }

    private void serverClient(ServerSocket serverSocket) {
        while (waitAcceptClient) {
            try (Socket clientSocket = serverSocket.accept()) {
                LOGGER.info("Client socket created");
//                configure(connectionFactory);
                new ServerClientServiceImpl(clientSocket);
//                factory.accept(clientSocket);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                LOGGER.info("Client socket closed");
            }
        }
    }

    @Override
    public void configure(ConnectionCreator factory) {
        factory.setCreator((clientSocket) -> new ConnectionHandler((Socket) clientSocket));
    }

}
