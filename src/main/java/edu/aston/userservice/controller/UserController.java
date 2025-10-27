package edu.aston.userservice.controller;

import edu.aston.userservice.dto.UserRequestDTO;
import edu.aston.userservice.dto.UserResponseDTO;
import edu.aston.userservice.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/application")
public class UserController {
    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody final UserRequestDTO userRequestDTO) {
        try {
            final UserResponseDTO user = this.userService.createUser(userRequestDTO);

            return ResponseEntity.ok(user);
        }
        catch(Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> readAll() {
        final List<UserResponseDTO> list = this.userService.findAll();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> readById(@PathVariable final Integer id) {
        try {
            final UserResponseDTO user = this.userService.findById(id);

            return ResponseEntity.ok(user);
        }
        catch(Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable final Integer id, @RequestBody final UserRequestDTO userRequestDTO) {
        try {
            final UserResponseDTO user = this.userService.updateUser(id, userRequestDTO);

            return ResponseEntity.ok(user);
        }
        catch(Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable final Integer id) {
        try {
            final boolean isDeleted = this.userService.deleteById(id);

            return ResponseEntity.ok(isDeleted ? "DELETED" : "NOT FOUND");
        }
        catch(Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }
}
