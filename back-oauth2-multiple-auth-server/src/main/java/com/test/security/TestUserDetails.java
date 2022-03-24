package com.test.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class TestUserDetails implements UserDetails {

	private List<GrantedAuthority> authorities = new ArrayList<>();

	public Collection<? extends GrantedAuthority> getAuthorities() {
		authorities.add(new SimpleGrantedAuthority("ROLE_TRUSTED_CLIENT"));
		return authorities;
	}
  
	@Override
	public String getPassword() {
		// This is th BCrypt encoding of myTestPass.
		return "$2a$10$n9tRc.W1GG5FZ/tyqyA.oeMxZsd9iMYHSZi6vpLyXPKOsfq7WyHZ2";
	}
	
	@Override
	public String getUsername() {
		return "myTestUser";
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
}