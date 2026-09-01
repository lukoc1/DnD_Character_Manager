package pl.visa.dndCM.avatar;

import org.springframework.stereotype.Service;
import pl.visa.dndCM.user.*;

import java.util.List;
import java.util.Optional;

@Service
public class AvatarService {
    private final AvatarRepository avatarRepository;

    public AvatarService(AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
    }


    public List<AvatarDTO> findAll() {
        return avatarRepository.findAll()
                .stream().map(s -> toDTO(s)).toList();
    }

    public void save(AvatarDTO avatarDTO) {
        avatarRepository.save(toEntity(avatarDTO));
    }

    public AvatarDTO toDTO(Avatar avatar) {
        return AvatarDTO.builder()
                .id(avatar.getId())
                .name(avatar.getName()).build();
    }

    public Avatar toEntity(AvatarDTO avatarDTO) {
        return Avatar.builder()
                .id(avatarDTO.getId()).name(avatarDTO.getName()).build();
    }

}