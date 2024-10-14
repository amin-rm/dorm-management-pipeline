package tn.esprit.tpfoyer.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer.entity.Chambre;
import tn.esprit.tpfoyer.entity.TypeChambre;
import tn.esprit.tpfoyer.repository.ChambreRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class ChambreServiceImpl implements IChambreService {

    ChambreRepository chambreRepository;

    public List<Chambre> retrieveAllChambres() {
        log.info("In Methode retrieveAllChambres : ");
        List<Chambre> listC = chambreRepository.findAll();
        log.info("Out of retrieveAllChambres : ");

        return listC;
    }

    public Chambre retrieveChambre(Long chambreId) {
        return chambreRepository.findById(chambreId).orElse(null);
    }

    public Chambre addChambre(Chambre c) {
        return chambreRepository.existsById(c.getIdChambre()) ? null : chambreRepository.save(c);
    }

    public Chambre modifyChambre(Chambre c) {

        return chambreRepository.save(c);
    }

    public void removeChambre(Long chambreId) {
        chambreRepository.deleteById(chambreId);
    }



    public List<Chambre> recupererChambresSelonTyp(TypeChambre tc)
    {
        return chambreRepository.findAllByTypeC(tc);
    }


    public Map<String, Integer> chambreStatistics() {

        Integer nbSimple = chambreRepository.findAllByTypeC(TypeChambre.SIMPLE).size();
        Integer nbDouble = chambreRepository.findAllByTypeC(TypeChambre.DOUBLE).size();

        Map<String, Integer> stats = new HashMap<>();
        stats.put("SIMPLE", nbSimple);
        stats.put("DOUBLE", nbDouble);
        stats.put("TOTAL", nbSimple + nbDouble);
        stats.put("SIMPLE %", nbSimple * 100 / (nbDouble + nbSimple));
        stats.put("DOUBLE %", nbDouble * 100 / (nbDouble + nbSimple));

        return stats;
    }


    public Chambre trouverchambreSelonEtudiant(long cin) {
       //

        return chambreRepository.trouverChselonEt(cin);
    }
}
