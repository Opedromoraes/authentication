package com.authentication.domain.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // Pra conseguir configurar do jeito que quisermos
@RequiredArgsConstructor
public class SecurityConfig {

    // Statefull = armazena as informalções do usuario (da sessão)
    // Stateless = Só faz a autenticação via token (palavra passe para o usuário conseguir fazer as proximas requisiões)

    private final SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // Config que aplica a requisição pra fazer a segurança -> verificar se o usuario está apto a realizar tal requisição (liberar ou não)
        return httpSecurity
                .csrf(csrf -> csrf.disable())// desativando a confiruação padrão do csrf
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //Declarando qual a nossa sessão
                .authorizeHttpRequests(authorize -> authorize //Quais requisições Https que podem ser autenticadas (que podem passar)
                        .requestMatchers(HttpMethod.POST,"/autenticador/login").permitAll()
                        .requestMatchers(HttpMethod.POST,"/usuario/registrar").permitAll()
                        .requestMatchers(HttpMethod.POST, "/produto").hasRole("ADMIN")  // O usuário tem que ter a role ADMIN para poder criar algo no endpoint "produto"
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class) // adicionar um filtro antes das autorizações acima para realizar as verificações do token
                .build();

    }

    @Bean // config indicando de onde precisa pegar o authentication Manager (não entendi direito kk) e fazer a injeção de dependência
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(); // config para criptografar a senha (utiliza algoritimos de Hash "conhecidos")
    }

}
