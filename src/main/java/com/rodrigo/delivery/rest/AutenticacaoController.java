package com.rodrigo.delivery.rest;

import com.rodrigo.delivery.domain.Usuario;
import com.rodrigo.delivery.domain.dto.LoginDto;
import com.rodrigo.delivery.domain.dto.TokenDto;
import com.rodrigo.delivery.domain.dto.UsuarioDto;
import com.rodrigo.delivery.service.AutenticacaoService;
import com.rodrigo.delivery.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:4200/login")
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AutenticacaoController {

    private final AuthenticationManager authManager;
    private final TokenService tokenService;
    private final AutenticacaoService autenticacaoService;

    @PostMapping("/criar")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Usuario criarUsuario(@RequestBody @Valid UsuarioDto usuarioDto) {
        return autenticacaoService.criarUsuario(usuarioDto);
    }
    @PostMapping
    public ResponseEntity<TokenDto> autenticar(@RequestBody @Valid LoginDto form) {
        UsernamePasswordAuthenticationToken dadosLogin = form.converter();
        try {
            Authentication authentication = authManager.authenticate(dadosLogin);
            String token = tokenService.gerarToken(authentication);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token);
            headers.add("access-control-expose-headers", "Authorization");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new TokenDto(token, "Bearer"));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
