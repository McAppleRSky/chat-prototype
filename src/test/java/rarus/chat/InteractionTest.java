package rarus.chat;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertEquals;

// https://www.baeldung.com/a-guide-to-java-sockets

public class InteractionTest {

    @Test
    void actionTest(){
        Thread serverThread = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(6666)) {
                try (Socket clientSocket = serverSocket.accept()) {
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                            String msg = in.readLine();
                            out.println("resp " + msg);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        })
                , clientThread = new Thread(() ->{
            try (Socket clientSocket = new Socket("localhost", 6666)) {
                try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                        String expectedMsg = randomAlphabetic(3);
                        out.println(expectedMsg);
                        String actualRes = in.readLine();
                        assertEquals("resp " + expectedMsg, actualRes);
                    }
                }
            } catch (ConnectException e){
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();
//        clientThread.run();
        clientThread.start();
    }

}
