package br.com.kevenaraujo.fisiofacil.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.kevenaraujo.fisiofacil.entity.Membro;
import br.com.kevenaraujo.fisiofacil.repository.MembroRepository;

@Service
public class MembroService {

    private final MembroRepository membroRepository;

    public MembroService(MembroRepository membroRepository) {
        this.membroRepository = membroRepository;
    }

    public List<Membro> listarMembrosPorCategoria(Long categoriaId) {
        return membroRepository.findByCategoriaId(categoriaId);
    }

    public Optional<Membro> buscarPorId(Long id) {
        return membroRepository.findById(id);
    }
}
