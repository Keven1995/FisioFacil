package br.com.kevenaraujo.fisiofacil.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.kevenaraujo.fisiofacil.dto.MembroResponseDTO;
import br.com.kevenaraujo.fisiofacil.dto.PlanoDTO;
import br.com.kevenaraujo.fisiofacil.entity.MembroPlano;
import br.com.kevenaraujo.fisiofacil.repository.MembroPlanoRepository;
import br.com.kevenaraujo.fisiofacil.service.MembroService;

@RestController
@RequestMapping("/api/membros")

public class MembroController {

    private final MembroService membroService;
    private final MembroPlanoRepository membroPlanoRepository;

    public MembroController(MembroService membroService, MembroPlanoRepository membroPlanoRepository) {
        this.membroService = membroService;
        this.membroPlanoRepository = membroPlanoRepository;
    }

    @GetMapping("/{membroId}/planos")
public List<PlanoDTO> listarPlanosDoMembro(@PathVariable Long membroId) {
    List<MembroPlano> membroPlanos = membroPlanoRepository.findByMembroId(membroId);
    
    return membroPlanos.stream()
            .map(mp -> new PlanoDTO(mp.getPlano().getNome(), mp.getPlano().getDescricao()))
            .collect(Collectors.toList());
}

    @GetMapping("/categoria/{categoriaId}")
    public List<MembroResponseDTO> listarMembrosPorCategoria(@PathVariable Long categoriaId) {
        return membroService.listarMembrosPorCategoria(categoriaId)
                .stream()
                .map(MembroResponseDTO::new)
                .toList();
    }
}
