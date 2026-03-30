package br.com.kevenaraujo.fisiofacil.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import br.com.kevenaraujo.fisiofacil.service.JwtService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Test
    void endpointProtegidoDeveRetornarUnauthorizedSemToken() throws Exception {
        mockMvc.perform(get("/api/planos"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void endpointProtegidoDeveRetornarOkComTokenValido() throws Exception {
        String token = jwtService.generateToken("user@test.com", Map.of("userName", "User"));

        mockMvc.perform(get("/api/planos")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
