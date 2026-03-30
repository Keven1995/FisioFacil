package br.com.kevenaraujo.fisiofacil.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "jwtSecret", "test-secret-key-with-at-least-32-chars");
        ReflectionTestUtils.setField(jwtService, "expirationMinutes", 60L);
    }

    @Test
    void generateTokenEExtractSubjectDevemFuncionar() {
        String token = jwtService.generateToken("user@test.com", Map.of("userName", "User"));

        String subject = jwtService.extractSubject(token);

        assertEquals("user@test.com", subject);
        assertTrue(jwtService.isTokenValid(token));
    }

    @Test
    void isTokenValidDeveRetornarFalseQuandoTokenInvalido() {
        assertFalse(jwtService.isTokenValid("token-invalido"));
    }

    @Test
    void generateTokenDeveLancarExcecaoQuandoSecretCurto() {
        ReflectionTestUtils.setField(jwtService, "jwtSecret", "secret-curto");

        assertThrows(IllegalStateException.class,
                () -> jwtService.generateToken("user@test.com", Map.of()));
    }
}
