package rarus.chat.server.backEnd.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserProfileTest {

    @Test
    void createTest() {
        UserProfile userProfile = new UserProfile();
        assertEquals(null, userProfile.getLogin());
    }

}