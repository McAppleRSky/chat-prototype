package rarus.chat.server.backEnd;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ClientConnectionAbstractTest extends ClientConnectionAbstract{

    @Test
    void helpTemplate() {
        String expected = "Possible commands:\n"
                 + "UserList - show active clients;\n"
                 + "ShowHistory N - show last N messages of chat with sending time and authors name.";
        assertEquals(expected, helpTemplate);
    }

    @Test
    void greetingFormat() {
        String expected = "Hi, tstName ! In chat 0 clients";
        assertEquals(expected, greetingFormat("tstName", 0));
    }

    @Test
    void usersFormat() {
        String expected = "UserList:\n" +
                "uno.";
        Set<String> users = new HashSet<String>();
        Collections.addAll(users, "uno", "duo");
        assertEquals(expected, usersFormat(users, "duo"));
    }

}
