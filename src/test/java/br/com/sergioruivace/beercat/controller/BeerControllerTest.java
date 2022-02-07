package br.com.sergioruivace.beercat.controller;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.sergioruivace.beercat.model.Beer;
import br.com.sergioruivace.beercat.model.BeerType;
import br.com.sergioruivace.beercat.model.Manufacturer;

@WebMvcTest(BeerController.class)
@AutoConfigureMockMvc
public class BeerControllerTest {
	
	@Autowired
    private MockMvc mvc;
	
	@Autowired
    private ObjectMapper mapper;

	@Test
	void list() throws Exception {
		RequestBuilder request = get("/beers")
				.contentType(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].name", is("name")));    
        
	}
	
	@Test
	void create() throws Exception {
		Manufacturer manufacturer = new Manufacturer(null, "Ambev", "Brazil");
		Beer beer = new Beer(null, "name", "description", 4.5F, BeerType.IPA, manufacturer);
		
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
		Manufacturer manufacturer = new Manufacturer(1l, "Ambev", "Brazil");
		Beer beer = new Beer(1l, "name", "description", 4.5F, BeerType.IPA, manufacturer);
		
		RequestBuilder request = get("/beers/" + beer.getId())
				.contentType(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name", is(beer.getName())))
			.andExpect(jsonPath("$.manufacturer.name", is(manufacturer.getName())));  
        
	}
	
	@Test
	void update() throws Exception {
		Manufacturer manufacturer = new Manufacturer(1l, "Ambev", "Brazil");
		Beer beer = new Beer(1l, "name", "description", 4.5F, BeerType.IPA, null);
		
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
		
		RequestBuilder request = delete("/beers/" + beer.getId())
				.contentType(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(status().isNoContent());    
        
	}
}
