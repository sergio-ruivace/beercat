package br.com.sergioruivace.beercat.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

import br.com.sergioruivace.beercat.client.PunkApiClient;
import br.com.sergioruivace.beercat.dto.ExternalManufacturerDTO;
import br.com.sergioruivace.beercat.model.Beer;
import br.com.sergioruivace.beercat.model.Manufacturer;
import br.com.sergioruivace.beercat.model.User;
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
		
	@Autowired
	private PunkApiClient punkApiClient;
	

	@GetMapping
	@Operation(description = "This API will return a beer page. The sortDirection need to be ASC or DESC, sortField need to be a field from beer, search could be any field from beer and manufacturer except id.")
	public ResponseEntity<Page<Beer>> list(@RequestParam(defaultValue = "0") int page, 
			@RequestParam(defaultValue = "10") int size, 
			@RequestParam(defaultValue = "name") String sortField, 
			@RequestParam(defaultValue = "asc") String sortDirection,
			@RequestParam(required = false) String search) {
		
		try {
			Direction direction = Direction.fromString(sortDirection);
			
			Pageable pageable = PageRequest.of(page, size, direction, sortField);
			Page<Beer> list = null;
			if(search != null && !search.isEmpty()) {
				list = repository.findAll(search, pageable);	
			}
			else {
				list = repository.findAll(pageable);
			}
			if (list == null || list.isEmpty()) {
				page++;
				List<ExternalManufacturerDTO> dtos = punkApiClient.list(page, size, search);
				list = new PageImpl<Beer>(ExternalManufacturerDTO.convertList(dtos));
			}
			
			return ResponseEntity.ok(list);	
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		} 	
	}
		
	
	@PostMapping
	public ResponseEntity<Beer> create(@RequestBody Beer form, UriComponentsBuilder uriBuilder, Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		
		if(!user.canEdit(form)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
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
	public ResponseEntity<Beer> update(@PathVariable Long id, @RequestBody Beer form, Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		
		Optional<Beer> optional = repository.findById(id);
		if (optional.isPresent()) {
			Beer toUpdate = optional.get();	
			
			if(!user.canEdit(toUpdate)) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}	
			Manufacturer newManufacturer = null;
			if(form.getManufacturer() != null && form.getManufacturer().getId() != null && user.isAdmin()) {
				Optional<Manufacturer> manufacturer = manufacturerRepository.findById(form.getManufacturer().getId());
				if (manufacturer.isPresent()) {
					newManufacturer = manufacturer.get();
				}
			}			
			toUpdate.update(form, newManufacturer);
			repository.save(toUpdate);
			return ResponseEntity.ok(toUpdate);	
		}		
		return ResponseEntity.notFound().build();		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> remove(@PathVariable Long id, Authentication authentication) {	
		User user = (User) authentication.getPrincipal();
		
		Optional<Beer> optional = repository.findById(id);
		if (optional.isPresent()) {
			if(!user.canEdit(optional.get())) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}			
			repository.deleteById(id);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();		
		
	}
}
