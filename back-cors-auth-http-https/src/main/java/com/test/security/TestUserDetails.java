package com.test.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import com.test.security.CustomGrantedAuthority;


public class TestUserDetails implements UserDetails {

	private List<CustomGrantedAuthority> authorities = new ArrayList<>();

	public Collection<? extends GrantedAuthority> getAuthorities() {
		authorities.add(new CustomGrantedAuthority());
		return authorities;
	}
  
	@Override
	public String getPassword() {
		return "myTestPass";
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