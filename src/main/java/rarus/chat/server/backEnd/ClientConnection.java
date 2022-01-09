package rarus.chat.server.backEnd;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rarus.chat.server.main.Main;
import rarus.chat.server.main.config.ConfigComponent;
import rarus.chat.server.backEnd.model.Message;
import rarus.chat.server.backEnd.service.ChatService;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import static java.lang.Thread.sleep;
import static java.time.LocalDateTime.now;

public class ClientConnection extends ClientConnectionAbstract {

    private final Logger
            LOGGER = LogManager.getLogger(ClientConnection.class);
    private ObjectMapper objectMapper = new ObjectMapper();
    boolean keepConnection = true;
//    UUID session;
    private String clientName;
    PrintWriter outMessage;
    Scanner inMessage;
    private final JedisPool jedisPool = (JedisPool) Main.context.get(JedisPool.class);

    public ClientConnection(Socket clientSocket) {
        LOGGER.info("... clientConnection created.");
        chatService.addClient(this);
        LOGGER.info("ClientConnection ref storing to chatService");
//        session = accountService.createUserSession();
        String nowCreatedSession = now().format(dateTimeFormatter);
//        this.clientSocket = clientSocket;
        try (PrintWriter outMessage = new PrintWriter(clientSocket.getOutputStream(), true)) {
            try (Scanner inMessage = new Scanner(clientSocket.getInputStream())) {
                LOGGER.info("Created Read/Write streams");
                this.outMessage = outMessage;
                this.inMessage = inMessage;
                firstMessage(nowCreatedSession);
                while (keepConnection) {
                    while (inMessage.hasNext()) {
                        nextMessage(inMessage.next());
                    }
                    sleep(0_125);
                    LOGGER.warn("Scanner message doesn't have next stringMessage");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private void nextMessage(String data) {
        Message message = null;
        String now = now().format(dateTimeFormatter);
        try {
            message = objectMapper.readValue(data, Message.class);
            String value = message.getText();
            String[] values = value.split(" ");
            switch (values[0]) {
                case ("help"):
                {
                    sendToClient(
                            objectMapper.writeValueAsString(
                                    new Message(
                                            "",
                                            now,
                                            helpTemplate
                                    ) ) );
                }
                break;
                case ("UserList"):
                {
                    Set<String> clientsNames = chatService.clientsNames();
                    clientsNames.remove(clientName);
                    StringBuilder text = new StringBuilder("UserList");
                    text.append(":");
                    if (clientsNames.isEmpty()) {
                        text.append(" ")
                                .append("nobody");
                    } else {
                        boolean first = true;
                        for (String name : clientsNames) {
                            if (first) {
                                first = false;
                                text.append("\n")
                                        .append(name);
                            } else {
                                text.append(", ")
                                        .append(name);
                            }
                        }
                    }
                    sendToClient(
                            objectMapper.writeValueAsString(
                                    new Message(
                                            "",
                                            now,
                                            text.toString()
                                    ) ) );
                }
                break;
                case ("ShowHistory"):
                {
                    int c = 0;
                    String notParseInt = null;
                    if (values.length == 2) {
                        try {
                            c = Integer.parseInt(values[1]);
                        } catch (NumberFormatException e) {
                            notParseInt = e.getMessage();
                        }
                        sendToClient(
                                objectMapper.writeValueAsString(
                                        new Message(
                                                "",
                                                now,
                                                "ShowHistory: Last " + values[1] + ':'
                                                        + (notParseInt == null ? "" : notParseInt) ) ) );
                        try (Jedis jedis = jedisPool.getResource()) {
                            List<String> lastMessages = jedis.lrange(archRoom, 0, c - 1);
                            c = lastMessages.size();
                            for (int i = c; --i >= 0;) {
                                sendToClient(lastMessages.get(i));
                            }
                        }
                    }
                }
                break;
                default:
                {
                    try (Jedis jedis = jedisPool.getResource()) {
                        jedis.lpush(
                                room,
                                objectMapper.writeValueAsString(
                                        new Message(
                                                clientName,
                                                now,
                                                value)));
                    }
                    String distributingMessage = null;
                    try (Jedis jedis = jedisPool.getResource()) {
                        distributingMessage = jedis.rpop(room);
                        chatService.sendMessageToAllClients(
                                distributingMessage);
                    }
                    String archKey = room + "-arch";
                    try (Jedis jedis = jedisPool.getResource()) {
                        jedis.lpush(archKey, distributingMessage);
                    }
                    try (Jedis jedis = jedisPool.getResource()) {
                        jedis.expire(archKey, 60 * 60 * 24);
                    }
                }
            }
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void firstMessage(String nowCreatedSession) {
        if (inMessage.hasNext()) {
            Message firstMessage = null;
            try {
                firstMessage = objectMapper.readValue(inMessage.next(), Message.class);
                String name = firstMessage.getName();
                if (name.isEmpty()) {
                    close();
                } else {
                    clientName = name;
//                accountService.addSession(session, new UserProfile(name));
                    String stringMessage = objectMapper.writeValueAsString(
                            new Message(name, nowCreatedSession, greetingFormat(name, chatService.clientCount())) );
                    outMessage.println(stringMessage);
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    /*@Override public void run() {
        chatService.addClient(this);
        try {
            this.outMessage = new PrintWriter(clientSocket.getOutputStream());
            this.inMessage = new Scanner(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

//        inMessage
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
    }*/

    // отправляем сообщение
    public void sendToClient(String stringMessage) {
        try {
            outMessage.println(stringMessage);
//            outMessage.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void close() {
        chatService.removeClient(this);
        chatService.sendMessageToAllClients("Клиентов в чате = " + chatService.clientCount());
    }
    public String getClientName() {
        return clientName;
    }
    public PrintWriter getOutMessage() {
        return outMessage;
    }  // fetch for test
    public Scanner getInMessage() {
        return inMessage;
    }  // fetch for test

}


abstract class ClientConnectionAbstract implements MessageTemplates
{
    final ConfigComponent configComponent = (ConfigComponent) Main.context.get(ConfigComponent.class);
    final String
            room = configComponent.getProperty("chat-room")
            ,archRoom = room + "-arch";
    final DateTimeFormatter
            dateTimeFormatter = (DateTimeFormatter) Main.context.get(DateTimeFormatter.class);
    final ChatService
            chatService = (ChatService)Main.context.get(ChatService.class);
//    final AccountService accountService = (AccountService)Main.context.get(AccountService.class);
    {
        if (Main.context.containsKey(ChatService.class)) {
            if (Main.context.get(ChatService.class)==null) {
                throw new NullPointerException("Main.context doesn't have instance of " + ChatService.class.getName());
            }
        } else {
            throw new NullPointerException("Main.context doesn't have key of " + ChatService.class.getName());
        }
        /*if (Main.context.containsKey(AccountService.class)) {
            if (Main.context.get(AccountService.class)==null) {
                throw new NullPointerException("Main.context doesn't have instance of " + AccountService.class.getName());
            }
        } else {
            throw new NullPointerException("Main.context doesn't have key of " + AccountService.class.getName());
        }*/
    }

    String greetingFormat(String currentName, int clientCount) {
        return String.format(greetingTemplate, currentName, clientCount);
    };

    String usersFormat(Set<String> clientsNames, String currentName) {
        clientsNames.remove(currentName);
        StringBuilder userListBuilder = new StringBuilder(usersTemplate);
        userListBuilder.append(":");
        if (clientsNames.isEmpty()) {
            userListBuilder.append(" ")
                    .append("nobody");
        } else {
            boolean first = true;
            for (String name : clientsNames) {
                if (first) {
                    first = false;
                    userListBuilder.append("\n")
                            .append(name);
                } else {
                    userListBuilder.append(", ")
                            .append(name);
                }
            }
        }
        return userListBuilder.append(".").toString();
    };

}

interface MessageTemplates {
    String helpTemplate = "Possible commands:\nUserList - show active clients;\n"
            + "ShowHistory N - show last N messages of chat with sending time and authors name."
            , greetingTemplate = "Hi, %s ! In chat %d clients"
            , usersTemplate = "UserList"
    ;
}
