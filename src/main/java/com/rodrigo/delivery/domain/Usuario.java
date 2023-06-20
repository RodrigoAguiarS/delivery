package com.rodrigo.delivery.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Usuario extends Pessoa implements UserDetails {

    private String email;

    @JsonIgnore
    private String senha;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Perfil> perfis = new ArrayList<>();

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return perfis;
    }


    @JsonIgnore
    @Override
    public String getPassword() {
        return senha;
    }


    @JsonIgnore
    @Override
    public String getUsername() {
        return email;
    }


    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }


    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
