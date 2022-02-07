package br.com.sergioruivace.beercat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public @Data @AllArgsConstructor @NoArgsConstructor class Manufacturer {
	private Long id;
	private String name;
	private String nationality;
}
