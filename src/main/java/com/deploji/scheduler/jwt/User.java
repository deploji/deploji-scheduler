package com.deploji.scheduler.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.jsonwebtoken.Claims;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    @Getter
    @Setter
    private Long id;
    private String username;
    private String password;

    @Getter
    @Setter
    private Role type;

    @Getter
    @Setter
    private Boolean enabled;

    @Getter
    @Setter
    private List<Role> userRoles;

    public User(String username) {
        this.username = username;
    }

    public User(Claims claims) {
        this.username = (String) claims.get("sub");
        this.id = Long.valueOf((Integer) claims.get("uid"));
        this.type = Role.valueOf(((String) claims.get("utp")).toUpperCase());
    }

    @Override
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.userRoles.stream().map(authority -> new SimpleGrantedAuthority(authority.name())).collect(Collectors.toList());
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }
    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }
}

