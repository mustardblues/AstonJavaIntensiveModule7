package edu.aston.userservice.controller;

import edu.aston.userservice.dto.UserRequestDTO;
import edu.aston.userservice.dto.UserResponseDTO;
import edu.aston.userservice.service.UserService;
import edu.aston.userservice.service.UserServiceException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.time.LocalDateTime;
import java.util.List;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    void testCreate1() throws Exception {
        final UserRequestDTO userRequestDTO = new UserRequestDTO("test", "test@email.com", 18);
        final UserResponseDTO userResponseDTO = new UserResponseDTO(1, "test", "test@email.com", 18, LocalDateTime.now());

        when(userService.createUser(any(UserRequestDTO.class))).thenReturn(userResponseDTO);

        mockMvc.perform(post("/application")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(userRequestDTO.getName()))
                .andExpect(jsonPath("$.email").value(userRequestDTO.getEmail()))
                .andExpect(jsonPath("$.age").value(userRequestDTO.getAge()))
                .andExpect(jsonPath("$.createdAt").exists());

        verify(userService).createUser(any(UserRequestDTO.class));
    }

    @Test
    void testCreate2() throws Exception {
        final UserRequestDTO userRequestDTO = new UserRequestDTO("0", "0", 0);

        when(userService.createUser(any(UserRequestDTO.class)))
                .thenThrow(new UserServiceException("Invalid user information"));

        mockMvc.perform(post("/application")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid user information"));

        verify(userService).createUser(any(UserRequestDTO.class));
    }

    @Test
    void testReadAll() throws Exception {
        final List<UserResponseDTO> list = List.of(
                new UserResponseDTO(1, "test", "test@email.com", 18, LocalDateTime.now())
        );

        when(userService.findAll()).thenReturn(list);

        mockMvc.perform(get("/application")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("test"))
                .andExpect(jsonPath("$[0].email").value("test@email.com"))
                .andExpect(jsonPath("$[0].age").value(18));

        verify(userService).findAll();
    }

    @Test
    void testReadById1() throws Exception {
        final UserResponseDTO userResponseDTO = new UserResponseDTO(1, "test", "test@email.com", 18, LocalDateTime.now());

        when(userService.findById(1)).thenReturn(userResponseDTO);

        mockMvc.perform(get("/application/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userResponseDTO.getId()))
                .andExpect(jsonPath("$.name").value(userResponseDTO.getName()))
                .andExpect(jsonPath("$.email").value(userResponseDTO.getEmail()))
                .andExpect(jsonPath("$.age").value(userResponseDTO.getAge()))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    void testReadById2() throws Exception {
        when(userService.findById(0))
                .thenThrow(new UserServiceException("Invalid user ID to read user information"));

        mockMvc.perform(get("/application/0")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(0)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid user ID to read user information"));
    }

    @Test
    void testUpdate1() throws Exception {
        final UserRequestDTO userRequestDTO = new UserRequestDTO("test", "test@email.com", 18);
        final UserResponseDTO userResponseDTO = new UserResponseDTO(1, "test", "test@email.com", 18, LocalDateTime.now());

        when(userService.updateUser(eq(1), any(UserRequestDTO.class))).thenReturn(userResponseDTO);

        mockMvc.perform(put("/application/1")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(userResponseDTO.getName()))
                .andExpect(jsonPath("$.email").value(userResponseDTO.getEmail()))
                .andExpect(jsonPath("$.age").value(userResponseDTO.getAge()));

        verify(userService).updateUser(eq(1), any(UserRequestDTO.class));
    }

    @Test
    void testUpdate2() throws Exception {
        final UserRequestDTO userRequestDTO = new UserRequestDTO("test", "test@email.com", 18);

        when(userService.updateUser(eq(0), any(UserRequestDTO.class)))
                .thenThrow(new UserServiceException("Invalid user ID to update user information"));

        mockMvc.perform(put("/application/0")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid user ID to update user information"));
    }

    @Test
    void testDelete1() throws Exception {
        when(userService.deleteById(1)).thenReturn(true);
        when(userService.deleteById(2)).thenReturn(false);

        mockMvc.perform(delete("/application/1"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/application/2"))
                .andExpect(status().isOk());

        verify(userService).deleteById(1);
        verify(userService).deleteById(2);
    }

    @Test
    void testDelete2() throws Exception {
        when(userService.deleteById(0))
                .thenThrow(new UserServiceException("Invalid user ID to delete user information"));

        mockMvc.perform(delete("/application/0")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(0)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid user ID to delete user information"));
    }
}
