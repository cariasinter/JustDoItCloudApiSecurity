package teccr.justdoitcloud.service;

import org.springframework.stereotype.Service;
import teccr.justdoitcloud.data.User;
import teccr.justdoitcloud.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service responsible for user-related business logic.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    // Constructor injection of UserRepository
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Authenticate a user by username. For initial purposes this only looks
     * up the user by username and returns it if present.
     *
     * @param username the username to authenticate
     * @return Optional containing the User if found
     */
    public Optional<User> findByUserName(String username) {
        if (username == null || username.trim().isEmpty()) {
            return Optional.empty();
        }
        return userRepository.findByUserName(username.trim());
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        if (id == null || id < 0) {
            return Optional.empty();
        }
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public User updateUserFields(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    if (updatedUser.getName() != null && !updatedUser.getName().trim().isEmpty()) {
                        existingUser.setName(updatedUser.getName().trim());
                    }
                    if (updatedUser.getEmail() != null && !updatedUser.getEmail().trim().isEmpty()) {
                        existingUser.setEmail(updatedUser.getEmail().trim());
                    }
                    if (updatedUser.getType() != null) {
                        existingUser.setType(updatedUser.getType());
                    }
                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}
