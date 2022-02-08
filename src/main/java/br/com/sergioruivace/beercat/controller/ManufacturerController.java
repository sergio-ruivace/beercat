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

import br.com.sergioruivace.beercat.model.Manufacturer;
import br.com.sergioruivace.beercat.repository.ManufacturerRepository;

@RestController
@RequestMapping("/manufacturers")
public class ManufacturerController {

	@Autowired
	private ManufacturerRepository repository;
	
	@GetMapping
	public ResponseEntity<List<Manufacturer>> list() {
		List<Manufacturer> list = repository.findAll();
		
		return ResponseEntity.ok(list);			
	}
		
	
	@PostMapping
	public ResponseEntity<Manufacturer> create(@RequestBody Manufacturer form, UriComponentsBuilder uriBuilder) {
		repository.save(form);
		URI uri = uriBuilder.path("/manufacturers/{id}").buildAndExpand(form.getId()).toUri();
		return ResponseEntity.created(uri).body(form);	
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Manufacturer> detail(@PathVariable Long id) {
		Optional<Manufacturer> optional = repository.findById(id);
		if (optional.isPresent()) {
			return ResponseEntity.ok(optional.get());
		}
		
		return ResponseEntity.notFound().build();
	
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Manufacturer> update(@PathVariable Long id, @RequestBody Manufacturer form) {
		Optional<Manufacturer> optional = repository.findById(id);
		if (optional.isPresent()) {
			Manufacturer toUpdate = optional.get();
			toUpdate.update(form);
			repository.save(toUpdate);
			return ResponseEntity.ok(toUpdate);	
		}		
		return ResponseEntity.notFound().build();	
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> remove(@PathVariable Long id) {
		Optional<Manufacturer> optional = repository.findById(id);
		if (optional.isPresent()) {
			repository.deleteById(id);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
		
	}
	
}
