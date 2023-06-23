package com.rodrigo.delivery.rest;

import com.rodrigo.delivery.domain.NewEmailRequest;
import com.rodrigo.delivery.domain.PasswordResetToken;
import com.rodrigo.delivery.domain.Usuario;
import com.rodrigo.delivery.domain.dto.NewSenhaRequest;
import com.rodrigo.delivery.repository.PasswordResetTokenRepository;
import com.rodrigo.delivery.repository.UsuarioRepository;
import com.rodrigo.delivery.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
public class PasswordResetController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetService passwordResetService;

    @PostMapping("/login-alterar")
    public ResponseEntity<String> requestPasswordReset(@RequestBody NewEmailRequest request) {
        String uid = UUID.randomUUID().toString();

        PasswordResetToken token = new PasswordResetToken(request.getEmail(), uid, true);
        passwordResetTokenRepository.save(token);

        String resetPasswordUrl = "http://localhost:4200/login-alterar/" + uid;
        String content = "Olá,\n\nClique no link a seguir para redefinir sua senha: " + resetPasswordUrl;

        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmailIgnoreCase(request.getEmail());
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            CompletableFuture.runAsync(() -> {
                passwordResetService.sendEmail(usuario.getEmail(), "Recuperação de senha", content);
            });
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login-alterar/{uid}")
    public ResponseEntity<String> showResetPasswordPage(@PathVariable("uid") String uid) {
        Optional<PasswordResetToken> tokenOptional = passwordResetTokenRepository.findByUidAndAtivo(uid, true);
        if (tokenOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(uid);
        }
    }

    @PostMapping("/login-alterar/{uid}")
    public ResponseEntity<String> updatePassword(@PathVariable("uid") String uid, @RequestBody NewSenhaRequest request) {
        Optional<PasswordResetToken> tokenOptional = passwordResetTokenRepository.findByUidAndAtivo(uid, true);
        if (tokenOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("UID inválido ou expirado.");
        }

        PasswordResetToken token = tokenOptional.get();
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(token.getEmail());
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            String senhaCriptografada = passwordEncoder.encode(request.getSenha());
            usuario.setSenha(senhaCriptografada);
            usuarioRepository.save(usuario);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar a senha. Usuário não encontrado.");
        }

        token.setAtivo(false);
        passwordResetTokenRepository.save(token);

        return ResponseEntity.ok().build();
    }
}




