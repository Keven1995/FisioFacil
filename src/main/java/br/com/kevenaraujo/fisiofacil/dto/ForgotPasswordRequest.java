package br.com.kevenaraujo.fisiofacil.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ForgotPasswordRequest {
    @Email(message = "E-mail invalido")
    @NotBlank(message = "E-mail e obrigatorio")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
