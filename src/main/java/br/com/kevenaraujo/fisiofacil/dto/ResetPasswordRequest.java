package br.com.kevenaraujo.fisiofacil.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResetPasswordRequest {
    @NotBlank(message = "Token e obrigatorio")
    private String token;

    @NotBlank(message = "Nova senha e obrigatoria")
    @Size(min = 8, message = "Senha deve ter no minimo 8 caracteres")
    private String novaSenha;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }
}
