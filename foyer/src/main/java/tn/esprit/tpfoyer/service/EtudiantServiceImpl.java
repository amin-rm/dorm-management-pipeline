package tn.esprit.tpfoyer.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer.entity.Etudiant;
import tn.esprit.tpfoyer.repository.EtudiantRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class EtudiantServiceImpl implements IEtudiantService {


    EtudiantRepository etudiantRepository;

    public List<Etudiant> retrieveAllEtudiants() {
        return etudiantRepository.findAll();
    }
    public Etudiant retrieveEtudiant(Long etudiantId) {
        return etudiantRepository.findById(etudiantId).orElse(null);
    }
    public Etudiant addEtudiant(Etudiant etudiant) {
        if (etudiantRepository.existsById(etudiant.getIdEtudiant())) {
            return null; // Return null if the student already exists
        }
        return etudiantRepository.save(etudiant); // Save and return the new Etudiant
    }
    public Etudiant modifyEtudiant(Etudiant c) {
        return etudiantRepository.save(c);
    }
    public void removeEtudiant(Long etudiantId) {
        etudiantRepository.deleteById(etudiantId);
    }
    public Etudiant recupererEtudiantParCin(long cin)
    {
        return etudiantRepository.findEtudiantByCinEtudiant(cin);
    }



}
