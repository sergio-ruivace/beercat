package br.com.sergioruivace.beercat.controller;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.sergioruivace.beercat.model.Beer;
import br.com.sergioruivace.beercat.model.BeerType;
import br.com.sergioruivace.beercat.model.Manufacturer;
import br.com.sergioruivace.beercat.repository.BeerRepository;
import br.com.sergioruivace.beercat.repository.ManufacturerRepository;

@WebMvcTest(BeerController.class)
@AutoConfigureMockMvc
public class BeerControllerTest {
	
	@Autowired
    private MockMvc mvc;
	
	@Autowired
    private ObjectMapper mapper;
	
	@MockBean
	private BeerRepository repository;
	
	@MockBean
	private ManufacturerRepository manufacturerRepository;


	@Test
	void list() throws Exception {
		Manufacturer manufacturer = new Manufacturer(1l, "Heineken", "Netherlands");
		Beer beer = new Beer(1l, "Heineken", "Heineken Lager Beer", 5F, BeerType.PALE_LAGER, manufacturer);		
		List<Beer> list = Arrays.asList(beer, beer, beer);
		
		when(repository.findAll()).thenReturn(list);		
		
		RequestBuilder request = get("/beers")
				.contentType(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].name", is(beer.getName())));    
        
	}
	
	@Test
	void create() throws Exception {
		Manufacturer manufacturer = new Manufacturer(1l, "Heineken", "Netherlands");
		Beer beer = new Beer(1l, "Test Name", "Test Description", 4.5F, BeerType.IPA, manufacturer);
		
		when(repository.save(beer)).thenReturn(beer);	
		
		RequestBuilder request = post("/beers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(beer));
		
		mvc.perform(request)			
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.name", is(beer.getName())))
			.andExpect(jsonPath("$.manufacturer.name", is(manufacturer.getName())));  

        
	}
	
	@Test
	void detail() throws Exception {
		Manufacturer manufacturer = new Manufacturer(1l, "Heineken", "Netherlands");
		Beer beer = new Beer(1l, "Heineken", "Heineken Lager Beer", 5F, BeerType.PALE_LAGER, manufacturer);
		
		when(repository.findById(1l)).thenReturn(Optional.of(beer));
		
		RequestBuilder request = get("/beers/" + beer.getId())
				.contentType(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name", is(beer.getName())))
			.andExpect(jsonPath("$.manufacturer.name", is(manufacturer.getName())));  
        
	}
	
	@Test
	void detail_invalidBeer() throws Exception {
		Long id = 999l;
		
		when(repository.findById(id)).thenReturn(Optional.empty());
		
		RequestBuilder request = get("/beers/" + id)
				.contentType(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(status().is4xxClientError());  
        
	}
	
	@Test
	void update() throws Exception {
		Manufacturer manufacturer = new Manufacturer(1l, "Heineken", "Netherlands");
		Beer beer = new Beer(1l, "Heineken", "Heineken", 5F, BeerType.PALE_LAGER, manufacturer);
		
		when(repository.findById(1l)).thenReturn(Optional.of(beer));
		when(repository.save(beer)).thenReturn(beer);	
		
		RequestBuilder request = put("/beers/" + beer.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(beer));
		
		mvc.perform(request)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name", is(beer.getName())))
			.andExpect(jsonPath("$.manufacturer.name", is(manufacturer.getName())));  
        
	}
	
	@Test
	void update_invalidBeer() throws Exception {
		Long id = 999l;
		Manufacturer manufacturer = new Manufacturer(1l, "Heineken", "Netherlands");
		Beer beer = new Beer(1l, "Heineken", "Heineken", 5F, BeerType.PALE_LAGER, manufacturer);
		
		when(repository.findById(id)).thenReturn(Optional.empty());
		
		RequestBuilder request = put("/beers/" + id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(beer));
		
		mvc.perform(request)
			.andExpect(status().is4xxClientError());  
        
	}
	
	@Test
	void update_invalidManufacturer() throws Exception {
		Long id = 999l;
		Manufacturer manufacturer = new Manufacturer(1l, "Heineken", "Netherlands");
		Beer beer = new Beer(1l, "Heineken", "Heineken", 5F, BeerType.PALE_LAGER, manufacturer);
		
		when(repository.findById(1l)).thenReturn(Optional.of(beer));
		when(manufacturerRepository.findById(id)).thenReturn(Optional.empty());
		
		RequestBuilder request = put("/beers/" + beer.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(beer));
		
		mvc.perform(request)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.name", is(beer.getName())))
		.andExpect(jsonPath("$.manufacturer.name", is(manufacturer.getName())));  
        
	}
	
	@Test
	void remove() throws Exception {
		Beer beer = new Beer(1l, "name", "description", 4.5F, BeerType.IPA, null);
		
		when(repository.findById(1l)).thenReturn(Optional.of(beer));
		
		RequestBuilder request = delete("/beers/" + beer.getId())
				.contentType(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(status().isNoContent());    
        
	}
	
	@Test
	void remove_invalidBeer() throws Exception {
		Long id = 999l;
		when(repository.findById(id)).thenReturn(Optional.empty());
		
		RequestBuilder request = delete("/beers/" + id)
				.contentType(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(status().is4xxClientError());    
        
	}
}
