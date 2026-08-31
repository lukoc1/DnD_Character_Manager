package pl.visa.dndCM.user;

import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> findAll() {
        return userRepository.findAll()
                .stream().map(s -> toDTO(s)).toList();
    }

    public void save(RegisterUserDTO userDTO) {
        userRepository.save(User.builder().name(userDTO.getName())
                .password(PasswordUtil.hashPassword(userDTO.getPassword())).build());
    }


    public UserDTO toDTO(User user) {
        return UserDTO.builder().id(user.getId()).name(user.getName())
                .avatarList(user.getAvatarList().stream()
                        .map(a -> a.getName()).toList())
                .build();
    }

    public User toEntity(UserDTO userDTO) {
        return User.builder().id(userDTO.getId()).name(userDTO.getName()).build();
    }


}
