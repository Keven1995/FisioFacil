package br.com.kevenaraujo.fisiofacil.dto;

import br.com.kevenaraujo.fisiofacil.entity.Membro;

public class MembroResponseDTO {
    private final Long id;
    private final String nome;

    public MembroResponseDTO(Membro membro) {
        this.id = membro.getId();
        this.nome = membro.getNome();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}
