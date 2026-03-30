package br.com.kevenaraujo.fisiofacil.usecase.planos;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.kevenaraujo.fisiofacil.dto.MembroPlanoResponseDTO;
import br.com.kevenaraujo.fisiofacil.repository.MembroPlanoRepository;

@Service
public class ListMembrosPorPlanoUseCase {

    private final MembroPlanoRepository membroPlanoRepository;

    public ListMembrosPorPlanoUseCase(MembroPlanoRepository membroPlanoRepository) {
        this.membroPlanoRepository = membroPlanoRepository;
    }

    public List<MembroPlanoResponseDTO> execute(Long planoId) {
        return membroPlanoRepository.findByPlanoId(planoId).stream()
                .map(MembroPlanoResponseDTO::new)
                .toList();
    }
}
