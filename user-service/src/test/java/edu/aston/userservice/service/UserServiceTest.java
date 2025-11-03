package edu.aston.userservice.service;

import edu.aston.userservice.entity.User;
import edu.aston.userservice.dto.UserRequestDTO;
import edu.aston.userservice.dto.UserResponseDTO;
import edu.aston.userservice.producer.UserEventProducer;
import edu.aston.userservice.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.Mock;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserEventProducer userEventProducer;

    @BeforeEach
    void setup() {
        this.userService = new UserServiceImpl(userRepository, userEventProducer);
    }

    @Test
    void testCreateUser1() throws Exception {
        final User user = new User(1, "test", "test@email.com", 18);
        final UserRequestDTO userRequestDTO = new UserRequestDTO("test", "test@email.com", 18);

        when(this.userRepository.save(any(User.class))).thenReturn(user);

        final UserResponseDTO userResponseDTO = this.userService.createUser(userRequestDTO);

        assertNotNull(userResponseDTO);

        assertEquals(user.getId(), userResponseDTO.getId());
        assertEquals(user.getName(), userResponseDTO.getName());
        assertEquals(user.getEmail(), userResponseDTO.getEmail());
        assertEquals(user.getAge(), userResponseDTO.getAge());
    }

    @Test
    void testCreateUser2() throws Exception {
        assertThrows(UserServiceException.class,
                () -> this.userService.createUser(new UserRequestDTO(null, "test@email.com", 18)));

        assertThrows(UserServiceException.class,
                () -> this.userService.createUser(new UserRequestDTO("123", "test@email.com", 18)));

        assertThrows(UserServiceException.class,
                () -> this.userService.createUser(new UserRequestDTO("test", null, 18)));

        assertThrows(UserServiceException.class,
                () -> this.userService.createUser(new UserRequestDTO("test", "123", 18)));

        assertThrows(UserServiceException.class,
                () -> this.userService.createUser(new UserRequestDTO("test", "test@email.com", 0)));
    }

    @Test
    void testFindAll() throws Exception {
        final List<User> expected = List.of(
                new User("test", "testA@email.com", 18)
        );

        when(this.userRepository.findAll()).thenReturn(expected);

        final List<UserResponseDTO> result = this.userService.findAll();

        assertEquals(expected.size(), result.size());
        assertEquals(expected.getFirst().getName(), result.getFirst().getName());
        assertEquals(expected.getFirst().getEmail(), result.getFirst().getEmail());
        assertEquals(expected.getFirst().getAge(), result.getFirst().getAge());
    }

    @Test
    void testFindById1() throws Exception {
        when(this.userRepository.findById(1)).thenReturn(Optional.of(new User(1, "test", "test@email.com", 18)));

        final UserResponseDTO userResponseDTO = this.userService.findById(1);

        assertNotNull(userResponseDTO);

        assertEquals(1, userResponseDTO.getId());
        assertEquals("test", userResponseDTO.getName());
        assertEquals("test@email.com", userResponseDTO.getEmail());
        assertEquals(18, userResponseDTO.getAge());
    }

    @Test
    void testFindById2() throws Exception {
        assertThrows(UserServiceException.class, () -> this.userService.findById(0));
    }

    @Test
    void testUpdateUser1() throws Exception {
        final User user = new User(1, "testA", "testA@email.com", 18);
        final UserRequestDTO userRequestDTO = new UserRequestDTO("testB", "testB@email.com", 19);

        when(this.userRepository.findById(1)).thenReturn(Optional.of(new User(1, "testA", "testA@email.com", 18)));
        when(this.userRepository.save(any(User.class))).thenReturn(new User(1, "testB", "testB@email.com", 19));

        final UserResponseDTO userResponseDTO = this.userService.updateUser(1 ,userRequestDTO);

        assertNotNull(userResponseDTO);

        assertEquals(user.getId(), userResponseDTO.getId());
        assertNotEquals(user.getName(), userResponseDTO.getName());
        assertNotEquals(user.getEmail(), userResponseDTO.getEmail());
        assertNotEquals(user.getAge(), userResponseDTO.getAge());
    }

    @Test
    void testUpdateUser2() throws Exception {
        assertThrows(UserServiceException.class,
                () -> this.userService.updateUser(0, new UserRequestDTO(null, "test@email.com", 18)));

        assertThrows(UserServiceException.class,
                () -> this.userService.updateUser(1, new UserRequestDTO("123", "test@email.com", 18)));

        assertThrows(UserServiceException.class,
                () -> this.userService.updateUser(1, new UserRequestDTO("test", null, 18)));

        assertThrows(UserServiceException.class,
                () -> this.userService.updateUser(1, new UserRequestDTO("test", "123", 18)));

        assertThrows(UserServiceException.class,
                () -> this.userService.updateUser(1, new UserRequestDTO("test", "test@email.com", 0)));
    }

    @Test
    void testDeleteById() throws Exception {
        when(this.userRepository.findById(1)).thenReturn(
                Optional.of(new User(1, "test", "test@email.com", 18)));
        when(this.userRepository.findById(2)).thenReturn(Optional.empty());

        doNothing().when(this.userEventProducer).sendEvent("DELETE", "test@email.com");

        assertTrue(this.userService.deleteById(1));
        assertFalse(this.userService.deleteById(2));
    }
}
