package br.com.sergioruivace.beercat.controller;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
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

import br.com.sergioruivace.beercat.model.Manufacturer;
import br.com.sergioruivace.beercat.model.User;
import br.com.sergioruivace.beercat.repository.BeerRepository;
import br.com.sergioruivace.beercat.repository.ManufacturerRepository;
import br.com.sergioruivace.beercat.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/manufacturers")
@Tag(name = "Manufacturer", description = "The manufacturer API")
public class ManufacturerController {

	@Autowired
	private ManufacturerRepository repository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BeerRepository beerRepository;
	
	@GetMapping
	@Operation(description = "This API will return a manufacturer page. The sortDirection need to be ASC or DESC, sortField need to be a field from manufacturer, search could be any field from manufacturer except id.")
	public ResponseEntity<Page<Manufacturer>> list(@RequestParam(defaultValue = "0") int page, 
			@RequestParam(defaultValue = "10") int size, 
			@RequestParam(defaultValue = "name") String sortField, 
			@RequestParam(defaultValue = "asc") String sortDirection,
			@RequestParam(required = false) String search) {
		try {
			Direction direction = Direction.fromString(sortDirection);
			
			Pageable pageable = PageRequest.of(page, size, direction, sortField);
			Page<Manufacturer> list = null;
			if(search != null && !search.isEmpty()) {
				list = repository.findAll(search, pageable);	
			}
			else {
				list = repository.findAll(pageable);
			}				
			return ResponseEntity.ok(list);

		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		} 	
	
	}
		
	
	@PostMapping
	public ResponseEntity<Manufacturer> create(@RequestBody Manufacturer form, UriComponentsBuilder uriBuilder, Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		if(!user.canEdit(form)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
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
	public ResponseEntity<Manufacturer> update(@PathVariable Long id, @RequestBody Manufacturer form, Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		
		Optional<Manufacturer> optional = repository.findById(id);
		if (optional.isPresent()) {
			Manufacturer toUpdate = optional.get();
			if(!user.canEdit(toUpdate)) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
			toUpdate.update(form);
			repository.save(toUpdate);
			return ResponseEntity.ok(toUpdate);	
		}		
		return ResponseEntity.notFound().build();	
		
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> remove(@PathVariable Long id, Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		
		Optional<Manufacturer> optional = repository.findById(id);
		if (optional.isPresent()) {
			if(!user.isAdmin()) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}	
			beerRepository.deleteByManufacturerId(id);
			userRepository.deleteByManufacturerId(id);
			repository.deleteById(id);			
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
		
	}
	
}
