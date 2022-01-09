package rarus.chat.server._frontEnd;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rarus.chat.server._frontEnd.connectionCreator.ConnectionFactory;
import rarus.chat.server._frontEnd.connectionCreator.ConnectionFactoryImpl;
import rarus.chat.server._frontEnd.connectionCreator.FactoryConfigurator;
import rarus.chat.server.backEnd.ClientConnection;
import rarus.chat.server.main.Main;
import rarus.chat.server.main.config.ConfigComponent;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class ServerComponent extends ServerComponentAbstract implements Runnable, FactoryConfigurator {

    private final Logger
            LOGGER = LogManager.getLogger(ServerComponent.class);
//    private final ConnectionService connectionService = (ConnectionService)Main.context.get(ConnectionService.class);
    private volatile ServerSocket serverSocket;
    private Object sync = new Object();
    private ConnectionFactory connectionFactory = new ConnectionFactoryImpl();

    @Override
    public void configure(ConnectionFactory connectionFactory) {
        this.connectionFactory.setCreator((Socket clientSocket) -> {
            LOGGER.info("New ClientConnection instance creating...");
            new ClientConnection(clientSocket);
        });
    }

    public ServerComponent() {
        super();
        configure(connectionFactory);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            LOGGER.info("Server socked started at " + port);
            this.serverSocket=serverSocket;
            synchronized (sync) {
                while (waitAcceptClient) {
                    new Thread(this).start();
                    sync.wait();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            LOGGER.info("Server closed");
        }
    }

    @Override
    public void run() {
        try (Socket clientSocket = serverSocket.accept()) {
            LOGGER.info("Client socket created");
            synchronized (sync) {
                sync.notify();
                connectionFactory.accepted(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            LOGGER.info("Client socket closed");
        }
    }

}

abstract class ServerComponentAbstract {

    final ConfigComponent
            configComponent = (ConfigComponent) Main.context.get(ConfigComponent.class);

    int port = 0;

    boolean waitAcceptClient = true;

    ServerComponentAbstract() {
        try {
            port = Integer.parseInt(configComponent.getProperty("server-port"));
            if (port == 0) {
                throw new IllegalArgumentException("Server port not define");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

}
