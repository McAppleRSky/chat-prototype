package rarus.chat.server._2_createConnection.creator;

import java.net.Socket;

public interface ConnectionCreator {

    void setCreator(Creator creator);

    void accept(Socket clientSocket);

}
