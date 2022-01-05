package rarus.chat.server._2_createConnection;

import rarus.chat.server._2_createConnection.creator.Connection;
import rarus.chat.server._3_service.ServerClientService;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

// реализуем интерфейс Runnable, который позволяет работать с потоками
public class ConnectionHandler implements Connection, Runnable{
    private Integer session;
    // экземпляр нашего сервера
    private ServerClientService chatService;
    private static final String HOST = "localhost";
    private static final int PORT = 3443;
    // клиентский сокет
    private Socket clientSocket = null;
    // количество клиента в чате, статичное поле
//    private static int clients_count = 0;
    private String clientName;
    // исходящее сообщение
    private PrintWriter outMessage;
    // входящее собщение
    private Scanner inMessage;

    @Override public PrintWriter getOutMessage() {
        return outMessage;
    }
    @Override public Scanner getInMessage() {
        return inMessage;
    }

    // конструктор, который принимает клиентский сокет и сервер
    public ConnectionHandler(Socket clientSocket//, ServerClientServiceImpl chatService
                                )
    {
        try {
//            clients_count++;
            this.chatService = chatService;
            this.clientSocket = clientSocket;
            this.outMessage = new PrintWriter(clientSocket.getOutputStream());
            this.inMessage = new Scanner(clientSocket.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

//    public ConnectionHandler() {}

    // Переопределяем метод run(), который вызывается когда
    // мы вызываем new Thread(client).start();
//    @Override
    public void run() {
        try {
            while (true) {
                // сервер отправляет сообщение
                chatService.sendMessageToAllClients("Новый участник вошёл в чат!");
                chatService.sendMessageToAllClients("Клиентов в чате = " + chatService.clientCount());
                break;
            }

            while (true) {
                // Если от клиента пришло сообщение
                if (inMessage.hasNext()) {
                    String clientMessage = inMessage.nextLine();
                    // если клиент отправляет данное сообщение, то цикл прерывается и
                    // клиент выходит из чата
                    if (clientMessage.equalsIgnoreCase("##session##end##")) {
                        break;
                    }
                    // выводим в консоль сообщение (для теста)
                    System.out.println(clientMessage);
                    // отправляем данное сообщение всем клиентам
                    chatService.sendMessageToAllClients(clientMessage);
                }
                // останавливаем выполнение потока на 100 мс
                Thread.sleep(100);
            }
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        finally {
            this.close();
        }
    }
    // отправляем сообщение
    public void sendMsg(String msg) {
        try {
            outMessage.println(msg);
            outMessage.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    // клиент выходит из чата
    public void close() {
        // удаляем клиента из списка
        chatService.removeClient(this);
//        clients_count--;
        chatService.sendMessageToAllClients("Клиентов в чате = " + chatService.clientCount());
    }

    public String getClientName() {
        return clientName;
    }
}
