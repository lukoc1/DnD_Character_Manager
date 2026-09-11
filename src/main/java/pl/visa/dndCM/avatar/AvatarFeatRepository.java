package pl.visa.dndCM.avatar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvatarFeatRepository extends JpaRepository<AvatarFeat, Long> {

    void deleteByAvatar(Avatar avatar);

    List<AvatarFeat> findByAvatar_Id(Long avatarId);
}
