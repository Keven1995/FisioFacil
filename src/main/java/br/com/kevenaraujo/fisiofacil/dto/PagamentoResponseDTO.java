package br.com.kevenaraujo.fisiofacil.dto;

import java.util.List;

import br.com.kevenaraujo.fisiofacil.entity.Membro;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagamentoResponseDTO {
    private String descricao;
    private List<Membro> membrosSuperiores;
    private List<Membro> membrosInferiores;

    public PagamentoResponseDTO(String descricao) {
        this.descricao = descricao;
    }
}
