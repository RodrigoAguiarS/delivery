package com.rodrigo.delivery.security;

import com.rodrigo.delivery.repository.UsuarioRepository;
import com.rodrigo.delivery.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfigurations {

    private final TokenService tokenService;

    private final UsuarioRepository usuarioRepository;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors() // Adicione esta linha para habilitar o CORS
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/produtos").permitAll()
                .antMatchers(HttpMethod.GET, "/api/produtos/*").permitAll()
                .antMatchers(HttpMethod.POST, "/api/auth").permitAll()
                .antMatchers(HttpMethod.POST, "/api/auth/criar").permitAll()
                .antMatchers(HttpMethod.GET, "/api/actuator/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/tarefas/*").hasRole("MODERADOR")
                .antMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, usuarioRepository), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() throws Exception {
        return (web) -> web.ignoring().antMatchers("/**.html", "/v2/api-docs", "/webjars/**", "/configuration/**", "/swagger-resources/**");
    }

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("1234"));
    }
}