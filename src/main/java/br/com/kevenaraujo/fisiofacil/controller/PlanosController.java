package br.com.kevenaraujo.fisiofacil.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.kevenaraujo.fisiofacil.entity.MembroPlano;
import br.com.kevenaraujo.fisiofacil.entity.Planos;
import br.com.kevenaraujo.fisiofacil.repository.MembroPlanoRepository;
import br.com.kevenaraujo.fisiofacil.service.PlanosService;

@RestController
@RequestMapping("/api/planos")
@CrossOrigin(origins = "https://fisio-facil-front-nas6pg7kr-keven1995s-projects.vercel.app")
public class PlanosController {

    @Autowired
    private PlanosService planosService;

    @Autowired
    private MembroPlanoRepository membroPlanoRepository;

    @GetMapping
    public List<Planos> listarPlanos() {
        return planosService.listarPlanos();
    }

    @GetMapping("/{planoId}/membros")
    public List<MembroPlano> listarMembrosPorPlano(@PathVariable Long planoId) {
        return membroPlanoRepository.findByPlanoId(planoId);
    }
}
