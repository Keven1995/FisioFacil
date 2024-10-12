package br.com.kevenaraujo.fisiofacil.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanoDTO {
    private String nome;
    private String descricao;

    public PlanoDTO(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }
}
