package br.com.kevenaraujo.fisiofacil.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @Email(message = "E-mail invalido")
    @NotBlank(message = "E-mail e obrigatorio")
    private String email;

    @NotBlank(message = "Senha e obrigatoria")
    private String password;


    public LoginRequest() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
