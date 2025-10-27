package edu.aston.userservice.service;

import edu.aston.userservice.entity.User;
import edu.aston.userservice.dto.UserRequestDTO;
import edu.aston.userservice.dto.UserResponseDTO;
import edu.aston.userservice.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

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
            if(id <= 0) {
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

        logger.info("Start creating a new user: [name={}, email={}, age={}].", name, email, age);

        try {
            UserValidator.validateData(name, email, age);

            final User user = this.userRepository.save(new User(name, email, age));

            logger.info("The user has been created: {}", user.toString());

            return new UserResponseDTO(user);
        }
        catch (Exception exception) {
            logger.error("Failed to add a new user to the database.");
            throw new UserServiceException("Failed to add a new user to the database", exception);
        }
    }

    @Override
    @Transactional
    public List<UserResponseDTO> findAll() {
        logger.info("Start searching for all users in the database.");

        final List<UserResponseDTO> list = this.userRepository.findAll()
                .stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList());

        logger.info("Found {} users in the database.", list.size());

        return list;
    }

    @Override
    @Transactional
    public UserResponseDTO findById(final Integer id) throws UserServiceException {
        logger.info("Start searching a user by ID: [id={}].", id);

        try {
            UserValidator.validateId(id);

            final User user = this.userRepository.findById(id)
                    .orElseThrow(() -> new UserServiceException("The user could not be found with ID: " + id));

            logger.info("Found a user in the database: {}.", user.toString());

            return new UserResponseDTO(user);
        }
        catch (Exception exception) {
            logger.error("Failed to find the user by ID in the database.");
            throw new UserServiceException("Failed to find the user by ID in the database", exception);
        }
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(final Integer id, final UserRequestDTO userRequestDTO) throws UserServiceException {
        final String name = userRequestDTO.getName();
        final String email = userRequestDTO.getEmail();
        final Integer age = userRequestDTO.getAge();

        logger.info("Start updating user information: [id={}, name={}, email={}, age={}].", id, name, email, age);

        try {
            UserValidator.validateId(id);
            UserValidator.validateData(name, email, age);

            this.userRepository.findById(id)
                    .orElseThrow(() -> new UserServiceException("The user could not be found with ID: " + id));

            final User user = this.userRepository.save(new User(id, name, email, age));

            logger.info("The user with ID {} has been updated in the database.", id);

            return new UserResponseDTO(user);
        }
        catch(Exception exception) {
            logger.error("Failed to update user information in the database.");
            throw new UserServiceException("Failed to update user information in the database", exception);
        }
    }

    @Override
    @Transactional
    public boolean deleteById(final Integer id) throws UserServiceException {
        logger.info("Deleting a user from the database: [id={}].", id);

        try {
            UserValidator.validateId(id);

            if(this.userRepository.existsById(id)) {
                userRepository.deleteById(id);

                logger.info("The user with ID {} was deleted from the database.", id);

                return true;
            }

            return false;
        }
        catch(Exception exception) {
            logger.error("Failed to delete a user information from the database.");
            throw new UserServiceException("Failed to delete a user information from the database", exception);
        }
    }
}
