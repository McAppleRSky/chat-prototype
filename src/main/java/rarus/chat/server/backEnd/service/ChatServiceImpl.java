package rarus.chat.server.backEnd.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rarus.chat.server.backEnd.ClientConnection;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServiceImpl implements ChatService {

    private static final Logger LOGGER = LogManager.getLogger(ChatServiceImpl.class);

    private Set<ClientConnection> clients = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public ChatServiceImpl() {
    }

    @Override
    public void sendMessageToAllClients(String stringMessage) {
        for (ClientConnection client : clients) {
            client.sendToClient(stringMessage);
        }
    }

    @Override
    public void addClient(ClientConnection client) {
        LOGGER.info("ClientConnection ref stored to chatService");
        clients.add(client);
    }

    @Override
    public void removeClient(ClientConnection client) {
        clients.remove(client);
    }

    @Override
    public int clientCount() {
        return clients.size();
    }

    @Override
    public Set<String> clientsNames() {
        Set<String> names = new HashSet<>();
        for (ClientConnection socket : clients) {
            names.add(socket.getClientName());
        }
        return names;
    }

    @Override
    public boolean clientAbsent(String name) {
        return false;
    }

}
