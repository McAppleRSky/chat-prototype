package rarus.chat.server._3_service;

import rarus.chat.server._2_createConnection.ConnectionHandler;

import java.util.Set;

public interface ServerClientService {

    void sendMessageToAllClients(String msg);

    void addClient(ConnectionHandler client);

    void removeClient(ConnectionHandler client);

    int clientCount();

    Set<String> clientsNames();

    boolean clientAbsent(String name);

}
