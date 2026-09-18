package pl.visa.dndCM.avatar.feat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.visa.dndCM.avatar.Avatar;

import java.util.List;

@Repository
public interface AvatarFeatRepository extends JpaRepository<AvatarFeat, Long> {

    void deleteByAvatar(Avatar avatar);

    List<AvatarFeat> findByAvatar_Id(Long avatarId);
}
