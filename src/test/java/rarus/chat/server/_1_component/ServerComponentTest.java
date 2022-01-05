package rarus.chat.server._1_component;

import org.junit.jupiter.api.Test;
import rarus.chat.server._2_createConnection.ConnectionFactory;
import rarus.chat.server._2_createConnection.ConnectionHandler;
import rarus.chat.server._2_createConnection.creator.Connection;
import rarus.chat.server._2_createConnection.creator.FactoryConfigurator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ServerComponentTest extends ConnectionFactory{

    Socket socketMock = mock(Socket.class);

    @Test
    void creatorTest() throws IOException {
        String testInputString = "testInputString",
                testOutputString = "testOutputString";
        when(socketMock.getInputStream())
                .thenReturn(
                        new ByteArrayInputStream(
                                testInputString.getBytes(StandardCharsets.UTF_8)
                        ) );

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(socketMock.getOutputStream())
                .thenReturn(outputStream);

        FactoryConfigurator factoryConfigurator = factory -> factory.setCreator(
                (o) -> new ConnectionHandler((Socket) o) );
        factoryConfigurator.configure(this);
        Connection connection = (Connection) this.creatorConnection.create(socketMock);

        Scanner inMessage = connection.getInMessage();
        assertTrue(inMessage.next().equals("testInputString"));
        PrintWriter outMessage = connection.getOutMessage();
        outMessage.println(testOutputString);
        outMessage.flush();
        assertEquals("testOutputString", new String( outputStream.toByteArray(), StandardCharsets.UTF_8).trim());
    }

}
