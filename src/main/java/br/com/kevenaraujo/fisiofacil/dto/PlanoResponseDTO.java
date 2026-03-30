package br.com.kevenaraujo.fisiofacil.dto;

import br.com.kevenaraujo.fisiofacil.entity.Planos;

public class PlanoResponseDTO {
    private final Long id;
    private final String nome;
    private final String descricao;
    private final Double preco;

    public PlanoResponseDTO(Planos plano) {
        this.id = plano.getId();
        this.nome = plano.getNome();
        this.descricao = plano.getDescricao();
        this.preco = plano.getPreco();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public Double getPreco() {
        return preco;
    }
}
