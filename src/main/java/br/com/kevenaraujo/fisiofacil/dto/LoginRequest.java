package br.com.kevenaraujo.fisiofacil.dto;

public class LoginRequest {
    private String email;
    private String password;

    // Construtor
    public LoginRequest() {}

    // Getters e Setters
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
