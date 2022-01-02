package rarus.chat._2_socket;

import rarus.chat._3_service.ClientService;
import rarus.chat._3_service.ClientServiceImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

// реализуем интерфейс Runnable, который позволяет работать с потоками
public class ClientHandler //implements Runnable
                            {
    // экземпляр нашего сервера
    private ClientService chatService;
    // исходящее сообщение
    private PrintWriter outMessage;
    // входящее собщение
    private Scanner inMessage;
    private static final String HOST = "localhost";
    private static final int PORT = 3443;
    // клиентский сокет
    private Socket clientSocket = null;
    // количество клиента в чате, статичное поле
//    private static int clients_count = 0;
    private String clientName;

    // конструктор, который принимает клиентский сокет и сервер
    public ClientHandler(Socket socket, ClientServiceImpl chatService) {
        try {
//            clients_count++;
            this.chatService = chatService;
            this.clientSocket = socket;
            this.outMessage = new PrintWriter(socket.getOutputStream());
            this.inMessage = new Scanner(socket.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
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
