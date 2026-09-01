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

        if (userRepository.existsByName(userDTO.getName())) {
            throw new IllegalArgumentException("User already exists");
        }
        userRepository.save(User.builder()
                .name(userDTO.getName())
                .password(PasswordUtil.hashPassword(userDTO.getPassword()))
                .build());
    }

    public Optional<User> findByName(String name) {
        Optional<User> user = userRepository.findByName(name);

        System.out.println("found: " + user.isPresent());

        if (user.isPresent()) {
            System.out.println("id: " + user.get().getId());
            System.out.println("name: " + user.get().getName());
        }

        return user;
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
