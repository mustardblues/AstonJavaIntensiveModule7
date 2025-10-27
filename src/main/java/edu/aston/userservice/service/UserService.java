package edu.aston.userservice.service;

import edu.aston.userservice.dto.UserRequestDTO;
import edu.aston.userservice.dto.UserResponseDTO;

import java.util.List;

public interface UserService {
    UserResponseDTO createUser(final UserRequestDTO userRequestDTO) throws UserServiceException;
    List<UserResponseDTO> findAll();
    UserResponseDTO findById(final Integer id) throws UserServiceException;
    UserResponseDTO updateUser(final Integer id, final UserRequestDTO userRequestDTO) throws UserServiceException;
    boolean deleteById(final Integer id) throws UserServiceException;
}
