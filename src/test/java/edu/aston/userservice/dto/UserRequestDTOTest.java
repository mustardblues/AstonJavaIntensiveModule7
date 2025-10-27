package edu.aston.userservice.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserRequestDTOTest {
    @Test
    void testInitialization1() {
        final UserRequestDTO userRequestDTO = new UserRequestDTO();

        assertNotEquals(null, userRequestDTO);
    }

    @Test
    void testInitialization2() {
        final UserRequestDTO userRequestDTO = new UserRequestDTO("Test", "test@email.com", 18);

        assertEquals("Test", userRequestDTO.getName());
        assertEquals("test@email.com", userRequestDTO.getEmail());
        assertEquals(18, userRequestDTO.getAge());
    }
}
