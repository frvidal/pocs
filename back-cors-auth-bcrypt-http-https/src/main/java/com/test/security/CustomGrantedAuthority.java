package com.test.security;

import org.springframework.security.core.GrantedAuthority;

import lombok.EqualsAndHashCode;

/**
 * <p>
 * Fitzhi custom granted authority.<br/>
 * The only difference with {@link org.springframework.security.core.authority.SimpleGrantedAuthority SimpleGrantedAuthority} consists of an empty constructor.
 * </p>
 * @author Fr&eacute;d&eacute;ric VIDAL
 */
@EqualsAndHashCode
public class CustomGrantedAuthority implements GrantedAuthority {

	/**
	 * For serialization purpose.
	 */
	private static final long serialVersionUID = -6159897754284501386L;

	/**
	 * Authority.
	 */
	String authority;
	
	/**
	 * Constructor.
	 * @param authority simple string containing the authority;
	 */
	public CustomGrantedAuthority() {
		super();
		this.authority = "ROLE_TRUSTED_CLIENT";
	}


	@Override
	public String getAuthority() {
		return authority;
	}

}
