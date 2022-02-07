package br.com.sergioruivace.beercat.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.sergioruivace.beercat.model.Beer;
import br.com.sergioruivace.beercat.model.BeerType;

@RestController
public class BeerController {

	@RequestMapping(path = "/beers", method = RequestMethod.GET)
	public ResponseEntity<List<Beer>> getBeers() {
		Beer beer = new Beer("name", "description", 4.5F, BeerType.IPA, null);
		
		List<Beer> list = Arrays.asList(beer, beer, beer);	
		
		return ResponseEntity.ok(list);			
	}
	
}
