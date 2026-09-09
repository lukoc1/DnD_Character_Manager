package pl.visa.dndCM.avatar;

import lombok.AllArgsConstructor;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.visa.dndCM.gameData.background.Background;
import pl.visa.dndCM.gameData.background.BackgroundRepository;
import pl.visa.dndCM.gameData.dndClass.DndClass;
import pl.visa.dndCM.gameData.dndClass.DndClassRepository;
import pl.visa.dndCM.exception.ErrorCode;
import pl.visa.dndCM.exception.ResourceNotFoundException;
import pl.visa.dndCM.gameData.specie.Specie;
import pl.visa.dndCM.gameData.specie.SpecieRepository;
import pl.visa.dndCM.user.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AvatarService {
    private final AvatarRepository avatarRepository;
    private final UserRepository userRepository;
    private final DndClassRepository dndClassRepository;
    private final BackgroundRepository backgroundRepository;
    private final SpecieRepository specieRepository;

    /// methods

    public AvatarDTO getAvatarById(Long id) {
        return avatarRepository.findById(id)
                .map(s -> toDTO(s))
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Avatar id=%s not found", id), ErrorCode.AVATAR_NOT_FOUND));
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

        User user = userRepository.findByName(userName)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User %s not found", userName), ErrorCode.USER_NOT_FOUND));

        Avatar avatar = toEntity(avatarDTO);
        avatar.setUser(user);
        avatar.setLevel(1);

        avatarRepository.save(avatar);
    }

    public void deleteAvatarById(Long id) {
        avatarRepository.deleteById(id);
    }


    /// utils

    public AvatarDTO toDTO(Avatar avatar) {

        return AvatarDTO.builder()
                .id(avatar.getId())
                .name(avatar.getName())
                .backgroundName(avatar.getBackground().getName())
                .backgroundId(avatar.getBackground().getId())
                .dndClassId(avatar.getDndClass().getId())
                .className(avatar.getDndClass().getName())
                .specieName(avatar.getSpecie().getName())
                .specieId(avatar.getSpecie().getId())
                .subclassName(avatar.getSubclassName())
                .level(avatar.getLevel())
                .userId(avatar.getUser().getId())
                .armorClass(avatar.getArmorClass())
                .maxHP(avatar.getMaxHP())
                .currentHP(avatar.getCurrentHP())
                .tempHP(avatar.getTempHP())
                .proficiencyBonus(avatar.getProficiencyBonus())
                .strMod(avatar.getStrMod())
                .strSco(avatar.getStrSco())
                .dexMod(avatar.getDexMod())
                .dexSco(avatar.getDexSco())
                .consMod(avatar.getConsMod())
                .consSco(avatar.getConsSco())
                .intMod(avatar.getIntMod())
                .intSco(avatar.getIntSco())
                .wisMod(avatar.getWisMod())
                .wisSco(avatar.getWisSco())
                .charMod(avatar.getCharMod())
                .charSco(avatar.getCharSco())
                .build();
    }

    public Avatar toEntity(AvatarDTO avatarDTO) {
        return Avatar.builder()
                .id(avatarDTO.getId())
                .name(avatarDTO.getName())
                .background(findBackground(avatarDTO.getBackgroundId()))
                .dndClass(findDndClass(avatarDTO.getDndClassId()))
                .specie(findSpecie(avatarDTO.getSpecieId()))
                .subclassName(avatarDTO.getSubclassName())
                .level(avatarDTO.getLevel())
                .build();
    }

    private DndClass findDndClass(Long dndClassId) {
        if (dndClassId == null) {
            return null;
        }

        return dndClassRepository.findById(dndClassId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("DndClass id=%s not found", dndClassId), ErrorCode.DND_CLASS_NOT_FOUND));
    }

    private Background findBackground(Long backgroundId) {
        if (backgroundId == null) {
            return null;
        }

        return backgroundRepository.findById(backgroundId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Background id=%s not found", backgroundId), ErrorCode.BACKGROUND_NOT_FOUND));
    }

    private Specie findSpecie(Long specieId) {
        if (specieId == null) {
            return null;
        }

        return specieRepository.findById(specieId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Specie id=%s not found", specieId), ErrorCode.SPECIE_NOT_FOUND));
    }

}
