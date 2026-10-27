package com.authentication.controller;

import com.authentication.domain.entity.Usuario;
import com.authentication.domain.enums.UserRole;
import com.authentication.repository.ProdutoRepository;
import com.authentication.repository.UsuarioRepository;
import com.authentication.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityIntegrationTest {

    private static final String PRODUCT_JSON = "{\"nome\":\"Keyboard\",\"preco\":199.9}";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenService tokenService;

    private Usuario admin;
    private Usuario user;

    @BeforeEach
    void setUp() {
        produtoRepository.deleteAll();
        usuarioRepository.deleteAll();
        admin = usuarioRepository.save(newUser("admin", UserRole.ADMIN));
        user = usuarioRepository.save(newUser("user", UserRole.USUARIO));
    }

    @Test
    void loginWithValidCredentialsReturnsToken() throws Exception {
        mockMvc.perform(post("/autenticador/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"admin\",\"senha\":\"secret123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void loginWithWrongPasswordReturns401() throws Exception {
        mockMvc.perform(post("/autenticador/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"admin\",\"senha\":\"wrong\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.path").value("/autenticador/login"));
    }

    @Test
    void loginWithBlankFieldsReturns400() throws Exception {
        mockMvc.perform(post("/autenticador/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"\",\"senha\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.login").exists())
                .andExpect(jsonPath("$.fieldErrors.senha").exists());
    }

    @Test
    void createProductIsForbiddenForRegularUser() throws Exception {
        mockMvc.perform(post("/produto")
                        .header("Authorization", bearer(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PRODUCT_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));
    }

    @Test
    void createProductIsAllowedForAdmin() throws Exception {
        mockMvc.perform(post("/produto")
                        .header("Authorization", bearer(admin))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PRODUCT_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").value("Keyboard"));
    }

    @Test
    void invalidTokenReturns401() throws Exception {
        mockMvc.perform(get("/produto/1").header("Authorization", "Bearer not-a-valid-token"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    void validTokenForDeletedUserReturns401() throws Exception {
        String token = bearer(user);
        usuarioRepository.delete(user);

        mockMvc.perform(get("/produto/1").header("Authorization", token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void missingProductReturns404() throws Exception {
        mockMvc.perform(get("/produto/999").header("Authorization", bearer(user)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product 999 not found"));
    }

    private Usuario newUser(String login, UserRole role) {
        return Usuario.builder().login(login).senha(passwordEncoder.encode("secret123")).role(role).build();
    }

    private String bearer(Usuario usuario) {
        return "Bearer " + tokenService.gerarToken(usuario);
    }
}
