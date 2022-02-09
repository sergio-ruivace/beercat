package br.com.sergioruivace.beercat.controller;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.sergioruivace.beercat.model.Beer;
import br.com.sergioruivace.beercat.model.Manufacturer;
import br.com.sergioruivace.beercat.repository.BeerRepository;
import br.com.sergioruivace.beercat.repository.ManufacturerRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/beers")
@Tag(name = "Beer", description = "The beer API")
public class BeerController {
	
	@Autowired
	private BeerRepository repository;
	
	@Autowired
	private ManufacturerRepository manufacturerRepository;

	@GetMapping
	@Operation(description = "This API will return a beer page. The sortDirection need to be ASC or DESC, sortField need to be a field from beer")
	public ResponseEntity<Page<Beer>> list(@RequestParam(defaultValue = "0") int page, 
			@RequestParam(defaultValue = "10") int size, 
			@RequestParam(defaultValue = "name") String sortField, 
			@RequestParam(defaultValue = "asc") String sortDirection) {
		
		try {
			Direction direction = Direction.fromString(sortDirection);
			
			Pageable pageable = PageRequest.of(page, size, direction, sortField);
			Page<Beer> list = repository.findAll(pageable);	
			
			return ResponseEntity.ok(list);	
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		} 	
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
