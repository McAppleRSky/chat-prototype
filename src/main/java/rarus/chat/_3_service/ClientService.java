package rarus.chat._3_service;

import rarus.chat._2_socket.ClientHandler;

import java.util.Set;

public interface ClientService {

    void sendMessageToAllClients(String msg);

    void addClient(ClientHandler client);

    void removeClient(ClientHandler client);

    int clientCount();

    Set<String> clientsNames();

    boolean clientAbsent(String name);

}
