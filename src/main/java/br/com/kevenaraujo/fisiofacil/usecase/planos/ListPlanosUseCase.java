package br.com.kevenaraujo.fisiofacil.usecase.planos;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.kevenaraujo.fisiofacil.dto.PlanoResponseDTO;
import br.com.kevenaraujo.fisiofacil.service.PlanosService;

@Service
public class ListPlanosUseCase {

    private final PlanosService planosService;

    public ListPlanosUseCase(PlanosService planosService) {
        this.planosService = planosService;
    }

    public List<PlanoResponseDTO> execute() {
        return planosService.listarPlanos().stream()
                .map(PlanoResponseDTO::new)
                .toList();
    }
}
