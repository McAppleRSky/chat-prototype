package rarus.chat.server.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
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
public class TestChatStartMultiplyMessage {

    private final int
            EXPECTED_COUNT = 2
//            100
            ;
    private int actualCount;

    @Test
    public void multiplyMessagingNamed() {
        Thread threadServer = new Thread(() -> {
            Main.main(null);
        });
        threadServer.start();

        int i;
        try (Socket clientSocket = new Socket(InetAddress.getByName(null), 3443)) {
            try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                try ( BufferedReader in = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()) ) ) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    for (i = 0; i < EXPECTED_COUNT; i++) {
                        String name = randomAlphabetic(4);
                        String stringMessage = objectMapper.writeValueAsString(
                                new Message(name, "", "") );
                        assertNotNull(stringMessage);
                        out.println(stringMessage);
                        Message message = objectMapper.readValue(in.readLine(), Message.class);
                        assertEquals(name, message.getName());
                        System.out.println(message.getName());
                        actualCount++;
                    }
                }
            }
        } catch (ConnectException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(EXPECTED_COUNT, actualCount);

        threadServer.stop();
    }

}
