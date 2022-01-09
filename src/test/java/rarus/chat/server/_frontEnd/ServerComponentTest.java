package rarus.chat.server._frontEnd;

import rarus.chat.server._frontEnd.connectionCreator.ConnectionFactory;
import rarus.chat.server.backEnd.ClientConnection;

import java.io.IOException;
import java.net.Socket;

import static org.mockito.Mockito.mock;

class ServerComponentTest {

//    @Override
    public void configure(ConnectionFactory connectionFactory) {
        connectionFactory.setCreator((sock) -> new ClientConnection(sock));
    }

//    @Test
    void creatorTest() throws IOException {
        String testInputString = "testInputString",
                testOutputString = "testOutputString";
        Socket socketMock = mock(Socket.class);
        /*Socket socketMock = mock(Socket.class);

        when(socketMock.getInputStream())
                .thenReturn(
                        new ByteArrayInputStream(
                                testInputString.getBytes(StandardCharsets.UTF_8) ) );

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(socketMock.getOutputStream())
                .thenReturn(outputStream);

        configure(this);

        = factory -> factory.setCreator(
                (sock) -> new ClientConnectionImpl((Socket)sock) );
        factoryConfigurator.configure(this);
        ClientConnectionImpl connection = (Connection) this.creatorConnection.create(socketMock);

        Scanner inMessage = connection.getInMessage();
        assertTrue(inMessage.next().equals("testInputString"));
        PrintWriter outMessage = connection.getOutMessage();
        outMessage.println(testOutputString);
        outMessage.flush();
        assertEquals("testOutputString", new String( outputStream.toByteArray(), StandardCharsets.UTF_8).trim());*/
    }

}
