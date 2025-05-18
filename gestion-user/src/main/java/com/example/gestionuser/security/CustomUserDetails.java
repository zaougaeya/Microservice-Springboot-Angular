package com.example.gestionuser.security;

import com.example.gestionuser.model.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> user.getRole().name());
    }

    @Override public String getPassword() { return user.getPassworduser(); }
    @Override public String getUsername() { return user.getMailuser(); }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    public String getId() { return user.getId(); }
    public com.example.gestionuser.model.Role getRole() { return user.getRole(); }
}
