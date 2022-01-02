package rarus.chat.main;

import org.junit.jupiter.api.Test;
import rarus.chat.ChatClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestChatStart {

    @Test
    public void givenGreetingClient_whenServerRespondsWhenStarted_thenCorrect() {
        ChatClient client = new ChatClient();
        client.startConnection("127.0.0.1", 6666);
        String response = client.sendMessage("hello server");
        assertEquals("hello client", response);
    }

}
