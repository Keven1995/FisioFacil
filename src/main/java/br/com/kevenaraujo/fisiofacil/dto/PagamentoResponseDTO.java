package br.com.kevenaraujo.fisiofacil.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagamentoResponseDTO {
    private String descricao;
    private List<MembroPagamentoDTO> membrosSuperiores;
    private List<MembroPagamentoDTO> membrosInferiores;

    public PagamentoResponseDTO(String descricao) {
        this.descricao = descricao;
    }
}
