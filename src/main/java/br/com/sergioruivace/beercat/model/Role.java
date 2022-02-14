package br.com.sergioruivace.beercat.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;

@Entity @Data
public class Role implements GrantedAuthority{

	private static final long serialVersionUID = 8896373145315911566L;
	public static String ADMIN = "ADMIN";
	public static String USER = "USER";
	

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;

	@Override
	public String getAuthority() {
		return name;
	}

}
