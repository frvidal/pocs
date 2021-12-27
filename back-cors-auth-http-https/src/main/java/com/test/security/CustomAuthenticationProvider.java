package com.test.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
 
	@Override
	public Authentication authenticate(Authentication authentication)  {
  
		String name = authentication.getName();
		String password = authentication.getCredentials().toString();
		 
		if ( ("myTestUser".equals(name)) && ("myTestPass".equals(password))) {
			System.out.println (name + " " + password + " is connected");
			List<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority("ROLE_TRUSTED_CLIENT"));
			return new UsernamePasswordAuthenticationToken(name, password, authorities);
		} else {
			System.out.println (name + " " + password + " is NOT connected");
			throw new BadCredentialsException(String.format("Invalid login %s", name));
		}
	}
 
	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	
}