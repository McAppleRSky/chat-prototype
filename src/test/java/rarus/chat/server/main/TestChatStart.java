package rarus.chat.server.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import rarus.chat.server.main.config.ConfigComponent;
import rarus.chat.server.backEnd.model.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings( "deprecation" )
public class TestChatStart {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void serverStartingTest() {
        Thread threadServer = new Thread(() -> {
            Main.main(null);
        });
        threadServer.start();

        String nameValue = null, requestMessage = null;
        try {
            nameValue = randomAlphabetic(3);
            requestMessage = objectMapper.writeValueAsString(new Message(nameValue, "", ""));
            assertNotNull(requestMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        try (Socket clientSocket = new Socket(InetAddress.getByName(null), 3443)) {
            try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                try ( BufferedReader in = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()) ) ) {
                    out.println(requestMessage);
                    System.out.println("Send " + requestMessage);
                    String responseMessage = in.readLine();
                    assertNotNull(responseMessage);
                    Message message = objectMapper.readValue(responseMessage, Message.class);
                    assertEquals(nameValue, message.getName());
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
