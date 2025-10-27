package edu.aston.userservice.entity;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    @Test
    void testInitialization1() {
        final User user = new User();

        assertNotEquals(null, user);
    }

    @Test
    void testInitialization2() {
        final User user = new User("test", "test@email.com", 18);

        assertEquals("test", user.getName());
        assertEquals("test@email.com", user.getEmail());
        assertEquals(18, user.getAge());
    }

    @Test
    void testInitialization3() {
        final User user = new User(1, "test", "test@email.com", 18);

        assertEquals(1, user.getId());
        assertEquals("test", user.getName());
        assertEquals("test@email.com", user.getEmail());
        assertEquals(18, user.getAge());
    }

    @Test
    void testEqualityBetweenObjects() {
        final User userA = new User(1, "test", "test@email.com", 18);
        final User userB = new User(1, "test", "test@email.com", 18);

        try {
            Field field = User.class.getDeclaredField("createdAt");
            field.setAccessible(true);

            field.set(userA, LocalDateTime.of(1988, 2, 18, 7, 30));
            field.set(userB, LocalDateTime.of(1988, 2, 18, 7, 30));
        }
        catch(Exception exception) {
            throw new RuntimeException(exception);
        }

        assertEquals(userA, userB);
        assertEquals(userA.hashCode(), userB.hashCode());
        assertEquals(userA.getCreatedAt(), userB.getCreatedAt());
    }

    @Test
    void testInequalityBetweenObjects() {
        final User user = new User(1, "test", "test@email.com", 18);

        assertNotEquals(new User(2, "test", "test@email.com", 18), user);
        assertNotEquals(new User(1, "none", "test@email.com", 18), user);
        assertNotEquals(new User(1, "test", "none@email.com", 18), user);
        assertNotEquals(new User(1, "test", "test@email.com", 19), user);
    }
}
