package br.com.sergioruivace.beercat.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Entity @Data
public class User  implements UserDetails{

	private static final long serialVersionUID = -3004202468292108721L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String email;
	private String password;
	
	@ManyToOne
	private Manufacturer manufacturer;
	
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Role> roles = new ArrayList<>();
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.getRoles();
	}

	@Override
	public String getUsername() {
		return this.getEmail();
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
	
	public boolean isAdmin() {	
		if (roles != null && roles.stream().anyMatch(r -> r.getName().equals(Role.ADMIN))) {
	    	return true;
	    } 
		return false;
	}
	
	public boolean canEdit(Beer newBeer) {
		if(newBeer != null ) {
	    	return canEdit(newBeer.getManufacturer());
	    }
		return false;
	}

	public boolean canEdit(Manufacturer newManufacturer) {
		if(manufacturer != null && newManufacturer != null && manufacturer.getId() == newManufacturer.getId()) {
	    	return true;
	    }
		if(isAdmin()) {
			return true;
		}
		return false;
	}
}
