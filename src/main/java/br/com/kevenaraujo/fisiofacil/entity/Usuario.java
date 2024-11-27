package br.com.kevenaraujo.fisiofacil.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private String nomeUsuario;

    @Column(updatable = false)
    private java.time.LocalDateTime criadoEm = java.time.LocalDateTime.now();

    private java.time.LocalDateTime atualizadoEm = java.time.LocalDateTime.now();

    // Token para redefinição de senha
    @Column(name = "reset_token", unique = true)
    private String resetToken;

    // Data de expiração do token
    @Column(name = "reset_token_expiration")
    private LocalDateTime resetTokenExpiration;
    
}
