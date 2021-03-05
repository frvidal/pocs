package com.test.security;

import org.springframework.security.core.GrantedAuthority;

import lombok.EqualsAndHashCode;

/**
 * <p>
 * Tixhì custom granted authority.<br/>
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
	 * Empty constructor for serialization purpose.
	 */
	public CustomGrantedAuthority() {
	}
	
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
		this.authority = "ROLE_USER";
	}


	@Override
	public String getAuthority() {
		return authority;
	}

}
