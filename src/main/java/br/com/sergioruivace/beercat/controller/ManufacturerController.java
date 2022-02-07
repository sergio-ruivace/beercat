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

import br.com.sergioruivace.beercat.model.Manufacturer;

@RestController
@RequestMapping("/manufacturers")
public class ManufacturerController {

	@GetMapping
	public ResponseEntity<List<Manufacturer>> list() {
		Manufacturer manufacturer = new Manufacturer(1l, "Ambev", "Brazil");
		List<Manufacturer> list = Arrays.asList(manufacturer, manufacturer, manufacturer);	
		
		return ResponseEntity.ok(list);			
	}
		
	
	@PostMapping
	public ResponseEntity<Manufacturer> create(@RequestBody Manufacturer form, UriComponentsBuilder uriBuilder) {
		form.setId(1l);		
		URI uri = uriBuilder.path("/manufacturers/{id}").buildAndExpand(form.getId()).toUri();
		return ResponseEntity.created(uri).body(form);	
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Manufacturer> detail(@PathVariable Long id) {
		Manufacturer manufacturer = new Manufacturer(1l, "Ambev", "Brazil");
		
		return ResponseEntity.ok(manufacturer);	
		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Manufacturer> update(@PathVariable Long id, @RequestBody Manufacturer form) {
		Manufacturer manufacturer = new Manufacturer(1l, "Ambev", "Brazil");
		
		return ResponseEntity.ok(manufacturer);	
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> remove(@PathVariable Long id) {
		return ResponseEntity.noContent().build();
		
	}
	
}
