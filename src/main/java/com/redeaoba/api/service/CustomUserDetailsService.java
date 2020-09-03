package com.redeaoba.api.service;

import com.redeaoba.api.model.Usuario;
import com.redeaoba.api.model.enums.AuthType;
import com.redeaoba.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public CustomUserDetailsService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario não identificado"));

        List<GrantedAuthority> authorityListAdmin = AuthorityUtils.createAuthorityList("USER", "ADMIN");
        List<GrantedAuthority> authorityListUser = AuthorityUtils.createAuthorityList("USER");

        return new User(usuario.getEmail(),
                usuario.getSenha(),
                usuario.getAuthType().equals(AuthType.ADMIN) ? authorityListAdmin : authorityListUser);
    }

}
