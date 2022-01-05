package rarus.chat.server._3_service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rarus.chat.server._2_createConnection.ConnectionHandler;

import java.io.*;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ServerClientServiceImpl implements ServerClientService {

    private static final Logger LOGGER = LogManager.getLogger(ServerClientServiceImpl.class);

    // список клиентов, которые будут подключаться к серверу
//    private ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
    private Set<ConnectionHandler> clients = Collections.newSetFromMap(new ConcurrentHashMap<>());

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
private void testRequestResponse(Socket clientSocket){
    try ( Scanner inMessage = new Scanner( clientSocket.getInputStream() ) ) {
        try ( PrintWriter outMessage = new PrintWriter(clientSocket.getOutputStream(), true) ) {
            LOGGER.info("Created socket's input, output streams");
/*                ServerComponent serverComponent = (ServerComponent) Main.context.get(ServerComponent.class);
                serverComponent.notify();*/
            while (true) {
                while (inMessage.hasNext()){
                    outMessage.println(inMessage.next());
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    LOGGER.info("Created socket's input, output streams");
}
    public ServerClientServiceImpl(Socket clientSocket)
    {
testRequestResponse(clientSocket);

//        Scanner inMessage = null;
        /*try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                String msg = in.readLine();
                out.println("resp " + msg);
            } catch (IOException e) {
                e.printStackTrace();}}*/

        /*InputStream
                    inputStream = clientSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        /*while (true){
            try ( Scanner inMessage = new Scanner(clientSocket.getInputStream()) ) {
                if(inMessage.hasNext()){
                    String receivingData = inMessage.nextLine();
                    System.out.println(receivingData);
                } } }*/

        /*Main.context.get(ServerComponent.class)
                .notify();
        ServerClientHandler client = new ServerClientHandler(clientSocket, this);
        clients.add(client);
        // каждое подключение клиента обрабатываем в новом потоке
//        new Thread(client).start();
        client.run();*/
    }

    @Override
    public void sendMessageToAllClients(String msg) {
        for (ConnectionHandler o : clients) {
            o.sendMsg(msg);
        }
    }

    @Override
    public void addClient(ConnectionHandler client) {
        clients.add(client);
    }

    @Override
    public void removeClient(ConnectionHandler client) {
        clients.remove(client);
    }

    @Override
    public int clientCount() {
        return clients.size();
    }

    @Override
    public Set<String> clientsNames() {
        Set<String> names = new HashSet<>();
        for (ConnectionHandler socket : clients) {
            names.add(socket.getClientName());
        }
        return names;
    }

    @Override
    public boolean clientAbsent(String name) {
        return false;
    }

}
