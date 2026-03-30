package br.com.kevenaraujo.fisiofacil.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.kevenaraujo.fisiofacil.entity.Planos;
import br.com.kevenaraujo.fisiofacil.repository.PlanosRepository;

@Service
public class PlanosService {
    private final PlanosRepository planoRepository;

    public PlanosService(PlanosRepository planoRepository) {
        this.planoRepository = planoRepository;
    }

    public List<Planos> listarPlanos() {
        return planoRepository.findAll();
    }

    public Optional<Planos> buscarPorId(Long id) {
        return planoRepository.findById(id);
    }
}
