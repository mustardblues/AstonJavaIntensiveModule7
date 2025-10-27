package edu.aston.userservice.dto;

import edu.aston.userservice.entity.User;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

public class UserResponseDTOTest {
    @Test
    void testInitialization1() {
        final LocalDateTime localDateTime = LocalDateTime.now();

        final UserResponseDTO userResponseDTO = new UserResponseDTO(1, "test", "test@email.com", 18, localDateTime);

        assertEquals(1, userResponseDTO.getId());
        assertEquals("test", userResponseDTO.getName());
        assertEquals("test@email.com", userResponseDTO.getEmail());
        assertEquals(18, userResponseDTO.getAge());
        assertEquals(localDateTime, userResponseDTO.getCreatedAt());
    }

    @Test
    void testInitialization2() {
        final User user = new User(1, "test", "test@email.com", 18);
        final UserResponseDTO userResponseDTO = new UserResponseDTO(user);

        assertEquals(user.getId(), userResponseDTO.getId());
        assertEquals(user.getName(), userResponseDTO.getName());
        assertEquals(user.getEmail(), userResponseDTO.getEmail());
        assertEquals(user.getAge(), userResponseDTO.getAge());
        assertEquals(user.getCreatedAt(), userResponseDTO.getCreatedAt());
    }
}
