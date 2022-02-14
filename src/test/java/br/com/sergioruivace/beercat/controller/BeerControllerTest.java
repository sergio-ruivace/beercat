package br.com.sergioruivace.beercat.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.sergioruivace.beercat.BeercatApplication;
import br.com.sergioruivace.beercat.model.Beer;
import br.com.sergioruivace.beercat.model.BeerType;
import br.com.sergioruivace.beercat.model.Manufacturer;
import br.com.sergioruivace.beercat.repository.BeerRepository;

@SpringBootTest(classes = BeercatApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test.properties")
public class BeerControllerTest {
	
	@Autowired
    private MockMvc mvc;
	
	@Autowired
    private ObjectMapper mapper;
	
	@Autowired
	private BeerRepository repository;
	
	@Test
	void list() throws Exception {		
		RequestBuilder request = get("/beers")
				.contentType(MediaType.APPLICATION_JSON);
		
		int dataCount = repository.findAll().size();
		
		mvc.perform(request)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content", hasSize(dataCount)));
        
	}
	
	@Test
	void list_wrongField() throws Exception {
		RequestBuilder request = get("/beers").param("sortField", "xxxx")
				.contentType(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(status().is4xxClientError());    
        
	}
	
	@Test
	void list_wrongDirection() throws Exception {
		RequestBuilder request = get("/beers").param("sortDirection", "xxxx")
				.contentType(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(status().is4xxClientError());    
        
	}
	
	@Test @WithUserDetails("user@beercat.com")
	void create() throws Exception {
		Manufacturer manufacturer = new Manufacturer(1l, null, null);
		Beer beer = new Beer(null, "Test Name", "Test Description", 4.5F, BeerType.IPA, manufacturer, null);
		
	
		RequestBuilder request = post("/beers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(beer));
		
		mvc.perform(request)			
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.name", is(beer.getName())));  

        
	}
	
	@Test @WithUserDetails("user@beercat.com")
	void detail() throws Exception {	
		RequestBuilder request = get("/beers/1")
				.contentType(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(status().isOk());  
        
	}
	
	@Test @WithUserDetails("user@beercat.com")
	void detail_invalidBeer() throws Exception {
		Long id = 999l;
		
		RequestBuilder request = get("/beers/" + id)
				.contentType(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(status().is4xxClientError());  
        
	}
	
	@Test @WithUserDetails("user@beercat.com")
	void update() throws Exception {
		Manufacturer manufacturer = new Manufacturer(1l, "Heineken", "Netherlands");
		Beer beer = new Beer(1l, "Heineken 2", "Heineken 2", 6F, BeerType.PALE_LAGER, manufacturer, null);

		
		RequestBuilder request = put("/beers/" + beer.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(beer));
		
		mvc.perform(request)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name", is(beer.getName())));  
        
	}
	
	@Test @WithUserDetails("user@erdinger.com")
	void update_OtherManufacturer() throws Exception {
		Manufacturer manufacturer = new Manufacturer(1l, "Heineken", "Netherlands");
		Beer beer = new Beer(1l, "Heineken 3", "Heineken 3", 1F, BeerType.PALE_LAGER, manufacturer, null);

		
		RequestBuilder request = put("/beers/" + beer.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(beer));
		
		mvc.perform(request)
			.andExpect(status().is4xxClientError());  
        
	}
	
	@Test @WithUserDetails("user@beercat.com")
	void update_invalidBeer() throws Exception {
		Long id = 999l;
		Manufacturer manufacturer = new Manufacturer(1l, "Heineken", "Netherlands");
		Beer beer = new Beer(id, "Heineken", "Heineken", 5F, BeerType.PALE_LAGER, manufacturer, null);

		
		RequestBuilder request = put("/beers/" + id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(beer));
		
		mvc.perform(request)
			.andExpect(status().is4xxClientError());  
        
	}	

	@Test @WithUserDetails("user@beercat.com")
	void remove() throws Exception {	
		RequestBuilder request = delete("/beers/2")
				.contentType(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(status().isNoContent());    
        
	}
	
	@Test @WithUserDetails("user@beercat.com")
	void remove_invalidBeer() throws Exception {
		Long id = 999l;
		
		RequestBuilder request = delete("/beers/" + id)
				.contentType(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(status().is4xxClientError());    
        
	}
}
