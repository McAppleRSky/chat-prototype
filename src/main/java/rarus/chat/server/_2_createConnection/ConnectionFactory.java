package rarus.chat.server._2_createConnection;

import rarus.chat.server._2_createConnection.creator.Connection;
import rarus.chat.server._2_createConnection.creator.ConnectionCreator;
import rarus.chat.server._2_createConnection.creator.Creator;

import java.net.Socket;

public class ConnectionFactory implements ConnectionCreator {

//    private Connection connection;
    protected Creator creatorConnection;

    @Override
    public void setCreator(Creator creatorConnection) {
        this.creatorConnection = creatorConnection;
    }

    @Override
    public void accept(Socket clientSocket) {
//        new Thread(Creator.)
    }
}
