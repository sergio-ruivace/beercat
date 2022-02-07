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

import br.com.sergioruivace.beercat.model.Manufacturer;

@WebMvcTest(ManufacturerController.class)
@AutoConfigureMockMvc
public class ManufacturerControllerTest {
	
	@Autowired
    private MockMvc mvc;
	
	@Autowired
    private ObjectMapper mapper;

	@Test
	void list() throws Exception {
		RequestBuilder request = get("/manufacturers")
				.contentType(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].name", is("Ambev")));    
        
	}
	
	@Test
	void create() throws Exception {
		Manufacturer manufacturer = new Manufacturer(null, "Ambev", "Brazil");
		
		RequestBuilder request = post("/manufacturers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(manufacturer));
		
		mvc.perform(request)			
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.name", is(manufacturer.getName())));  

        
	}
	
	@Test
	void detail() throws Exception {
		Manufacturer manufacturer = new Manufacturer(1l, "Ambev", "Brazil");
		
		RequestBuilder request = get("/manufacturers/" + manufacturer.getId())
				.contentType(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name", is(manufacturer.getName())));  
        
	}
	
	@Test
	void update() throws Exception {
		Manufacturer manufacturer = new Manufacturer(1l, "Ambev", "Brazil");
		
		RequestBuilder request = put("/manufacturers/" + manufacturer.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(manufacturer));
		
		mvc.perform(request)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name", is(manufacturer.getName())));  
        
	}
	
	@Test
	void remove() throws Exception {
		Manufacturer manufacturer = new Manufacturer(1l, "Ambev", "Brazil");
		
		RequestBuilder request = delete("/manufacturers/" + manufacturer.getId())
				.contentType(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(status().isNoContent());    
        
	}
}
