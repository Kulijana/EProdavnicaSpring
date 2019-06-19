package com.aleksadj.eprodavnicapring.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import model.ADJUloga;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class UserDetailsImpl implements UserDetails {


    private String username;
    private String password;
    private List<ADJUloga> roles;

    @Override
    public Collection<SimpleGrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        for(ADJUloga r:roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_"+r.getIme()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        return password;
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<ADJUloga> getRoles() {
        return roles;
    }

    public void setRoles(List<ADJUloga> roles) {
        this.roles = roles;
    }

}

