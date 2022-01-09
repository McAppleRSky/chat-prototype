package rarus.chat.server._frontEnd.connectionCreator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rarus.chat.server._frontEnd.ServerComponent;

import java.net.Socket;

public class ConnectionFactoryImpl implements ConnectionFactory {

    private final Logger
            LOGGER = LogManager.getLogger(ConnectionFactoryImpl.class);

    private ConnectionCreator creatorConnection;

    @Override
    public void setCreator(ConnectionCreator creatorConnection) {
        this.creatorConnection = creatorConnection;
    }

    @Override
    public void accepted(Socket clientSocket) {
        if (creatorConnection == null) {
            LOGGER.error("ConnectionCreator not defining, can't create client");
            throw new NullPointerException("ConnectionCreator not defining");
        }
        LOGGER.info("ClientConnection creating");
        creatorConnection.create(clientSocket);
    }

}
