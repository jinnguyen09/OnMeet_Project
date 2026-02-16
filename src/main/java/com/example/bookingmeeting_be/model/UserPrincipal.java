package com.example.bookingmeeting_be.model;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class UserPrincipal implements UserDetails {
    private static final long serialVersionUID = 1L;
    private Integer userId;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> roles;
    public UserPrincipal(
            Integer userId,
            String email,
            String password,
            Collection<? extends GrantedAuthority> roles) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }
    public static UserPrincipal build(Users user) {
//        List<GrantedAuthority> authorities = List.of(
//                new SimpleGrantedAuthority(role.getName())
//        );
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role: user.getRole()){
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return new UserPrincipal(
                user.getUserId(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public  String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserPrincipal user = (UserPrincipal) o;
        return Objects.equals(userId, user.userId);
    }
    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
