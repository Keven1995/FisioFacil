package br.com.kevenaraujo.fisiofacil.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.kevenaraujo.fisiofacil.dto.PlanoDTO;
import br.com.kevenaraujo.fisiofacil.entity.Membro;
import br.com.kevenaraujo.fisiofacil.entity.MembroPlano;
import br.com.kevenaraujo.fisiofacil.repository.MembroPlanoRepository;
import br.com.kevenaraujo.fisiofacil.service.MembroService;

@RestController
@RequestMapping("/api/membros")
@CrossOrigin(origins = "https://fisio-facil-front-end.vercel.app")

public class MembroController {

    @Autowired
    private MembroService membroService;
    
    @Autowired
    private MembroPlanoRepository membroPlanoRepository;

    @GetMapping("/{membroId}/planos")
public List<PlanoDTO> listarPlanosDoMembro(@PathVariable Long membroId) {
    List<MembroPlano> membroPlanos = membroPlanoRepository.findByMembroId(membroId);
    
    return membroPlanos.stream()
            .map(mp -> new PlanoDTO(mp.getPlano().getNome(), mp.getPlano().getDescricao()))
            .collect(Collectors.toList());
}

    @GetMapping("/categoria/{categoriaId}")
    public List<Membro> listarMembrosPorCategoria(@PathVariable Long categoriaId) {
        return membroService.listarMembrosPorCategoria(categoriaId);
    }
}
