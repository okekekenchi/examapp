package com.example.examapp.model;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@SuppressWarnings(value = { "serial" })
public class UserDetailModel extends UserModel implements UserDetails{

	public UserDetailModel(final UserModel userModel){
		super(userModel);
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return getRoleName()
			.stream()
				.map(role->new SimpleGrantedAuthority("ROLE_"+role.getRoleName()))
				.collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return super.getPassword();
	}

	@Override
	public String getUsername() {
		return super.getEmail();
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
