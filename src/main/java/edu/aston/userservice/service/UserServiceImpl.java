package edu.aston.userservice.service;

import edu.aston.userservice.entity.User;
import edu.aston.userservice.dto.UserRequestDTO;
import edu.aston.userservice.dto.UserResponseDTO;
import edu.aston.userservice.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private static class UserValidator {
        static final int MIN_AGE = 18;
        static final int MAX_AGE = 99;

        public static void validateData(final String name, final String email, final int age) throws UserServiceException {
            if(isInvalidUserName(name)) {
                throw new UserServiceException("The user's name is invalid");
            }

            if(isInvalidUserEmail(email)) {
                throw new UserServiceException("The user's email is invalid");
            }

            if(age < MIN_AGE || age > MAX_AGE) {
                throw new UserServiceException("The user's age is out of range");
            }
        }

        public static void validateId(final int id) throws UserServiceException {
            if(id < 0) {
                throw new UserServiceException("The user's ID must be greater than 0");
            }
        }

        private static boolean isInvalidUserName(final String name) {
            if(name == null || name.isBlank()) {
                return true;
            }

            return !name.chars().allMatch(Character::isLetter);
        }

        private static boolean isInvalidUserEmail(final String email) {
            if(email == null || email.isBlank()) {
                return true;
            }

            return !email.contains("@");
        }
    }

    public UserServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserResponseDTO createUser(final UserRequestDTO userRequestDTO) throws UserServiceException {
        final String name = userRequestDTO.getName();
        final String email = userRequestDTO.getEmail();
        final Integer age = userRequestDTO.getAge();

        try {
            UserValidator.validateData(name, email, age);

            final User user = this.userRepository.save(new User(name, email, age));

            return new UserResponseDTO(user);
        }
        catch (Exception exception) {
            throw new UserServiceException("Failed to add a new user to the database", exception);
        }
    }

    @Override
    @Transactional
    public List<UserResponseDTO> findAll() throws UserServiceException {
        try {
            final List<UserResponseDTO> list = this.userRepository.findAll()
                    .stream()
                    .map(UserResponseDTO::new)
                    .collect(Collectors.toList());

            return list;
        }
        catch (Exception exception) {
            throw new UserServiceException("Failed to find all users in the database", exception);
        }
    }

    @Override
    @Transactional
    public UserResponseDTO findById(final Integer id) throws UserServiceException {
        try {
            UserValidator.validateId(id);

            final User user = this.userRepository.findById(id)
                    .orElseThrow(() -> new UserServiceException("The user could not be found with ID: " + id));

            return new UserResponseDTO(user);
        }
        catch (Exception exception) {
            throw new UserServiceException("Failed to find the user by ID in the database", exception);
        }
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(final Integer id, final UserRequestDTO userRequestDTO) throws UserServiceException {
        final String name = userRequestDTO.getName();
        final String email = userRequestDTO.getEmail();
        final Integer age = userRequestDTO.getAge();

        try {
            UserValidator.validateId(id);
            UserValidator.validateData(name, email, age);

            this.userRepository.findById(id)
                    .orElseThrow(() -> new UserServiceException("The user could not be found with ID: " + id));

            final User user = this.userRepository.save(new User(id, name, email, age));

            return new UserResponseDTO(user);
        }
        catch(Exception exception) {
            throw new UserServiceException("Failed to update user information in the database", exception);
        }
    }

    @Override
    @Transactional
    public boolean deleteById(final Integer id) throws UserServiceException {
        try {
            UserValidator.validateId(id);

            if(this.userRepository.existsById(id)) {
                userRepository.deleteById(id);

                return true;
            }

            return false;
        }
        catch(Exception exception) {
            throw new UserServiceException("Failed to delete a user information from the database", exception);
        }
    }
}
