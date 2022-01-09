package rarus.chat.server._frontEnd.connectionCreator;

import org.junit.jupiter.api.Test;

import java.net.Socket;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class ConnectionFactoryImplTest implements FactoryConfigurator {

    ConnectionFactory connectionFactory = new ConnectionFactoryImpl();
    Socket socket = null;

    @Override
    public void configure(ConnectionFactory connectionFactory) {
        connectionFactory.setCreator((sock) -> this.socket = sock);
    }

    @Test
    void creatingTest() {
        configure(connectionFactory);
        Socket socketMock = mock(Socket.class);
        connectionFactory.accepted(socketMock);
        assertNotNull(socket);
        assertEquals(socketMock, socket);
        /*String testInputString = "testInputString",
                testOutputString = "testOutputString";

        Socket socketMock = mock(Socket.class);
        Main.context.put(ConfigComponent.class, new ConfigComponent() {

            @Override
            public String getProperty(String property) {
                return "test";
            }

            @Override public void setProperty(String property, String value) {} });
        Main.context.put(DateTimeFormatter.class, null);
        ChatService chatServiceMock = mock(ChatService.class);
        Main.context.put(ChatService.class, chatServiceMock);
        AccountService accountServiceMock = mock(AccountService.class);
        Main.context.put(AccountService.class, accountServiceMock);
        Main.context.put(DateTimeFormatter.class, null);

        configure(connectionFactory);
        connectionFactory.accepted(socketMock);
        when(socketMock.getInputStream())
                .thenReturn(
                        new ByteArrayInputStream(
                                testInputString.getBytes(StandardCharsets.UTF_8) ) );
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(socketMock.getOutputStream())
                .thenReturn(outputStream);

        verify(chatServiceMock).addClient(argThat(
                x -> false ));*/
    }

}
