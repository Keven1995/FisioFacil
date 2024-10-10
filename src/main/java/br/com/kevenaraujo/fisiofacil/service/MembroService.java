package br.com.kevenaraujo.fisiofacil.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.kevenaraujo.fisiofacil.entity.Membro;
import br.com.kevenaraujo.fisiofacil.repository.MembroRepository;

@Service
public class MembroService {

    @Autowired
    private MembroRepository membroRepository;

    // Método que busca os membros de uma categoria específica
    public List<Membro> listarMembrosPorCategoria(Long categoriaId) {
        return membroRepository.findByCategoriaId(categoriaId);
    }
}