package br.com.sergioruivace.beercat.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity @Data @AllArgsConstructor @NoArgsConstructor
public class Beer {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String description;
	private Float graduation;
	
	@Enumerated(EnumType.STRING)
	private BeerType type;
	
	@ManyToOne
	private Manufacturer manufacturer;
	
	private String pictureUrl;

	public void update(Beer form, Manufacturer newManufacturer) {
		this.description = form.description;
		this.graduation = form.graduation;
		this.name = form.name;
		this.type = form.type;
		this.pictureUrl = form.getPictureUrl();
		
		if (newManufacturer != null) {
			this.manufacturer = newManufacturer;
		}			
		
	}

}
