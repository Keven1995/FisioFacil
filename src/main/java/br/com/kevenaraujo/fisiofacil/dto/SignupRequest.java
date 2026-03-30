package br.com.kevenaraujo.fisiofacil.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignupRequest {
    @Email(message = "E-mail invalido")
    @NotBlank(message = "E-mail e obrigatorio")
    private String email;

    @NotBlank(message = "Senha e obrigatoria")
    @Size(min = 8, message = "Senha deve ter no minimo 8 caracteres")
    private String senha;

    @NotBlank(message = "Nome de usuario e obrigatorio")
    private String nomeUsuario;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }
}
