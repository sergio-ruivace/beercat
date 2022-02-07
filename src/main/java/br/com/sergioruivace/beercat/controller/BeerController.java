package br.com.sergioruivace.beercat.controller;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.sergioruivace.beercat.model.Beer;
import br.com.sergioruivace.beercat.model.BeerType;
import br.com.sergioruivace.beercat.model.Manufacturer;

@RestController
@RequestMapping("/beers")
public class BeerController {

	@GetMapping
	public ResponseEntity<List<Beer>> list() {
		Manufacturer manufacturer = new Manufacturer(1l, "Ambev", "Brazil");
		Beer beer = new Beer(1l, "name", "description", 4.5F, BeerType.IPA, manufacturer);		
		List<Beer> list = Arrays.asList(beer, beer, beer);	
		
		return ResponseEntity.ok(list);			
	}
		
	
	@PostMapping
	public ResponseEntity<Beer> create(@RequestBody Beer form, UriComponentsBuilder uriBuilder) {
		form.setId(1l);
		
		URI uri = uriBuilder.path("/beers/{id}").buildAndExpand(form.getId()).toUri();
		return ResponseEntity.created(uri).body(form);	
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Beer> detail(@PathVariable Long id) {
		Manufacturer manufacturer = new Manufacturer(1l, "Ambev", "Brazil");
		Beer beer = new Beer(1l, "name", "description", 4.5F, BeerType.IPA, manufacturer);
		
		return ResponseEntity.ok(beer);	
		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Beer> update(@PathVariable Long id, @RequestBody Beer form) {
		Manufacturer manufacturer = new Manufacturer(1l, "Ambev", "Brazil");
		Beer beer = new Beer(1l, "name", "description", 4.5F, BeerType.IPA, manufacturer);
		
		return ResponseEntity.ok(beer);	
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> remove(@PathVariable Long id) {
		return ResponseEntity.noContent().build();
		
	}
	
}
