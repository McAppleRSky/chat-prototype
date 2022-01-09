package rarus.chat.server.backEnd.service;

import rarus.chat.server.backEnd.ClientConnection;

import java.util.Set;

public interface ChatService {

    void sendMessageToAllClients(String stringMessage);

    void addClient(ClientConnection client);

    void removeClient(ClientConnection client);

    int clientCount();

    Set<String> clientsNames();

    boolean clientAbsent(String name);

}
