package pl.visa.dndCM.avatar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Long> {

    List<Avatar> findAllByUser_Id(Long id);

    List<Avatar> findAllByUser_IdAndDraftFalse(Long id);

    List<Avatar> findAllByDraftTrue();

}
