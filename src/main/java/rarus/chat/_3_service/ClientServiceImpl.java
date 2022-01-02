package rarus.chat._3_service;

import rarus.chat._2_socket.ClientHandler;
import rarus.chat.main.Main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ClientServiceImpl implements ClientService {

    // список клиентов, которые будут подключаться к серверу
//    private ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
    private Set<ClientHandler> clients = Collections.newSetFromMap(new ConcurrentHashMap<>());

    /*public ClientServiceImpl() {
        // порт, который будет прослушивать наш сервер
        final int PORT = 3443;
        // сокет клиента, это некий поток, который будет подключаться к серверу
        // по адресу и порту
        Socket clientSocket = null;
        // серверный сокет
        ServerSocket serverSocket = null;
        try //(ServerSocket serverSocket =  new ServerSocket(PORT))
        {
            // создаём серверный сокет на определенном порту
            serverSocket = new ServerSocket(PORT);
            System.out.println("Сервер запущен!");
            // запускаем бесконечный цикл
            while (true) {
                // таким образом ждём подключений от сервера
                clientSocket = serverSocket.accept();
                // создаём обработчик клиента, который подключился к серверу
                // this - это наш сервер
                ClientHandler client = new ClientHandler(clientSocket, this);
                clients.add(client);
                // каждое подключение клиента обрабатываем в новом потоке
                new Thread(client).start();
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            try {
                // закрываем подключение
                clientSocket.close();
                System.out.println("Сервер остановлен");
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }*/

    public ClientServiceImpl(Socket clientSocket) {
        ClientHandler client = new ClientHandler(clientSocket, this);
        clients.add(client);
        // каждое подключение клиента обрабатываем в новом потоке
//        new Thread(client).start();
        client.run();
    }

    @Override
    public void sendMessageToAllClients(String msg) {
        for (ClientHandler o : clients) {
            o.sendMsg(msg);
        }
    }

    @Override
    public void addClient(ClientHandler client) {
        clients.add(client);
    }

    @Override
    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }

    @Override
    public int clientCount() {
        return clients.size();
    }

    @Override
    public Set<String> clientsNames() {
        Set<String> names = new HashSet<>();
        for (ClientHandler socket : clients) {
            names.add(socket.getClientName());
        }
        return names;
    }

    @Override
    public boolean clientAbsent(String name) {
        return false;
    }

}
