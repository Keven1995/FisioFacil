package br.com.kevenaraujo.fisiofacil.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.kevenaraujo.fisiofacil.dto.PagamentoResponseDTO;
import br.com.kevenaraujo.fisiofacil.usecase.pagamento.GetPagamentoDetalhesUseCase;

@RestController
@RequestMapping("/api/pagamento")
public class PagamentoController {

    private final GetPagamentoDetalhesUseCase getPagamentoDetalhesUseCase;

    public PagamentoController(GetPagamentoDetalhesUseCase getPagamentoDetalhesUseCase) {
        this.getPagamentoDetalhesUseCase = getPagamentoDetalhesUseCase;
    }

    @GetMapping("/{planoId}/membro/{membroId}")
    public PagamentoResponseDTO getPagamentoDetalhes(@PathVariable Long planoId, @PathVariable Long membroId) {
        return getPagamentoDetalhesUseCase.execute(planoId, membroId);
    }
}
