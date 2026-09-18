package pl.visa.dndCM.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.visa.dndCM.exception.ErrorCode;
import pl.visa.dndCM.exception.ResourceNotFoundException;
import pl.visa.dndCM.exception.UserAlreadyExistException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDTO> findAll() {
        return userRepository.findAll()
                .stream().map(this::toDTO).toList();
    }

    public UserDTO findByEmail(String email) {
        return userRepository.findByEmail(email).stream()
                .map(this::toDTO)
                .findFirst().orElseThrow(() -> new ResourceNotFoundException(String.format("User %s not found", email), ErrorCode.USER_NOT_FOUND));

    }

    public UserDTO findById(Long id) {
        return userRepository.findById(id).stream()
                .map(this::toDTO)
                .findFirst().orElseThrow(() -> new ResourceNotFoundException(String.format("User %d not found", id), ErrorCode.USER_NOT_FOUND));
    }

    public void save(RegisterUserDTO userDTO) {

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new UserAlreadyExistException(
                    String.format("Email '%s' already exist.", userDTO.getEmail()), ErrorCode.USER_ALREADY_EXIST);
        }

        userRepository.save(User.builder()
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .role(Role.USER)
                .build());
    }

    public Optional<UserDTO> findByEmailOptional(String email) {
        return userRepository.findByEmail(email).stream()
                .map(this::toDTO)
                .findFirst();
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public void update(String currentEmail, EditUserDTO userDTO) {

        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User %s not found", currentEmail), ErrorCode.USER_NOT_FOUND));

        if (!user.getEmail().equals(userDTO.getEmail()) && userRepository.existsByEmail(userDTO.getEmail())) {
            throw new UserAlreadyExistException(String.format("Email '%s' already exist.", userDTO.getEmail()), ErrorCode.USER_ALREADY_EXIST);
        }

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());

        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
            if (userDTO.getPassword().length() < 7 || userDTO.getPassword().length() > 30) {
                throw new IllegalArgumentException("Password length must be between 7 and 30 characters.");
            }
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        userRepository.save(user);
    }

    // utils

    public UserDTO toDTO(User user) {
        return UserDTO.builder().id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .avatarList(user.getAvatarList() == null ? List.of() : user.getAvatarList().stream()
                        .map(a -> a.getName()).toList())
                .build();
    }

    public User toEntity(UserDTO userDTO) {
        return User.builder().id(userDTO.getId())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .build();
    }
}
