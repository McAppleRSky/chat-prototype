package rarus.chat.component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rarus.chat._3_service.ClientServiceImpl;
import rarus.chat.main.Main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerComponentImpl implements ServerComponent{

    private final Logger LOGGER = LogManager.getLogger(ServerComponentImpl.class);

    public ServerComponentImpl() {
    }

    @Override
    public void running() {
        ConfigComponent configService = (ConfigComponent) Main.context.get(ConfigComponent.class);
        int port = Integer.parseInt(configService.getProperty("server-port"));
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Main.context.put(ServerSocket.class, serverSocket);
            LOGGER.info("Server socked started");
            while (true) {
                criticalRunning();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            LOGGER.info("Server closed");
        }
    }

    private synchronized void criticalRunning() {
        new Thread(() -> {
            ServerSocket serverSocket = (ServerSocket) Main.context.get(ServerSocket.class);
            try (Socket clientSocket = serverSocket.accept()) {
                LOGGER.info("Client socket started");
                new ClientServiceImpl(clientSocket);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                LOGGER.info("Client socket closed");
            }
        });
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
