package rarus.chat.server.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import rarus.chat.model.Message;
import rarus.chat.server.main.config.ConfigComponent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.*;

public class TestChatStart {

    @Test
    public void serverStartingTest() {
        Thread threadServer = new Thread(() -> {
            Main.main(null);
        });
        threadServer.start();

        String nameValue = null, messageValue = null;
        try {
            nameValue = randomAlphabetic(3);
            ObjectMapper objectMapper = new ObjectMapper();
            messageValue = objectMapper.writeValueAsString(new Message(nameValue, "", ""));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String finalMessageValue = messageValue;
        String finalNameValue = nameValue;
//        try (Socket clientSocket = new Socket("localhost", 3443)) {
        try (Socket clientSocket = new Socket(InetAddress.getByName(null), 3443)) {
            try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                try ( BufferedReader in = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()) ) ) {
//                    System.out.println("test client running");
                    ObjectMapper objectMapper = new ObjectMapper();
                    out.println(finalMessageValue);
                    String response = in.readLine();
                    Message message = objectMapper.readValue(response, Message.class);
                    assertEquals(finalNameValue, message.getName());
                    System.out.println(message.getName());
                }
            }
        } catch (ConnectException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        threadServer.stop();
    }

    @Test
    void contextLoadDateTimeFormat() {
        ConfigComponent configComponent = (ConfigComponent) Main.context.get(ConfigComponent.class);
        assertEquals("3443", configComponent.getProperty("server-port"));
        assertEquals("default-room", configComponent.getProperty("chat-room"));
        assertEquals("yyyy-MM-dd HH:mm:ss", configComponent.getProperty("date-time-format"));
        assertEquals("6379", configComponent.getProperty("redis-port"));
    }

}
