package br.com.kevenaraujo.fisiofacil.dto;

import java.time.LocalDateTime;

import br.com.kevenaraujo.fisiofacil.entity.Usuario;

public class UsuarioResponseDTO {
    private final Long id;
    private final String email;
    private final String nomeUsuario;
    private final LocalDateTime criadoEm;
    private final LocalDateTime atualizadoEm;

    public UsuarioResponseDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.email = usuario.getEmail();
        this.nomeUsuario = usuario.getNomeUsuario();
        this.criadoEm = usuario.getCriadoEm();
        this.atualizadoEm = usuario.getAtualizadoEm();
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }
}
