package br.com.sergioruivace.beercat.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import br.com.sergioruivace.beercat.model.Manufacturer;
import br.com.sergioruivace.beercat.repository.BeerRepository;
import br.com.sergioruivace.beercat.repository.ManufacturerRepository;

@RestController
@RequestMapping("/beers")
public class BeerController {
	
	@Autowired
	private BeerRepository repository;
	
	@Autowired
	private ManufacturerRepository manufacturerRepository;

	@GetMapping
	public ResponseEntity<List<Beer>> list() {					
		List<Beer> list = repository.findAll();	
		
		return ResponseEntity.ok(list);			
	}
		
	
	@PostMapping
	public ResponseEntity<Beer> create(@RequestBody Beer form, UriComponentsBuilder uriBuilder) {
		repository.save(form);
		
		URI uri = uriBuilder.path("/beers/{id}").buildAndExpand(form.getId()).toUri();
		return ResponseEntity.created(uri).body(form);	
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Beer> detail(@PathVariable Long id) {
		Optional<Beer> optional = repository.findById(id);
		if (optional.isPresent()) {
			return ResponseEntity.ok(optional.get());
		}
		
		return ResponseEntity.notFound().build();
		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Beer> update(@PathVariable Long id, @RequestBody Beer form) {
		Optional<Beer> optional = repository.findById(id);
		if (optional.isPresent()) {
			Manufacturer newManufacturer = null;
			if(form.getManufacturer() != null && form.getManufacturer().getId() != null) {
				Optional<Manufacturer> manufacturer = manufacturerRepository.findById(form.getManufacturer().getId());
				if (manufacturer.isPresent()) {
					newManufacturer = manufacturer.get();
				}
			}
			
			Beer toUpdate = optional.get();			
			toUpdate.update(form, newManufacturer);
			repository.save(toUpdate);
			return ResponseEntity.ok(toUpdate);	
		}		
		return ResponseEntity.notFound().build();		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> remove(@PathVariable Long id) {
		Optional<Beer> optional = repository.findById(id);
		if (optional.isPresent()) {
			repository.deleteById(id);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();		
		
	}
	
}
