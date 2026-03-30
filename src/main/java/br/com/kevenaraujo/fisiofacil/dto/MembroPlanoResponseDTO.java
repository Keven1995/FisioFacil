package br.com.kevenaraujo.fisiofacil.dto;

import br.com.kevenaraujo.fisiofacil.entity.MembroPlano;

public class MembroPlanoResponseDTO {
    private final Long membroId;
    private final String membroNome;
    private final Long planoId;
    private final String planoNome;
    private final String planoDescricao;

    public MembroPlanoResponseDTO(MembroPlano membroPlano) {
        this.membroId = membroPlano.getMembro() != null ? membroPlano.getMembro().getId() : null;
        this.membroNome = membroPlano.getMembro() != null ? membroPlano.getMembro().getNome() : null;
        this.planoId = membroPlano.getPlano() != null ? membroPlano.getPlano().getId() : null;
        this.planoNome = membroPlano.getPlano() != null ? membroPlano.getPlano().getNome() : null;
        this.planoDescricao = membroPlano.getPlano() != null ? membroPlano.getPlano().getDescricao() : null;
    }

    public Long getMembroId() {
        return membroId;
    }

    public String getMembroNome() {
        return membroNome;
    }

    public Long getPlanoId() {
        return planoId;
    }

    public String getPlanoNome() {
        return planoNome;
    }

    public String getPlanoDescricao() {
        return planoDescricao;
    }
}
