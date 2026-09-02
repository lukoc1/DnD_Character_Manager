package pl.visa.dndCM.avatar;

import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.visa.dndCM.user.*;

import java.util.List;
import java.util.Optional;

@Service
public class AvatarService {
    private final AvatarRepository avatarRepository;
    private final UserRepository userRepository;

    public AvatarService(AvatarRepository avatarRepository, UserRepository userRepository) {
        this.avatarRepository = avatarRepository;
        this.userRepository = userRepository;
    }

    /// methods

    public AvatarDTO getAvatarById(Long id) {
        return avatarRepository.findById(id).map(s -> toDTO(s)).orElseThrow(() -> new IllegalArgumentException("Avatar not found"));
    }

    public List<AvatarDTO> findAll() {
        return avatarRepository.findAll()
                .stream().map(s -> toDTO(s)).toList();
    }

    public List<AvatarDTO> findAllByUserId(Long id) {
        return avatarRepository.findAllByUser_Id(id).stream()
                .map(a -> toDTO(a)).toList();
    }

    public void save(AvatarDTO avatarDTO, String userName) {

        User user = userRepository.findByName(userName).orElseThrow(() -> new IllegalArgumentException("User not found"));

        Avatar avatar = toEntity(avatarDTO);
        avatar.setUser(user);

        avatarRepository.save(avatar);
    }


    /// utils

    public AvatarDTO toDTO(Avatar avatar) {
        return AvatarDTO.builder()
                .id(avatar.getId())
                .name(avatar.getName())
                .background(avatar.getBackground())
                .className(avatar.getClassName())
                .species(avatar.getSpecies())
                .subclassName(avatar.getSubclassName())
                .userId(avatar.getUser().getId())
                .build();
    }

    public Avatar toEntity(AvatarDTO avatarDTO) {
        return Avatar.builder()
                .id(avatarDTO.getId())
                .name(avatarDTO.getName())
                .background(avatarDTO.getBackground())
                .className(avatarDTO.getClassName())
                .species(avatarDTO.getSpecies())
                .subclassName(avatarDTO.getSubclassName())
                .build();
    }

}