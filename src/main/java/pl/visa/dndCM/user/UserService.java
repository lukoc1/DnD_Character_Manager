package pl.visa.dndCM.user;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> findAll() {
        return userRepository.findAll()
                .stream().map(this::toDTO).toList();
    }

    public void save(RegisterUserDTO userDTO) {
        // prevent duplicate usernames (unique constraint in DB causes 500)
        if (userRepository.findByName(userDTO.getName()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        userRepository.save(User.builder()
                .name(userDTO.getName())
                .password(PasswordUtil.hashPassword(userDTO.getPassword()))
                .build());
    }

    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    public UserDTO toDTO(User user) {
        return UserDTO.builder().id(user.getId()).name(user.getName())
                .avatarList(user.getAvatarList() == null ? List.of() : user.getAvatarList().stream()
                        .map(a -> a.getName()).toList())
                .build();
    }

    public User toEntity(UserDTO userDTO) {
        return User.builder().id(userDTO.getId()).name(userDTO.getName()).build();
    }
}
