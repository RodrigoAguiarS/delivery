package com.rodrigo.delivery.rest;

import com.rodrigo.delivery.domain.NewEmailRequest;
import com.rodrigo.delivery.domain.PasswordResetToken;
import com.rodrigo.delivery.domain.Usuario;
import com.rodrigo.delivery.domain.dto.NewSenhaRequest;
import com.rodrigo.delivery.repository.PasswordResetTokenRepository;
import com.rodrigo.delivery.repository.UsuarioRepository;
import com.rodrigo.delivery.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PasswordResetController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetService passwordResetService;

    @PostMapping("/reset-password")
    public ResponseEntity<String> requestPasswordReset(@RequestBody NewEmailRequest request) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(request.getEmail());
        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email não encontrado.");
        }

        Usuario usuario = usuarioOptional.get();
        String uid = UUID.randomUUID().toString();

        PasswordResetToken token = new PasswordResetToken(usuario.getEmail(), uid, true);
        passwordResetTokenRepository.save(token);

        String resetPasswordUrl = "http://localhost:8080/reset-password/" + uid;
        String content = "Olá,\n\nClique no link a seguir para redefinir sua senha: " + resetPasswordUrl;
        passwordResetService.sendEmail(usuario.getEmail(), "Recuperação de senha", content);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body("{\"message\": \"Email de recuperação de senha enviado.\"}");
    }

    @GetMapping("/reset-password/{uid}")
    public String showResetPasswordPage(@PathVariable("uid") String uid) {
        Optional<PasswordResetToken> tokenOptional = passwordResetTokenRepository.findByUidAndAtivo(uid, true);
        if (tokenOptional.isEmpty()) {
            return "error";
        } else {
            return "reset-password";
        }
    }

    @PostMapping("/reset-password/{uid}")
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

        return ResponseEntity.ok("Senha atualizada com sucesso.");
    }
}




