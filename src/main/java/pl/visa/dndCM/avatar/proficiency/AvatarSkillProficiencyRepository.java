package pl.visa.dndCM.avatar.proficiency;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.visa.dndCM.avatar.Avatar;

import java.util.List;

@Repository
public interface AvatarSkillProficiencyRepository extends JpaRepository<AvatarSkillProficiency, Long> {

    void deleteByAvatar(Avatar avatar);

    List<AvatarSkillProficiency> findByAvatar_Id(Long avatarId);
}
