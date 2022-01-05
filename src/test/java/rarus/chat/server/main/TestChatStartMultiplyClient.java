package rarus.chat.server.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import rarus.chat.model.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestChatStartMultiplyClient {

    @Test
    public void serverStartingTest() {
        Thread threadServer = new Thread(() -> {
            Main.main(null);
        });
        threadServer.start();

        {
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
            try (Socket clientSocket = new Socket("localhost", 3443)) {
                try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                    try ( BufferedReader in = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()) ) ) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        out.println(finalMessageValue);
                        String response = in.readLine();
                        Message message = objectMapper.readValue(response, Message.class);
                        assertEquals(finalNameValue, message.getName());
                        System.out.println("Response name " + message.getName());
                    }
                }
            } catch (ConnectException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        {
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
            try (Socket clientSocket = new Socket("localhost", 3443)) {
                try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                    try ( BufferedReader in = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()) ) ) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        out.println(finalMessageValue);
                        String response = in.readLine();
                        Message message = objectMapper.readValue(response, Message.class);
                        assertEquals(finalNameValue, message.getName());
                        System.out.println("Response name " + message.getName());
                    }
                }
            } catch (ConnectException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        {
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
            try (Socket clientSocket = new Socket("localhost", 3443)) {
                try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                    try ( BufferedReader in = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()) ) ) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        out.println(finalMessageValue);
                        String response = in.readLine();
                        Message message = objectMapper.readValue(response, Message.class);
                        assertEquals(finalNameValue, message.getName());
                        System.out.println("Response name " + message.getName());
                    }
                }
            } catch (ConnectException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        threadServer.stop();
    }

}
