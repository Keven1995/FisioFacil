package br.com.kevenaraujo.fisiofacil.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.kevenaraujo.fisiofacil.entity.Membro;
import br.com.kevenaraujo.fisiofacil.entity.MembroPlano;
import br.com.kevenaraujo.fisiofacil.repository.MembroPlanoRepository;
import br.com.kevenaraujo.fisiofacil.service.MembroService;

@RestController
@RequestMapping("/api/membros")
public class MembroController {

    @Autowired
    private MembroService membroService;
    
    @Autowired
    private MembroPlanoRepository membroPlanoRepository;

    @GetMapping("/{membroId}/planos")
    public List<MembroPlano> listarPlanosDoMembro(@PathVariable Long membroId) {
        return membroPlanoRepository.findByMembroId(membroId);
    }

    @GetMapping("/categoria/{categoriaId}")
    public List<Membro> listarMembrosPorCategoria(@PathVariable Long categoriaId) {
        return membroService.listarMembrosPorCategoria(categoriaId);
    }
}