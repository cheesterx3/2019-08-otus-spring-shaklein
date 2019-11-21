package ru.otus.study.spring.librarymvc.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.otus.study.spring.librarymvc.domain.Role;
import ru.otus.study.spring.librarymvc.domain.User;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

public class CustomUserPrincipal implements UserDetails {
    private final User user;

    CustomUserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(Role::getRole)
                .map(SimpleGrantedAuthority::new)
                .distinct()
                .collect(toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getDescription() {
        return user.getName();
    }

    public User getUser(){
        return user;
    }
}
