package com.authentication.domain.config;

import com.authentication.repository.UsuarioRepository;
import com.authentication.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter { // extende da classe -> filtro que acontece uma vez na requisição

    private final TokenService service;
    private final UsuarioRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        if (token != null){
            var login = service.validarToken(token);
            UserDetails user = repository.findByLogin(login);

            var authentication = new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication); // fez a validação e a salvou no contexto do usuário
        }
        filterChain.doFilter(request,response); // chamando o próximo filtro (pois já foi finalizado o que era necessário realizar)
    }

    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization"); // onde o usuário vai passar o token
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ",""); // substituição ocorre para substituir o valor do Bearer (portador) por somente o valor do token
    }

}
