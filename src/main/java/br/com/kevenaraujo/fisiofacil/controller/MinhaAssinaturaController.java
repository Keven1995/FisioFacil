package br.com.kevenaraujo.fisiofacil.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.kevenaraujo.fisiofacil.dto.IniciarExercicioRequest;
import br.com.kevenaraujo.fisiofacil.dto.MinhaAssinaturaResponseDTO;
import br.com.kevenaraujo.fisiofacil.dto.SelecionarAssinaturaRequest;
import br.com.kevenaraujo.fisiofacil.usecase.assinatura.GetMinhaAssinaturaUseCase;
import br.com.kevenaraujo.fisiofacil.usecase.assinatura.IniciarExercicioUseCase;
import br.com.kevenaraujo.fisiofacil.usecase.assinatura.SelectMinhaAssinaturaUseCase;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/minha-assinatura")
public class MinhaAssinaturaController {

    private final SelectMinhaAssinaturaUseCase selectMinhaAssinaturaUseCase;
    private final GetMinhaAssinaturaUseCase getMinhaAssinaturaUseCase;
    private final IniciarExercicioUseCase iniciarExercicioUseCase;

    public MinhaAssinaturaController(
            SelectMinhaAssinaturaUseCase selectMinhaAssinaturaUseCase,
            GetMinhaAssinaturaUseCase getMinhaAssinaturaUseCase,
            IniciarExercicioUseCase iniciarExercicioUseCase) {
        this.selectMinhaAssinaturaUseCase = selectMinhaAssinaturaUseCase;
        this.getMinhaAssinaturaUseCase = getMinhaAssinaturaUseCase;
        this.iniciarExercicioUseCase = iniciarExercicioUseCase;
    }

    @PostMapping("/selecionar")
    public ResponseEntity<Map<String, String>> selecionarAssinatura(
            Authentication authentication,
            @Valid @RequestBody SelecionarAssinaturaRequest request) {
        selectMinhaAssinaturaUseCase.execute(authentication.getName(), request);
        return ResponseEntity.ok(Map.of("message", "Assinatura selecionada com sucesso"));
    }

    @GetMapping
    public MinhaAssinaturaResponseDTO getMinhaAssinatura(Authentication authentication) {
        return getMinhaAssinaturaUseCase.execute(authentication.getName());
    }

    @PostMapping("/exercicios/iniciar")
    public ResponseEntity<Map<String, String>> iniciarExercicio(
            Authentication authentication,
            @Valid @RequestBody IniciarExercicioRequest request) {
        iniciarExercicioUseCase.execute(authentication.getName(), request.exercicioKey());
        return ResponseEntity.ok(Map.of("message", "Exercicio marcado como iniciado"));
    }
}
