package pl.visa.dndCM.gameData.specie;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecieService {

    private final SpecieRepository specieRepository;

    public SpecieService(SpecieRepository specieRepository) {
        this.specieRepository = specieRepository;
    }

    public List<Specie> findAll() {
        return specieRepository.findAll();
    }

}
