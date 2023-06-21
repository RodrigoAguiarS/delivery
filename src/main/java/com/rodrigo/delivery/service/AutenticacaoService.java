package com.rodrigo.delivery.service;

import com.rodrigo.delivery.domain.Endereco;
import com.rodrigo.delivery.domain.Perfil;
import com.rodrigo.delivery.domain.Pessoa;
import com.rodrigo.delivery.domain.Usuario;
import com.rodrigo.delivery.domain.dto.EnderecoDto;
import com.rodrigo.delivery.domain.dto.PerfilDto;
import com.rodrigo.delivery.domain.dto.UsuarioDto;
import com.rodrigo.delivery.repository.PerfilRepository;
import com.rodrigo.delivery.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AutenticacaoService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;
    private final BuscarEnderecoService buscarEnderecoService;
    private final PasswordEncoder passwordEncoder;
    private final PerfilRepository perfilRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(username);
        if (usuario.isPresent()) {
            return usuario.get();
        }

        throw new UsernameNotFoundException("Dados inválidos!");
    }
    public Usuario criarUsuario(UsuarioDto usuarioDto) {
        Usuario usuario = modelMapper.map(usuarioDto, Usuario.class);

        String cep = usuarioDto.getEndereco().getCep();
        EnderecoDto enderecoCompleto = buscarEnderecoService.buscarEndereco(cep);
        Endereco endereco = modelMapper.map(enderecoCompleto, Endereco.class);
        endereco.setNumeroCasa(usuarioDto.getEndereco().getNumeroCasa());
        usuario.setEndereco(endereco);

        String senhaCriptografada = passwordEncoder.encode(usuarioDto.getSenha());
        usuario.setSenha(senhaCriptografada);

        // Definir perfil padrão para o usuário (por exemplo, "Usuário")
        Optional<Perfil> perfilPadraoOptional = perfilRepository.findByNome("USUARIO");
        Perfil perfilPadrao = perfilPadraoOptional.orElseGet(() -> {
            Perfil novoPerfil = new Perfil();
            novoPerfil.setNome("USUARIO");
            return perfilRepository.save(novoPerfil);
        });
        List<Perfil> perfis = new ArrayList<>();
        perfis.add(perfilPadrao);
        usuario.setPerfis(perfis);

        return usuarioRepository.save(usuario);
    }
    public boolean isPerfilAdmin(String authority) {
        // Verifique se o perfil é de administração usando o perfilRepository
        Optional<Perfil> perfilOptional = perfilRepository.findByNome(authority);
        return perfilOptional.isPresent() && perfilOptional.get().getNome().equals("ADMIN");
    }
}
