package br.com.kevenaraujo.fisiofacil.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.kevenaraujo.fisiofacil.dto.PagamentoResponseDTO;
import br.com.kevenaraujo.fisiofacil.entity.Membro;
import br.com.kevenaraujo.fisiofacil.entity.Planos;
import br.com.kevenaraujo.fisiofacil.service.MembroService;
import br.com.kevenaraujo.fisiofacil.service.PlanosService;

@RestController
@RequestMapping("/api/pagamento")
@CrossOrigin(origins = "http://frontend:3000")
public class PagamentoController {

    @Autowired
    private PlanosService planosService;

    @Autowired
    private MembroService membroService;

    @GetMapping("/{planoId}/membro/{membroId}")
    public PagamentoResponseDTO getPagamentoDetalhes(@PathVariable Long planoId, @PathVariable Long membroId) {
        Optional<Planos> plano = planosService.buscarPorId(planoId);
        Optional<Membro> membro = membroService.buscarPorId(membroId);

        if (plano.isPresent() && membro.isPresent()) {
            String descricao = "Plano: " + plano.get().getNome() + ", Membro: " + membro.get().getNome();
            PagamentoResponseDTO response = new PagamentoResponseDTO(descricao);

            // Se for o plano Plus, retornar a lista de membros por categoria
            if (plano.get().getNome().equalsIgnoreCase("Plus")) {
                List<Membro> membrosSuperiores = membroService.listarMembrosPorCategoria(1L); // Categoria 1: Membros Superiores
                List<Membro> membrosInferiores = membroService.listarMembrosPorCategoria(2L); // Categoria 2: Membros Inferiores

                response.setMembrosSuperiores(membrosSuperiores);
                response.setMembrosInferiores(membrosInferiores);
            }

            return response;
        } else {
            throw new RuntimeException("Plano ou Membro não encontrado");
        }
    }
}
