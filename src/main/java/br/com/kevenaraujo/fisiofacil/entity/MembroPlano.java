package br.com.kevenaraujo.fisiofacil.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity

public class MembroPlano {

    public static List<MembroPlano> findByPlanoId(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "membro_id")
    private Membro membro;

    @ManyToOne
    @JoinColumn(name = "plano_id")
    private Planos plano;
}
