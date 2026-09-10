package pl.visa.dndCM.avatar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvatarSkillProficiencyRepository extends JpaRepository<AvatarSkillProficiency, Long> {

    void deleteByAvatar(Avatar avatar);

    List<AvatarSkillProficiency> findByAvatar_Id(Long avatarId);
}
