package edu.aston.event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserEventTest {
    @Test
    void testInitialization1() {
        final UserEvent userRequestDTO = new UserEvent();

        assertNotEquals(null, userRequestDTO);
    }

    @Test
    void testInitialization2() {
        final UserEvent userEvent = new UserEvent("CREATE", "test@email.com");

        assertEquals("CREATE", userEvent.getAction());
        assertEquals("test@email.com", userEvent.getEmail());
    }

    @Test
    void testInitialization3() {
        final UserEvent userEvent = new UserEvent("CREATE", "test@email.com");

        userEvent.setAction("DELETE");
        userEvent.setEmail("test2@email.com");

        assertEquals("DELETE", userEvent.getAction());
        assertEquals("test2@email.com", userEvent.getEmail());
    }
}
