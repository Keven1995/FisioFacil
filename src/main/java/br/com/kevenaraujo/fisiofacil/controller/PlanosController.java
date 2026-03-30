package br.com.kevenaraujo.fisiofacil.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.kevenaraujo.fisiofacil.dto.MembroPlanoResponseDTO;
import br.com.kevenaraujo.fisiofacil.dto.PlanoResponseDTO;
import br.com.kevenaraujo.fisiofacil.usecase.planos.ListMembrosPorPlanoUseCase;
import br.com.kevenaraujo.fisiofacil.usecase.planos.ListPlanosUseCase;

@RestController
@RequestMapping("/api/planos")
public class PlanosController {

    private final ListPlanosUseCase listPlanosUseCase;
    private final ListMembrosPorPlanoUseCase listMembrosPorPlanoUseCase;

    public PlanosController(
            ListPlanosUseCase listPlanosUseCase,
            ListMembrosPorPlanoUseCase listMembrosPorPlanoUseCase) {
        this.listPlanosUseCase = listPlanosUseCase;
        this.listMembrosPorPlanoUseCase = listMembrosPorPlanoUseCase;
    }

    @GetMapping
    public List<PlanoResponseDTO> listarPlanos() {
        return listPlanosUseCase.execute();
    }

    @GetMapping("/{planoId}/membros")
    public List<MembroPlanoResponseDTO> listarMembrosPorPlano(@PathVariable Long planoId) {
        return listMembrosPorPlanoUseCase.execute(planoId);
    }
}
