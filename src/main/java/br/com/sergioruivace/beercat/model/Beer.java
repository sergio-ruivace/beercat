package br.com.sergioruivace.beercat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public @Data @AllArgsConstructor @NoArgsConstructor class Beer {
	private String name;
	private String description;
	private Float graduation;
	private BeerType type;
	private Manufacturer manufacturer;

}
