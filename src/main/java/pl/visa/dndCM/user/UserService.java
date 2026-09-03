package pl.visa.dndCM.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public void save(RegisterUserDTO userDTO) {

        if (userRepository.existsByName(userDTO.getName())) {
            throw new IllegalArgumentException("User already exists");
        }

        userRepository.save(User.builder()
                .name(userDTO.getName())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .role("USER")
                .build());
    }

    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    // utils

    public UserDTO toDTO(User user) {
        return UserDTO.builder().id(user.getId()).name(user.getName())
                .avatarList(user.getAvatarList() == null ? List.of() : user.getAvatarList().stream()
                        .map(a -> a.getName()).toList())
                .build();
    }

    public User toEntity(UserDTO userDTO) {
        return User.builder().id(userDTO.getId()).name(userDTO.getName()).role("USER")
                .build();
    }
}
