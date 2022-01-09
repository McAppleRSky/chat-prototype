package rarus.chat.server._frontEnd.connectionCreator;

import java.net.Socket;

public interface ConnectionFactory {

    void setCreator(ConnectionCreator creator);

    void accepted(Socket clientSocket);

}
